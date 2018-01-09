package com.ql.jcjr.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.activity.LoginActivityCheck;
import com.ql.jcjr.activity.NoviceExclusiveActivity;
import com.ql.jcjr.adapter.AdvertInfoViewPaperAdapterLoop;
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
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.UrlUtil;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.LoopPager.LoopPagerAdapterWrapper;
import com.ql.jcjr.view.LoopPager.LoopViewPager;
import com.ql.jcjr.view.NoScrollListView;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class HomePageFragmentLoop extends BaseFragment{

    @ViewInject(R.id.ll_advert)
    private FrameLayout mLlAdvert;
    @ViewInject(R.id.in_advert)
    private LoopViewPager viewFlow;
    @ViewInject(R.id.ll_viewflowindic)
    private LinearLayout mLlViewflowindic;

    private Timer mTimer=null;
    public int currentIndex=1;
    private Handler mHandler;
    private View[] indicViews;
    private View[] pageViews;
    private boolean hasAdvert = false;
    private int mLength;

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
    private ArrayList<BannerEntity.ResultBean> bannerUrlList = new ArrayList<>();
    private List<String> rollNewsTitleList = new ArrayList<>();
    private List<RollNewsEntity.ResultBean> resultBeanList;

    private String mNoviceExclusiveId;

    @Override
    protected int getContentView() {
        return R.layout.fragment_home_page_loop;
    }

    @Override
    protected void initView(View view) {
        ViewUtils.inject(this, view);
        mContext = getActivity();
        mLayoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                initAdvert(bannerUrlList);
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("广告栏失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
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

    private void initAdvert(final ArrayList<BannerEntity.ResultBean> list) {
        int advertLength = list.size();
        mLength = advertLength;
        if(advertLength > 0){
            pageViews = new View[advertLength];
            for (int i = 0; i < advertLength; i++) {
                ImageView imageView = (ImageView)mLayoutInflater.inflate(R.layout.item_for_advert, null);
                pageViews[i] = imageView;
            }

            Resources res = getActivity().getResources();
            int indicateWH = res.getDimensionPixelSize(R.dimen.dimen_10px);
            int indicatePadding = res.getDimensionPixelSize(R.dimen.dimen_10px);
            //指示圆点
            indicViews = new View[advertLength];
            for (int i = 0; i < advertLength; i++) {
                View view = new View(getActivity());

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(indicateWH, indicateWH);
                lp.setMargins(indicatePadding, 0, indicatePadding, 0);

                view.setLayoutParams(lp);

                if (i == 0) {
                    // 默认选中第一张图片
                    view.setBackgroundResource(R.drawable.dot_24b279_15px);
                } else {
                    view.setBackgroundResource(R.drawable.dot_cc_16px);
                }
                indicViews[i] = view;
                mLlViewflowindic.addView(view);
            }

            currentIndex = 0;
            AdvertInfoViewPaperAdapterLoop adapter = new AdvertInfoViewPaperAdapterLoop(getActivity(), list, pageViews, advertLength);
//            viewFlow.setBoundaryCaching(true);
            viewFlow.setAdapter(new LoopPagerAdapterWrapper(adapter));
//            viewFlow.setOffscreenPageLimit(advertLength-1);
            viewFlow.addOnPageChangeListener(new GuidePageChangeListener());
            viewFlow.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_DOWN){
                        stopAutoScroller();
                    }else if(event.getAction()==MotionEvent.ACTION_UP){
                        startAutoScroller();
                    }
                    return false;
                }
            });
            viewFlow.setCurrentItem(currentIndex);

            mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    if(msg.what==1){
//                        viewFlow.setCurrentItem(currentIndex);
//                        currentIndex++;
//                        LogUtil.i("handleMessage:"+currentIndex);
                    }
                };
            };
            hasAdvert = true;
            startAutoScroller();
        }
        else{
            mLlAdvert.setVisibility(View.GONE);
        }
    }

    /**
     * 开始自动滚动
     */
    public boolean startAutoScroller(){
        return startTime();
    }
    /**
     * 停止自动滚动
     */
    public boolean stopAutoScroller(){
        if(mTimer!=null){
            mTimer.cancel();
            mTimer=null;
            return true;
        }else{
            return false;
        }
    }

    /**
     * 启动计时
     */
    private boolean startTime(){
        if(mTimer==null){
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg=new Message();
                    msg.what=1;
                    mHandler.sendMessage(msg);
                }
            },3000,3000); //每2秒执行一次
            return true;
        }else{
            return false;
        }
    }

    // 指引页面更改事件监听器
    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int arg0) {
            currentIndex = arg0;
//            LogUtil.i("onPageSelected:"+currentIndex);
//            LogUtil.i("onPageSelected real:"+LoopViewPager.toRealPosition(currentIndex, 6));
//            setImageBackground(LoopViewPager.toRealPosition(currentIndex, mLength));
        }

        /**
         * 设置选中的tip的背景
         *
         * @param selectItems
         */
        private void setImageBackground(int selectItems) {
            for (int i = 0; i < indicViews.length; i++) {
                if (selectItems == i) {
                    indicViews[selectItems].setBackgroundResource(R.drawable.dot_24b279_15px);
                } else {
                    indicViews[i].setBackgroundResource(R.drawable.dot_cc_16px);
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(hasAdvert){
            startAutoScroller();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(hasAdvert){
            stopAutoScroller();
        }
    }

    @OnClick({R.id.btn_bid, R.id.iv_fun_xsfl, R.id.iv_fun_yqyl})
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
        }
    }

}