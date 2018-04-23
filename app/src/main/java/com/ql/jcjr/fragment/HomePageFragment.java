package com.ql.jcjr.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.activity.BidDetailActivity;
import com.ql.jcjr.activity.LoginActivity;
import com.ql.jcjr.activity.LoginActivityCheck;
import com.ql.jcjr.activity.MessageActActivity;
import com.ql.jcjr.activity.NoviceExclusiveActivity;
import com.ql.jcjr.activity.RealNameActivity;
import com.ql.jcjr.adapter.HomeZixunListAdapter;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.base.BaseFragment;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.entity.BannerEntity;
import com.ql.jcjr.entity.HomeDataEntity;
import com.ql.jcjr.entity.MessageActEntity;
import com.ql.jcjr.entity.RiskWarningEntity;
import com.ql.jcjr.entity.RollNewsEntity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.UrlUtil;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.IndicatorView;
import com.ql.jcjr.view.NoScrollListView;
import com.ql.jcjr.view.PullToRefreshView;
import com.sunfusheng.marqueeview.MarqueeView;
import com.umeng.analytics.MobclickAgent;
import com.uuch.adlibrary.AdConstant;
import com.uuch.adlibrary.AdManager;
import com.uuch.adlibrary.bean.AdInfo;
import com.uuch.adlibrary.transformer.DepthPageTransformer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class HomePageFragment extends BaseFragment implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterLoadListener {

    @ViewInject(R.id.in_advert)
    private IndicatorView mIndicatorView;
    @ViewInject(R.id.marqueeView)
    private MarqueeView mMarqueeView;
    @ViewInject(R.id.tv_annualized_rate)
    private TextView mTvAnnualizedRate;
    @ViewInject(R.id.tv_term)
    private TextView mTvTerm;
    @ViewInject(R.id.ll_marqueeView)
    private LinearLayout mLlmarquee;
    @ViewInject(R.id.pull_refresh_view)
    private PullToRefreshView mPullRefresh;
    @ViewInject(R.id.tv_gf_total)
    private TextView mTotal;
    @ViewInject(R.id.tv_gf_people)
    private TextView mPeople;
    @ViewInject(R.id.iv_banner)
    private ImageView mIvBanner;
    @ViewInject(R.id.ll_1)
    private LinearLayout mLl;
    @ViewInject(R.id.tv_limit_people)
    private TextView mLimitPeople;
    @ViewInject(R.id.iv_bid_title)
    private ImageView mIvTitle;
    @ViewInject(R.id.btn_bid)
    private Button mBidJump;
    @ViewInject(R.id.iv_0)
    private ImageView iv_0;
    @ViewInject(R.id.iv_1)
    private ImageView iv_1;
    @ViewInject(R.id.iv_2)
    private ImageView iv_2;
    @ViewInject(R.id.iv_3)
    private ImageView iv_3;
    @ViewInject(R.id.tv_0)
    private TextView tv_0;
    @ViewInject(R.id.tv_1)
    private TextView tv_1;
    @ViewInject(R.id.tv_2)
    private TextView tv_2;
    @ViewInject(R.id.tv_3)
    private TextView tv_3;
    @ViewInject(R.id.tv_diffrent_bid)
    private TextView tv_diffrent_bid;
    @ViewInject(R.id.lv_selective_finance)
    private NoScrollListView lv_selective_finance;


    private static final int INDEX_BEGINNER_WELFARE = 0;
    private static final int INDEX_INVITATION = 1;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<BannerEntity.ResultBean> bannerUrlList = new ArrayList<>();
    private List<String> rollNewsTitleList = new ArrayList<>();
    private List<RollNewsEntity.ResultBean> resultBeanList;
    private List<MessageActEntity.ResultBean> mMessageList = new ArrayList<>();

    private String mNoviceExclusiveId;
    private int banner=0;
    private String mBidName;
    private String url0="";
    private String url1="";
    private String url2="";
    private String url3="";
    private HomeZixunListAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_home_page;
    }

    @Override
    protected void initView(View view) {
        ViewUtils.inject(this, view);
        mContext = getActivity();
        mLayoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mTvAnnualizedRate.setTypeface(JcbApplication.getPingFangBoldTypeFace());
        mTvTerm.setTypeface(JcbApplication.getPingFangBoldTypeFace());
        mLimitPeople.setTypeface(JcbApplication.getPingFangBoldTypeFace());
        mPullRefresh.setOnHeaderRefreshListener(this);
        mPullRefresh.setOnFooterLoadListener(this);
        mPullRefresh.removeFootView();
        initListView();
        mIndicatorView.setFocusable(true);
        mIndicatorView.setFocusableInTouchMode(true);
        mIndicatorView.requestFocus();
    }

    private void initListView() {
        mAdapter = new HomeZixunListAdapter(mContext, mMessageList);
        lv_selective_finance.setAdapter(mAdapter);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if(isVisible) {
            banner();
            initMarqueeView();
//            getNoviceExclusive();
            getZiXun();
            getData();

        }
    }

    private void getRiskWarning() {
        SenderResultModel resultModel = ParamsManager.getRisk();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("风险测评 " + responeJson);
                        RiskWarningEntity entity = GsonParser.getParsedObj(responeJson, RiskWarningEntity.class);
                        RiskWarningEntity.ResultBean resultBean = entity.getResult();
                        if(StringUtils.isBlank(resultBean.getType())||resultBean.getType()==null){
                            //未测评
                            UserData.getInstance().setRiskWarning(false);
                        }else {
                            //已测评
                            UserData.getInstance().setRiskWarning(true);
                        }
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("风险测评 " + entity.errorInfo);
                    }

                }, mContext);
    }

    private void getZiXun() {
        SenderResultModel resultModel = ParamsManager.senderMessageAct("1","4",3);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("首页资讯 " + responeJson);
                MessageActEntity entity = GsonParser.getParsedObj(responeJson, MessageActEntity.class);

                mMessageList.clear();
                mMessageList.addAll(entity.getResult());
                mAdapter.notifyDataSetChanged();
                initToset(entity.getResult());
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("首页资讯 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
                mPullRefresh.onHeaderRefreshFinish();
            }

        }, mContext);

    }

    private void initToset(final List<MessageActEntity.ResultBean> result) {
        lv_selective_finance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 Boolean noShare;
                if (result.get(i).getShare().equals("0")){
                    noShare=true;
                }else {
                    noShare=true;
                }
                UrlUtil.showHtmlPage(mContext,result.get(i).getName(),RequestURL.INTERCEPT_ZIXUNDETAIL_URL+result.get(i).getId(),noShare);
            }
        });
    }

    private void getData() {
        SenderResultModel resultModel = ParamsManager.getHomeData();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                if (UserData.getInstance().isLogin()&&!UserData.getInstance().getRiskWarning()){
                    getRiskWarning();
                }

                mIvTitle.setImageResource(R.drawable.icon_xszx_sy);
                LogUtil.i("首页数据获取成功 " + responeJson);
                mPullRefresh.onHeaderRefreshFinish();
                HomeDataEntity entity = GsonParser.getParsedObj(responeJson, HomeDataEntity.class);
                mTotal.setText(entity.getResult().getResult3().getAccount());
                mPeople.setText(entity.getResult().getResult3().getCount());
//                mLimitPeople.setText(entity.getResult().getResult2().getTender_times()+"人");
//                tv_diffrent_bid.setText("累计申购");
                tv_diffrent_bid.setText("限购额度");
                mLimitPeople.setText(entity.getResult().getResult2().getMost_account()+"元");
                mLl.setVisibility(View.VISIBLE);
                if (entity.getResult().getResult1().getCode().equals("1")){
                    banner=1;
                    mIvBanner.setImageResource(R.drawable.smrz_sy);

                }else if (entity.getResult().getResult1().getCode().equals("2")){
                    if (!UserData.getInstance().getRiskWarning()){
                        banner=4;
                        mIvBanner.setImageResource(R.drawable.zcjs_cp);
                    }else {
                        banner = 2;
                        mIvBanner.setImageResource(R.drawable.xszx_sy);
                    }
                }else if (entity.getResult().getResult1().getCode().equals("3")){
                    banner=3;
                    mLl.setVisibility(View.GONE);
                    mIvTitle.setImageResource(R.drawable.title_sy);
                    tv_diffrent_bid.setText("剩余金额");
                    mLimitPeople.setText(entity.getResult().getResult2().getLast_account()+"元");
                }else if (entity.getResult().getResult1().getCode().equals("0")){
                    mIvBanner.setImageResource(R.drawable.zcjs_sy);
                }
                mTvAnnualizedRate.setText(entity.getResult().getResult2().getApr());
                mTvTerm.setText(entity.getResult().getResult2().getTime_limit_day()+"天");



                mNoviceExclusiveId=entity.getResult().getResult2().getId();
                mBidName = entity.getResult().getResult2().getName();
                if (entity.getResult().getResult2().getIsselled().equals("0")){
                    mBidJump.setEnabled(true);
                    mBidJump.setText("立即加入");
                }else {
                    mBidJump.setEnabled(false);
                    mBidJump.setText("已售罄");
                }
                if (entity.getResult().getResult4().size()>=4){
                    initFour(entity.getResult().getResult4());
                }
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("广告栏失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
                mPullRefresh.onHeaderRefreshFinish();
            }

        }, mContext);

    }

    private void initFour(List<HomeDataEntity.ResultBean.ResultBeanFour> entity) {
        tv_0.setText(entity.get(0).getTitle());
        tv_1.setText(entity.get(1).getTitle());
        tv_2.setText(entity.get(2).getTitle());
        tv_3.setText(entity.get(3).getTitle());
        GlideUtil.displayPic(mContext,entity.get(0).getPic(),R.drawable.ptjs_sy,iv_0);
        GlideUtil.displayPic(mContext,entity.get(1).getPic(),R.drawable.yqyl_sy,iv_1);
        GlideUtil.displayPic(mContext,entity.get(2).getPic(),R.drawable.dhzx_sy,iv_2);
        GlideUtil.displayPic(mContext,entity.get(3).getPic(),R.drawable.mrqd_sy,iv_3);
        url0 = entity.get(0).getLink();
        url1 = entity.get(1).getLink();
        url2 = entity.get(2).getLink();
        url3 = entity.get(3).getLink();
    }

    private void banner() {
        SenderResultModel resultModel = ParamsManager.senderBanner();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("广告栏成功 " + responeJson);

                BannerEntity entity = GsonParser.getParsedObj(responeJson, BannerEntity.class);
                bannerUrlList.clear();
                bannerUrlList.addAll(entity.getResult());
                initDialog(entity.getResultTanPing());
                initAdvert(bannerUrlList);
                mPullRefresh.onHeaderRefreshFinish();
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("广告栏失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
                mPullRefresh.onHeaderRefreshFinish();
            }

        }, mContext);
    }

    private void initDialog(List<BannerEntity.ResultBean> resultTanPing) {
        if (resultTanPing.size()==0)return;
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (UserData.getInstance().getDay()==day) return;

        List advList = new ArrayList<>();

        for (int i=0;i<resultTanPing.size();i++) {
            AdInfo adInfo = new AdInfo();
            adInfo.setActivityImg(resultTanPing.get(i).getPic());
            adInfo.setUrl(resultTanPing.get(i).getUrl());
            advList.add(adInfo);
        }
        /**
         * 创建广告活动管理对象
         */
        final AdManager adManager = new AdManager((Activity) mContext, advList);
        adManager.setOverScreen(true)
                .setWidthPerHeight(0.73f)
                .setPageTransformer(new DepthPageTransformer());
/**
 * 执行弹窗的显示操作
 */
        adManager.showAdDialog(AdConstant.ANIM_RIGHT_TO_LEFT);
        UserData.getInstance().setDay(day);

        adManager.setOnImageClickListener(new AdManager.OnImageClickListener() {
            @Override
            public void onImageClick(View view, AdInfo advInfo) {
                UrlUtil.showHtmlPage(mContext,"详情", advInfo.getUrl());
                adManager.dismissAdDialog();
            }
        });

    }

    private void initMarqueeView() {
        getRollNews();
        mMarqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                UrlUtil.showHtmlPage(mContext,"公告详情", RequestURL.NOTICE_DETAILS_URL + resultBeanList.get(position).getId());
            }
        });
    }

    private void getRollNews() {
        SenderResultModel resultModel = ParamsManager.senderRollNews();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                mPullRefresh.onHeaderRefreshFinish();
                LogUtil.i("首页公告成功 " + responeJson);
                RollNewsEntity entity = GsonParser.getParsedObj(responeJson, RollNewsEntity.class);
                resultBeanList = entity.getResult();
                if (resultBeanList.size()==0||resultBeanList==null){
                    mLlmarquee.setVisibility(View.GONE);
                    return;
                }else {
                    mLlmarquee.setVisibility(View.VISIBLE);
                }
                rollNewsTitleList.clear();
                for (int i = 0; i < resultBeanList.size(); i++){
                    rollNewsTitleList.add(resultBeanList.get(i).getName());
                }

                mMarqueeView.startWithList(rollNewsTitleList);
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                mPullRefresh.onHeaderRefreshFinish();
                LogUtil.i("首页公告失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

//    private void getNoviceExclusive() {
//        SenderResultModel resultModel = ParamsManager.senderNoviceExclusive();
//
//        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {
//
//            @Override
//            public void onSuccess(String responeJson) {
//                mPullRefresh.onHeaderRefreshFinish();
//                LogUtil.i("首页年化 " + responeJson);
//                NoviceExclusiveEntity entity = GsonParser.getParsedObj(responeJson,NoviceExclusiveEntity.class);
////                mTvAnnualizedRate.setText(entity.getResult().getApr());
////                mTvLowestAmt.setText(getResources().getString(R.string.str_lowest_account, entity.getResult().getLowestaccount()));
////                mTvTerm.setText(entity.getResult().getBorrowTime().replace("天",""));
//                mNoviceExclusiveId = entity.getResult().getId();
//            }
//
//            @Override
//            public void onFailure(ResponseEntity entity) {
//                mPullRefresh.onHeaderRefreshFinish();
//                LogUtil.i("首页年化失败 " + entity.errorInfo);
//                CommonToast.showHintDialog(mContext, entity.errorInfo);
//            }
//
//        }, mContext);
//    }

    private void initAdvert(final List<BannerEntity.ResultBean> list) {
        List<String> urlList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            urlList.add(list.get(i).getPic());
        }

        List<View> views = new ArrayList<>();
        for (int i = 0; i < urlList.size(); i++) {
            View advertView = mLayoutInflater.inflate(R.layout.item_advert, null);
            ImageView imageView = (ImageView) advertView.findViewById(R.id.image);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (mContext==null) return;
                GlideUtil.displayPic(mContext, urlList.get(i), -1, imageView);

            views.add(advertView);
            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(finalI).getShare().equals("0")){
                        UrlUtil.showHtmlPage(mContext,"详情", list.get(finalI).getUrl(),true);
                    }else {
                        UrlUtil.showHtmlPage(mContext,"详情", list.get(finalI).getUrl());
                    }

                }
            });
        }
        mIndicatorView.setPagerViewList(views);
        mIndicatorView.startAutoPlay();

    }

    /**
     * 停止滑动
     */
    public void stopPlay() {
        if (mIndicatorView == null)
            return;
        mIndicatorView.stopAutoPlay();
    }

    @Override
    public void onResume() {
        super.onResume();
        banner();
        initMarqueeView();
        getZiXun();
        getData();

        if (mIndicatorView == null)
            return;
        mIndicatorView.startAutoPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPlay();
    }

    @OnClick({R.id.btn_bid, R.id.ll_ptjs, R.id.ll_yqyl, R.id.tv_gonggao_more,R.id.ll_dhzx,R.id.ll_mrqd,R.id.ll_1,R.id.tv_zixun_more})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_bid:
                if (!UserData.getInstance().isLogin()){
                    if (UserData.getInstance().getPhoneNumber().equals("")) {
                         intent = new Intent(mContext, LoginActivityCheck.class);
                        startActivity(intent);
                    }else {
                         intent = new Intent(mContext, LoginActivity.class);
                        intent.putExtra("phone_num", UserData.getInstance().getPhoneNumber());
                        startActivity(intent);
                    }
                    break;
                }
                if (banner==3){
                    intent = new Intent(mContext, BidDetailActivity.class);
                    intent.putExtra("bid_title", mBidName);
                    intent.putExtra("bid_id", mNoviceExclusiveId);
                    startActivity(intent);
                }else {
                    intent = new Intent(mContext, NoviceExclusiveActivity.class);
                    intent.putExtra("bid_id",mNoviceExclusiveId);
                    intent.putExtra("bid_title", mBidName);
                    startActivity(intent);
                }
                break;

            case R.id.ll_ptjs:

                UrlUtil.showHtmlPage(mContext,tv_0.getText().toString(), url0,true);

                break;

            case R.id.tv_zixun_more:

                UrlUtil.showHtmlPage(mContext,"积财学堂", RequestURL.INTERCEPT_ZIXUNMORE_URL,true);  //积财学堂

                break;

            case R.id.ll_yqyl:

                    UrlUtil.showHtmlPage(mContext,tv_1.getText().toString(), url1,true);

                break;

            case R.id.ll_dhzx:

                    UrlUtil.showHtmlPage(mContext,tv_2.getText().toString(), url2,true);

                break;

            case R.id.ll_mrqd:

                    UrlUtil.showHtmlPage(mContext,tv_3.getText().toString(), url3,true);

                break;

            case R.id.tv_gonggao_more:
                intent = new Intent();
                intent.setClass(mContext, MessageActActivity.class);
                intent.putExtra("msg_type",1);
                startActivity(intent);
                break;
            case R.id.ll_1:
                if (!UserData.getInstance().isLogin()||banner==0){

                        intent = new Intent(mContext, LoginActivityCheck.class);
                        startActivity(intent);

                    Map<String, String> datas = new HashMap<String, String>();
                    MobclickAgent.onEventValue(mContext, "click_index_getredbag", datas, 1);
                    break;
                }
                if (banner==1){
                    intent = new Intent(mContext, RealNameActivity.class);
                    startActivity(intent);
                }else if (banner==2){
                    intent = new Intent(mContext, NoviceExclusiveActivity.class);
                    intent.putExtra("bid_id",mNoviceExclusiveId);
                    intent.putExtra("bid_title", mBidName);
                    startActivity(intent);
                }else if (banner==4){
                    UrlUtil.showHtmlPage(mContext,"风险测评", RequestURL.RISKTEST_URL,true);
                }
                else {}
                break;
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        banner();
        initMarqueeView();
//        getNoviceExclusive();
        getZiXun();
        getData();

    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        mPullRefresh.onFooterLoadFinish();
        mPullRefresh.setOnHeaderRefreshListener(this);
        mPullRefresh.setOnFooterLoadListener(this);
        mPullRefresh.removeFootView();
    }
}