package com.ql.jcjr.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.activity.BidDetailActivity;
import com.ql.jcjr.activity.NoviceExclusiveActivity;
import com.ql.jcjr.adapter.YyyAdapter;
import com.ql.jcjr.base.BaseFragment;
import com.ql.jcjr.entity.BidListEntity;
import com.ql.jcjr.entity.HomeDataEntity;
import com.ql.jcjr.entity.XSBEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.holder.BidShowView;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.PullToRefreshView;
import com.ql.jcjr.view.XListView;

import java.util.ArrayList;
import java.util.List;

import static com.ql.jcjr.utils.StatusBarCompat.getStatusBarHeight;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class ManageMoneyFragment extends BaseFragment implements AdapterView.OnItemClickListener,
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterLoadListener, View.OnClickListener {

//    @ViewInject(R.id.cvw_pager)
//    private NoScrollViewPager mViewPager;
//    @ViewInject(R.id.rg__manage_money)
//    private RadioGroup mRadioGroup;
    @ViewInject(R.id.lv_yyy)
    private XListView mLvYyy;
    @ViewInject(R.id.pull_refresh_view)
    private PullToRefreshView mPullToRefreshView;
    @ViewInject(R.id.tv_moren1)
    private TextView tv_moren1;
    @ViewInject(R.id.tv_apr1)
    private TextView tv_apr1;
    @ViewInject(R.id.iv_apr_up1)
    private ImageView iv_apr_up1;
    @ViewInject(R.id.iv_apr_down1)
    private ImageView iv_apr_down1;
    @ViewInject(R.id.tv_time1)
    private TextView tv_time1;
    @ViewInject(R.id.iv_time_down1)
    private ImageView iv_time_down1;
    @ViewInject(R.id.iv_time_up1)
    private ImageView iv_time_up1;
    @ViewInject(R.id.tv_jindu1)
    private TextView tv_jindu1;
    @ViewInject(R.id.iv_jindu_down1)
    private ImageView iv_jindu_down1;
    @ViewInject(R.id.iv_jindu_up1)
    private ImageView iv_jindu_up1;
    @ViewInject(R.id.ll_1)
    private LinearLayout ll_1;

    @ViewInject(R.id.v_status)
    private View v_status;


    private final static int IDEX_YYY = 0;//月月盈
    private final static int INDEX_TTL = 1;//天天利
    private final static int INDEX_XSZX = 2;//新手专享

    private Context mContext;
    private YyyAdapter mAdapter;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    private List<BidListEntity.ResultBean> mBidList = new ArrayList<>();
    private List<HomeDataEntity.ResultBean.ResultBeanTwo.BidBean> mBidxsb = new ArrayList<>();

    private String order="";
    private boolean a=true;
    private boolean b=true;
    private boolean c=true;
    private Boolean isTouch=false;//判断是否是排序跳转
    // 分页加载索引
    private int mPageIndex = 1;
    private View headerView;
    private TextView tv_moren;
    private TextView tv_apr;
    private ImageView iv_apr_up;
    private ImageView iv_apr_down;
    private TextView tv_time;
    private ImageView iv_time_down;
    private ImageView iv_time_up;
    private TextView tv_jindu;
    private ImageView iv_jindu_down;
    private ImageView iv_jindu_up;
    private ConvenientBanner cb_bidshow_two;


    @Override
    protected int getContentView() {
        return R.layout.fragment_manage_money;
    }

    @Override
    protected void initView(View view) {
        ViewUtils.inject(this, view);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v_status.getLayoutParams();
        layoutParams.height = getStatusBarHeight(getActivity());
        v_status.setLayoutParams(layoutParams);

        mContext = getActivity();
        mLayoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterLoadListener(this);
        headerView = LayoutInflater.from(mContext).inflate(R.layout.head_first,null);
        mLvYyy.addHeaderView(headerView);
        initId();
        initScorllListener();
    }

    private SparseArray recordSp = new SparseArray(0);
    private void initScorllListener() {
        mLvYyy.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View firstView = view.getChildAt(0);
                if (null != firstView) {
                    ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                    if (null == itemRecord) {
                        itemRecord = new ItemRecod();
                    }
                    itemRecord.height = firstView.getHeight();
                    itemRecord.top = firstView.getTop();
                    recordSp.append(firstVisibleItem, itemRecord);
                    int h = getScrollY();//滚动距离
                    if (h>cb_bidshow_two.getHeight()||firstVisibleItem!=0){
                        ll_1.setVisibility(View.VISIBLE);
                    }else {
                        ll_1.setVisibility(View.GONE);
                    }
                }

            }
        });
    }
    public int getScrollY() {
        View c = mLvYyy.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = mLvYyy.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight() ;
    }
    class ItemRecod {
        int height = 0;
        int top = 0;
    }
    private void initId() {
        tv_moren = (TextView) headerView.findViewById(R.id.tv_moren);
        tv_apr = (TextView) headerView.findViewById(R.id.tv_apr);
        iv_apr_up = (ImageView) headerView.findViewById(R.id.iv_apr_up);
        iv_apr_down = (ImageView) headerView.findViewById(R.id.iv_apr_down);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);
        iv_time_down = (ImageView) headerView.findViewById(R.id.iv_time_down);
        iv_time_up = (ImageView) headerView.findViewById(R.id.iv_time_up);
        tv_jindu = (TextView) headerView.findViewById(R.id.tv_jindu);
        iv_jindu_down = (ImageView) headerView.findViewById(R.id.iv_jindu_down);
        iv_jindu_up = (ImageView) headerView.findViewById(R.id.iv_jindu_up);
        cb_bidshow_two = (ConvenientBanner) headerView.findViewById(R.id.cb_bidshow_two);
        LinearLayout rl_apr= (LinearLayout) headerView.findViewById(R.id.rl_apr);
        LinearLayout ll_time= (LinearLayout) headerView.findViewById(R.id.ll_time);
        LinearLayout ll_jindu= (LinearLayout) headerView.findViewById(R.id.ll_jindu);

        tv_moren.setOnClickListener(this);
        rl_apr.setOnClickListener(this);
        ll_time.setOnClickListener(this);
        ll_jindu.setOnClickListener(this);
    }

    @Override
    protected void onFragmentFirstVisible() {
        init();
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if(isVisible) {
            getYyyList(String.valueOf(mPageIndex));
        }
    }

    //    public void setCurrentItem(int index){
