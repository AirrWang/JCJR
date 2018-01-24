package com.ql.jcjr.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.activity.LoginActivityCheck;
import com.ql.jcjr.activity.MessageActActivity;
import com.ql.jcjr.activity.NoviceExclusiveActivity;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.base.BaseFragment;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.entity.BannerEntity;
import com.ql.jcjr.entity.NoviceExclusiveEntity;
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
import com.ql.jcjr.utils.UrlUtil;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.IndicatorView;
import com.ql.jcjr.view.NoScrollListView;
import com.sunfusheng.marqueeview.MarqueeView;
import com.uuch.adlibrary.AdConstant;
import com.uuch.adlibrary.AdManager;
import com.uuch.adlibrary.bean.AdInfo;
import com.uuch.adlibrary.transformer.DepthPageTransformer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class HomePageFragment extends BaseFragment{

    @ViewInject(R.id.in_advert)
    private IndicatorView mIndicatorView;
    @ViewInject(R.id.iv_fun_xsfl)
    private ImageView mIVFunXsfl;
    @ViewInject(R.id.iv_fun_yqyl)
    private ImageView mIVFunYqyl;
    @ViewInject(R.id.marqueeView)
    private MarqueeView mMarqueeView;
    @ViewInject(R.id.tv_annualized_rate)
    private TextView mTvAnnualizedRate;
    @ViewInject(R.id.lv_selective_finance)
    private NoScrollListView mLvSelectiveFinance;
    @ViewInject(R.id.tv_lowest_amt)
    private TextView mTvLowestAmt;
    @ViewInject(R.id.tv_term)
    private TextView mTvTerm;

    private static final int INDEX_BEGINNER_WELFARE = 0;
    private static final int INDEX_INVITATION = 1;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<BannerEntity.ResultBean> bannerUrlList = new ArrayList<>();
    private List<String> rollNewsTitleList = new ArrayList<>();
    private List<RollNewsEntity.ResultBean> resultBeanList;

    private String mNoviceExclusiveId;

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

        banner();
        initMarqueeView();
        getNoviceExclusive();
    }

    private void banner() {
        SenderResultModel resultModel = ParamsManager.senderBanner();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("广告栏成功 " + responeJson);

                BannerEntity entity = GsonParser.getParsedObj(responeJson, BannerEntity.class);
                bannerUrlList.addAll(entity.getResult());
                initDialog(entity.getResultTanPing());
                initAdvert(bannerUrlList);
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("广告栏失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
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
                LogUtil.i("首页公告成功 " + responeJson);
                RollNewsEntity entity = GsonParser.getParsedObj(responeJson, RollNewsEntity.class);
                resultBeanList = entity.getResult();
                for (int i = 0; i < resultBeanList.size(); i++){
                    rollNewsTitleList.add(resultBeanList.get(i).getName());
                }

                mMarqueeView.startWithList(rollNewsTitleList);
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("首页公告失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

    private void getNoviceExclusive() {
        SenderResultModel resultModel = ParamsManager.senderNoviceExclusive();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("首页年化 " + responeJson);
                NoviceExclusiveEntity entity = GsonParser.getParsedObj(responeJson,NoviceExclusiveEntity.class);
                mTvAnnualizedRate.setText(entity.getResult().getApr());
                mTvLowestAmt.setText(getResources().getString(R.string.str_lowest_account, entity.getResult().getLowestaccount()));
                mTvTerm.setText(entity.getResult().getBorrowTime().replace("天",""));
                mNoviceExclusiveId = entity.getResult().getId();
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("首页年化失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

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
            GlideUtil.displayPic(mContext, urlList.get(i), -1, imageView);
            views.add(advertView);
            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UrlUtil.showHtmlPage(mContext,"详情", list.get(finalI).getUrl());
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
        if (mIndicatorView == null)
            return;
        mIndicatorView.startAutoPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPlay();
    }

    @OnClick({R.id.btn_bid, R.id.iv_fun_xsfl, R.id.iv_fun_yqyl, R.id.tv_gonggao_more})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_bid:
//                final MainActivity mainActivity = (MainActivity) getActivity();
//                mainActivity.mViewPager.setCurrentItem(INDEX_MANAGE_MONEY);
                intent = new Intent(mContext, NoviceExclusiveActivity.class);
                intent.putExtra("bid_id",mNoviceExclusiveId);
                startActivity(intent);
                break;

            case R.id.iv_fun_xsfl:
                UrlUtil.showHtmlPage(mContext,"新手福利", RequestURL.XSFL_URL);
                break;

            case R.id.iv_fun_yqyl:
                if(UserData.getInstance().isLogin()) {
                    UrlUtil.showHtmlPage(mContext,"邀请有礼", RequestURL.YQYL_URL + UserData.getInstance().getUSERID());
                }else {
                    intent = new Intent(mContext, LoginActivityCheck.class);
                    startActivity(intent);
                }
                break;

            case R.id.tv_gonggao_more:
                intent = new Intent();
                intent.setClass(mContext, MessageActActivity.class);
                intent.putExtra("msg_type",1);
                startActivity(intent);
                break;
        }
    }

}