package com.ql.jcjr.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.activity.LoginActivityCheck;
import com.ql.jcjr.activity.NoviceExclusiveActivity;
import com.ql.jcjr.adapter.ShortCutAdapter;
import com.ql.jcjr.base.BaseFragment;
import com.ql.jcjr.constant.AppConfig;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class HomePageFragment_old extends BaseFragment implements AdapterView.OnItemClickListener {

    @ViewInject(R.id.in_advert)
    private IndicatorView mIndicatorView;
    @ViewInject(R.id.marqueeView)
    private MarqueeView mMarqueeView;
//    @ViewInject(R.id.tv_notice)
//    private TextView mTvNotice;
//    @ViewInject(R.id.gv_shortcut)
//    private GridView mGridView;
    @ViewInject(R.id.tv_annualized_rate)
    private TextView mTvAnnualizedRate;
    @ViewInject(R.id.lv_selective_finance)
    private NoScrollListView mLvSelectiveFinance;
    @ViewInject(R.id.tv_lowest_amt)
    private TextView mTvLowestAmt;
    @ViewInject(R.id.tv_term)
    private TextView mTvTerm;

    private static final int INDEX_BEGINNER_WELFARE = 0;
    private static final int INDEX_MALL = 1;
    private static final int INDEX_INVITATION = 2;
    private static final int INDEX_ABOUT_US = 3;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ShortCutAdapter shortCutAdapter;
    private List<Integer> imgList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
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
        banner();
        initMarqueeView();
        initGridView();
//        initListVieww();
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
                mTvTerm.setText(getResources().getString(R.string.str_term, entity.getResult().getBorrowTime()));
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

    private void initGridView() {
        shortCutAdapter = new ShortCutAdapter(getActivity(), initImgList(), initTitleList(), 0);
//        mGridView.setAdapter(shortCutAdapter);
//        mGridView.setOnItemClickListener(this);
//        setGridViewHeightBasedOnChildren(mGridView);
    }


    private List<Integer> initImgList() {
        imgList.clear();
//        imgList.add(R.drawable.menu_icon_register);
//        imgList.add(R.drawable.menu_icon_mall);
//        imgList.add(R.drawable.menu_icon_invitation);
//        imgList.add(R.drawable.menu_icon_aboutus);
        return imgList;
    }

    private List<String> initTitleList() {
        titleList.clear();
        titleList.add("新手福利");
        titleList.add("安全保障");
        titleList.add("邀请有礼");
        titleList.add("关于我们");
        return titleList;
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView) {
        ListAdapter adapter = gridView.getAdapter();
        if (adapter == null) {
            return;
        }

        // GridView 总高度
        int totalHeight;

        // 计算GridView item 高度
        View item = adapter.getView(0, null, gridView);
        item.measure(0, 0);
        int itemHeight = item.getMeasuredHeight();

        // GridView 列数
        int column = getResources().getInteger(R.integer.home_page_grid_columms);

        // 计算GridView 行数
        int row;
        int tmpCount = gridView.getCount() / column;
        if (gridView.getCount() % gridView.getNumColumns() != 0) {
            row = tmpCount + 1;
        } else {
            row = tmpCount;
        }

        // 计算GridView总高度
        totalHeight = itemHeight * row;

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        // 加上分割线高度
        params.height = totalHeight + (tmpCount - 1);

        // 设置GridView布局参数
        gridView.setLayoutParams(params);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case INDEX_BEGINNER_WELFARE:
                UrlUtil.showHtmlPage(mContext,"新手福利", RequestURL.XSFL_URL);
                break;
            case INDEX_MALL:
                UrlUtil.showHtmlPage(mContext,"安全保障", RequestURL.AQBZ_URL);
                break;
            case INDEX_INVITATION:
                if(UserData.getInstance().isLogin()) {
                    UrlUtil.showHtmlPage(mContext,"邀请有礼", RequestURL.YQYL_URL + UserData.getInstance().getUSERID());
                }else {
                    Intent intent = new Intent(mContext, LoginActivityCheck.class);
                    startActivity(intent);
                }

                break;
            case INDEX_ABOUT_US:
                UrlUtil.showHtmlPage(mContext,"关于我们", AppConfig.ABOUT_US_URL);
                break;
        }
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

    @OnClick({R.id.btn_bid})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bid:
//                final MainActivity mainActivity = (MainActivity) getActivity();
//                mainActivity.mViewPager.setCurrentItem(INDEX_MANAGE_MONEY);
                Intent intent = new Intent(mContext, NoviceExclusiveActivity.class);
                intent.putExtra("bid_id",mNoviceExclusiveId);
                startActivity(intent);
                break;
        }
    }

}