//        switch (index){
//            case 0:
//                mRadioGroup.check(R.id.tab_yyy);
//                break;
//            case 1:
//                mRadioGroup.check(R.id.tab_ttl);
//                break;
//            case 2:
//                mRadioGroup.check(R.id.tab_xszx);
//                break;
//        }
//        mViewPager.setCurrentItem(index);
//    }

    private void init() {
//        initViewPager();
//        initBottomTab();

        initListView();
    }

    private void initListView() {
        mAdapter = new YyyAdapter(mContext, mBidList);

        mLvYyy.setAdapter(mAdapter);
        mLvYyy.setOnItemClickListener(this);
    }

    private void getYyyList(final String pageIndex) {
        getXSB();

        SenderResultModel resultModel = ParamsManager.senderBidListNormal(pageIndex, "10", "",order);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        initUI();
                        LogUtil.i("理财列表normal " + responeJson);
                        BidListEntity entity = GsonParser.getParsedObj(responeJson, BidListEntity.class);
                        if(pageIndex.equals("1")) {
                            mBidList.clear();
                        }
                        boolean needShowAll = false;
                        if (entity.getResult()==null||entity.getResult().size()==0){
                            needShowAll = true;
                        }
                        if(needShowAll){
                            mLvYyy.addFooterView();
                            //设置不在加载更多
                            mPullToRefreshView.setLoadMoreEnable(false);
                        }else {
                            mLvYyy.removeFooterView();
                            //设置可以加载更多
                            mPullToRefreshView.setLoadMoreEnable(true);
                        }
                        mBidList.addAll(entity.getResult());
                        mAdapter.notifyDataSetChanged();
                        if (isTouch){
                            mLvYyy.smoothScrollToPosition(0);
                            mAdapter.notifyDataSetChanged();
                        }
                        finishRefresh();
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("理财列表normal " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                        finishRefresh();
                    }

                }, mContext);
    }

    private void getXSB() {
        SenderResultModel resultModel = ParamsManager.senderBidListXSB();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("理财列表xsb " + responeJson);
                        XSBEntity entity = GsonParser.getParsedObj(responeJson, XSBEntity.class);
                        mBidxsb.clear();
                        mBidxsb.addAll(entity.getResult());

                        if (mBidxsb == null || mBidxsb.size() == 0) {
                            cb_bidshow_two.setVisibility(View.GONE);
                            return;
                        }else {
                            cb_bidshow_two.setVisibility(View.VISIBLE);
                        }

                        cb_bidshow_two.setPages(
                                new CBViewHolderCreator<BidShowView>() {
                                    @Override
                                    public BidShowView createHolder() {
                                        return new BidShowView();
                                    }
                                }, mBidxsb)
                                //设置点击监听事件
                                .setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int i) {
                                        Intent intent = new Intent(mContext, NoviceExclusiveActivity.class);
                                        intent.putExtra("bid_id", mBidxsb.get(i).getId());
                                        intent.putExtra("bid_title", mBidxsb.get(i).getName());
                                        startActivity(intent);
                                    }
                                })
                                .setPageIndicator(new int[]{R.drawable.block_disable, R.drawable.block_selected});

                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("理财列表xsb " + entity.errorInfo);
                    }

                }, mContext);

    }

    private void initUI() {
        if (order.equals("apr_down")){
            b=true;
            c=true;
            tv_moren.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr.setTextColor(getResources().getColor(R.color.btn_main));
            iv_apr_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down.setImageResource(R.drawable.arrow_yellow_down_wdtz);
            tv_time.setTextColor(getResources().getColor(R.color.font_black));
            iv_time_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu.setTextColor(getResources().getColor(R.color.font_black));
            iv_jindu_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down.setImageResource(R.drawable.arrow_down_wdtz);

            tv_moren1.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr1.setTextColor(getResources().getColor(R.color.btn_main));
            iv_apr_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down1.setImageResource(R.drawable.arrow_yellow_down_wdtz);
            tv_time1.setTextColor(getResources().getColor(R.color.font_black));
            iv_time_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down1.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu1.setTextColor(getResources().getColor(R.color.font_black));
            iv_jindu_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down1.setImageResource(R.drawable.arrow_down_wdtz);

        }else if (order.equals("apr_up")){
            b=true;
            c=true;
            tv_moren.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr.setTextColor(getResources().getColor(R.color.btn_main));
            iv_apr_up.setImageResource(R.drawable.arrow_wdtz);
            iv_apr_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time.setTextColor(getResources().getColor(R.color.font_black));
            iv_time_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu.setTextColor(getResources().getColor(R.color.font_black));
            iv_jindu_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down.setImageResource(R.drawable.arrow_down_wdtz);

            tv_moren1.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr1.setTextColor(getResources().getColor(R.color.btn_main));
            iv_apr_up1.setImageResource(R.drawable.arrow_wdtz);
            iv_apr_down1.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time1.setTextColor(getResources().getColor(R.color.font_black));
            iv_time_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down1.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu1.setTextColor(getResources().getColor(R.color.font_black));
            iv_jindu_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down1.setImageResource(R.drawable.arrow_down_wdtz);

        }else if (order.equals("time_down")){
            a=true;
            c=true;
            tv_moren.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr.setTextColor(getResources().getColor(R.color.font_black));
            iv_apr_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time.setTextColor(getResources().getColor(R.color.btn_main));
            iv_time_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down.setImageResource(R.drawable.arrow_yellow_down_wdtz);
            tv_jindu.setTextColor(getResources().getColor(R.color.font_black));
            iv_jindu_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down.setImageResource(R.drawable.arrow_down_wdtz);

            tv_moren1.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr1.setTextColor(getResources().getColor(R.color.font_black));
            iv_apr_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down1.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time1.setTextColor(getResources().getColor(R.color.btn_main));
            iv_time_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down1.setImageResource(R.drawable.arrow_yellow_down_wdtz);
            tv_jindu1.setTextColor(getResources().getColor(R.color.font_black));
            iv_jindu_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down1.setImageResource(R.drawable.arrow_down_wdtz);
        }else if (order.equals("time_up")){
            a=true;
            c=true;
            tv_moren.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr.setTextColor(getResources().getColor(R.color.font_black));
            iv_apr_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time.setTextColor(getResources().getColor(R.color.btn_main));
            iv_time_up.setImageResource(R.drawable.arrow_wdtz);
            iv_time_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu.setTextColor(getResources().getColor(R.color.font_black));
            iv_jindu_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down.setImageResource(R.drawable.arrow_down_wdtz);

            tv_moren1.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr1.setTextColor(getResources().getColor(R.color.font_black));
            iv_apr_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down1.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time1.setTextColor(getResources().getColor(R.color.btn_main));
            iv_time_up1.setImageResource(R.drawable.arrow_wdtz);
            iv_time_down1.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu1.setTextColor(getResources().getColor(R.color.font_black));
            iv_jindu_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down1.setImageResource(R.drawable.arrow_down_wdtz);

        }else if (order.equals("jindu_down")){
            a=true;
            b=true;
            tv_moren.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr.setTextColor(getResources().getColor(R.color.font_black));
            iv_apr_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time.setTextColor(getResources().getColor(R.color.font_black));
            iv_time_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu.setTextColor(getResources().getColor(R.color.btn_main));
            iv_jindu_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down.setImageResource(R.drawable.arrow_yellow_down_wdtz);

            tv_moren1.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr1.setTextColor(getResources().getColor(R.color.font_black));
            iv_apr_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down1.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time1.setTextColor(getResources().getColor(R.color.font_black));
            iv_time_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down1.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu1.setTextColor(getResources().getColor(R.color.btn_main));
            iv_jindu_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down1.setImageResource(R.drawable.arrow_yellow_down_wdtz);

        }else if (order.equals("jindu_up")){
            a=true;
            b=true;
            tv_moren.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr.setTextColor(getResources().getColor(R.color.font_black));
            iv_apr_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time.setTextColor(getResources().getColor(R.color.font_black));
            iv_time_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu.setTextColor(getResources().getColor(R.color.btn_main));
            iv_jindu_up.setImageResource(R.drawable.arrow_wdtz);
            iv_jindu_down.setImageResource(R.drawable.arrow_down_wdtz);

            tv_moren1.setTextColor(getResources().getColor(R.color.font_black));
            tv_apr1.setTextColor(getResources().getColor(R.color.font_black));
            iv_apr_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down1.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time1.setTextColor(getResources().getColor(R.color.font_black));
            iv_time_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down1.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu1.setTextColor(getResources().getColor(R.color.btn_main));
            iv_jindu_up1.setImageResource(R.drawable.arrow_wdtz);
            iv_jindu_down1.setImageResource(R.drawable.arrow_down_wdtz);

        }else {  //默认
            tv_moren.setTextColor(getResources().getColor(R.color.btn_main));
            tv_apr.setTextColor(getResources().getColor(R.color.font_black));
            iv_apr_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time.setTextColor(getResources().getColor(R.color.font_black));
            iv_time_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu.setTextColor(getResources().getColor(R.color.font_black));
            iv_jindu_up.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down.setImageResource(R.drawable.arrow_down_wdtz);
            b=true;
            c=true;
            a=true;
            tv_moren1.setTextColor(getResources().getColor(R.color.btn_main));
            tv_apr1.setTextColor(getResources().getColor(R.color.font_black));
            iv_apr_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_apr_down1.setImageResource(R.drawable.arrow_down_wdtz);
            tv_time1.setTextColor(getResources().getColor(R.color.font_black));
            iv_time_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_time_down1.setImageResource(R.drawable.arrow_down_wdtz);
            tv_jindu1.setTextColor(getResources().getColor(R.color.font_black));
            iv_jindu_up1.setImageResource(R.drawable.arrow_gray_wdtz);
            iv_jindu_down1.setImageResource(R.drawable.arrow_down_wdtz);
        }
    }

    private void finishRefresh() {
        mPullToRefreshView.onHeaderRefreshFinish();
        mPullToRefreshView.onFooterLoadFinish();
    }

//    private void initViewPager() {
//        mFragmentList.clear();
//
//        mFragmentList.add(new YYYFragment());
//
////        mFragmentList.add(new TTLFragment());
////
////        mFragmentList.add(new XSZXFragment());
//
//        mViewPager.setAdapter(new ViewPagerAdapter(getFragmentManager(), mFragmentList));
//        mViewPager.setOffscreenPageLimit(mFragmentList.size() - 1);
//        mViewPager.setScanScroll(false);
//        mViewPager.setCurrentItem(0);
//    }

//    private void initBottomTab() {
//        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.tab_yyy:
//                        mViewPager.setCurrentItem(IDEX_YYY);
//                        break;
//
//                    case R.id.tab_ttl:
//                        mViewPager.setCurrentItem(INDEX_TTL);
//                        break;
//
//                    case R.id.tab_xszx:
//                        mViewPager.setCurrentItem(INDEX_XSZX);
//                        break;
//                }
//            }
//        });
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if(mBidList.get(position).getPwd().equals("1")){
//            gotoBidDetail(position);
//        }
//        else{
            gotoBidDetail(position);
//        }
    }

    private void gotoBidDetail(int position){
        position--;
        if (position==mBidList.size())return;
        Intent intent = new Intent(mContext, BidDetailActivity.class);
        intent.putExtra("bid_title", mBidList.get(position).getName());
        intent.putExtra("bid_id", mBidList.get(position).getId());
        mContext.startActivity(intent);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        isTouch=false;
        mPageIndex = 1;

        //设置允许加载更多
        mPullToRefreshView.setLoadMoreEnable(true);
        //移除footer
        mLvYyy.removeFooterView();

        getYyyList(String.valueOf(mPageIndex));
    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        isTouch=false;
        mPageIndex++;
        getYyyList(String.valueOf(mPageIndex));
    }

    @OnClick({R.id.tv_moren,R.id.rl_apr,R.id.ll_time,R.id.ll_jindu,R.id.tv_moren1,R.id.rl_apr1,R.id.ll_time1,R.id.ll_jindu1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_moren:
                isTouch=true;
                order="";
                mPageIndex = 1;
                getYyyList(String.valueOf(mPageIndex));
                break;
            case R.id.rl_apr:
                isTouch=true;
                if (a){
                    order="apr_down";
                }else {
                    order="apr_up";
                }
                a=!a;
                mPageIndex = 1;
                getYyyList(String.valueOf(mPageIndex));
                break;

            case R.id.ll_time:
                isTouch=true;
                if (b){
                    order="time_down";
                }else {
                    order="time_up";
                }
                b=!b;
                mPageIndex = 1;
                getYyyList(String.valueOf(mPageIndex));
                break;
            case R.id.ll_jindu:
                isTouch=true;
                if (c){
                    order="jindu_down";
                }else {
                    order="jindu_up";
                }
                c=!c;
                mPageIndex = 1;
                getYyyList(String.valueOf(mPageIndex));
                break;
            case R.id.tv_moren1:
                isTouch=true;
                order="";
                mPageIndex = 1;
                getYyyList(String.valueOf(mPageIndex));
                break;
            case R.id.rl_apr1:
                isTouch=true;
                if (a){
                    order="apr_down";
                }else {
                    order="apr_up";
                }
                a=!a;
                mPageIndex = 1;
                getYyyList(String.valueOf(mPageIndex));
                break;

            case R.id.ll_time1:
                isTouch=true;
                if (b){
                    order="time_down";
                }else {
                    order="time_up";
                }
                b=!b;
                mPageIndex = 1;
                getYyyList(String.valueOf(mPageIndex));
                break;
            case R.id.ll_jindu1:
                isTouch=true;
                if (c){
                    order="jindu_down";
                }else {
                    order="jindu_up";
                }
                c=!c;
                mPageIndex = 1;
                getYyyList(String.valueOf(mPageIndex));
                break;
        }
    }


}