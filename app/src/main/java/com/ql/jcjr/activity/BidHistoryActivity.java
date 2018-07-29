package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.BidHistoryAdapter;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.entity.BidHistoryEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.ActionBar;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.PullToRefreshView;
import com.ql.jcjr.view.XListView;

import java.util.ArrayList;
import java.util.List;

public class BidHistoryActivity extends BaseActivity  implements AdapterView.OnItemClickListener,
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterLoadListener{

    @ViewInject(R.id.ab_header)
    private ActionBar mActionBar;
    @ViewInject(R.id.pull_refresh_view)
    private PullToRefreshView mPullToRefreshView;
    @ViewInject(R.id.lv_history)
    private XListView mLvHistory;
    @ViewInject(R.id.ll_1)
    private LinearLayout ll_1;
    @ViewInject(R.id.tv_order_paytime_1)
    private TextView tv_order_paytime_1;
    @ViewInject(R.id.tv_order_datatime_1)
    private TextView tv_order_datatime_1;
    @ViewInject(R.id.iv_00)
    private ImageView iv_00;
    @ViewInject(R.id.iv_01)
    private ImageView iv_01;
    @ViewInject(R.id.iv_02)
    private ImageView iv_02;
    @ViewInject(R.id.iv_03)
    private ImageView iv_03;

    @ViewInject(R.id.rl_1)
    private RelativeLayout rl_1;
    @ViewInject(R.id.rl_2)
    private RelativeLayout rl_2;

    @ViewInject(R.id.ll_tip_none)
    private RelativeLayout mLlTipNone;
    @ViewInject(R.id.tv_none_btn)
    private Button mTvNoneBtn;

    private Context mContext;

    private BidHistoryAdapter mAdapter;
    private List<BidHistoryEntity.ResultBean.ListBean> mList = new ArrayList<>();

    // 分页加载索引
    private int mPageIndex = 1;

    private boolean isHistory = false;
    private View headerView;
    private TextView mTvBidHistoryTotalMoney;
    private TextView mTvBidHistoryTotalGet;
    private TextView mTvBidHistoryTotalGetPre;
    private TextView mTvBidHistoryTotalMoneyPre;
    private LinearLayout mlLBidHistoryTopbg;
    private LinearLayout mlLBidHistoryContent;
    private RelativeLayout mlLBidHistory;
    private View mViewDivider;
    private LinearLayout mlLBidHistoryBottom;
    private ImageView iv_0;
    private ImageView iv_1;
    private ImageView iv_2;
    private ImageView iv_3;
    private RelativeLayout mRlPay;
    private RelativeLayout mRlGet;
    private TextView tv_order_paytime;
    private TextView tv_order_datatime;
    private Boolean paytime=false;//正序是true 倒序是false
    private Boolean gettime=true;//正序是true 倒序是false
    private String order="0";
    private Boolean isTouch=false;//判断是否是排序跳转
    private String one="0";
    private String two="2";
    private Boolean isOne=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bd_history);
        ViewUtils.inject(this);
        mContext = this;

        init();
    }
    private SparseArray recordSp = new SparseArray(0);
    private int mCurrentfirstVisibleItem = 0;

    private void init() {
        isHistory = getIntent().getBooleanExtra("tag_history", false);
        headerView = LayoutInflater.from(mContext).inflate(R.layout.listview_top,null);
        initId();
        mLvHistory.addHeaderView(headerView);

        initScorllListener();


        mTvNoneBtn.setText("立即投资");
        mTvNoneBtn.setVisibility(View.VISIBLE);

        if(!isHistory){
            mActionBar.setRightText("历史投资");
        }
        else{
            mActionBar.setTitle("历史投资");
            mlLBidHistoryTopbg.setBackgroundResource(R.drawable.bg_card_lstz);

            mTvBidHistoryTotalMoney.setTextColor(getResources().getColor(R.color.btn_main));
            mTvBidHistoryTotalGetPre.setText("已收利息(元)");
            mTvBidHistoryTotalGetPre.setTextColor(getResources().getColor(R.color.font_black));

            mTvBidHistoryTotalGet.setTextColor(getResources().getColor(R.color.btn_main));
            mTvBidHistoryTotalMoneyPre.setText("已收本金(元)");
            mTvBidHistoryTotalMoneyPre.setTextColor(getResources().getColor(R.color.font_black));

            mViewDivider.setBackgroundResource(R.drawable.list_divider_v_grey);

            mlLBidHistory.setBackgroundColor(getResources().getColor(R.color.c_page_bg));
        }
        initListView();

        getContent(String.valueOf(mPageIndex));

        mPullToRefreshView.setOnHeaderRefreshListener(this);
        mPullToRefreshView.setOnFooterLoadListener(this);
    }

    private void initScorllListener() {
        if (isHistory){
            mlLBidHistoryBottom.setVisibility(View.GONE);
            return;
        }
        mLvHistory.setOnScrollListener(new AbsListView.OnScrollListener() {


            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                mCurrentfirstVisibleItem = firstVisibleItem;
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
                    if (h>mlLBidHistory.getHeight()||firstVisibleItem!=0){
                        ll_1.setVisibility(View.VISIBLE);
                    }else {
                        ll_1.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    class ItemRecod {
        int height = 0;
        int top = 0;
    }
    public int getScrollY() {
        View c = mLvHistory.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = mLvHistory.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight() ;
    }
    private void initId() {
        mTvBidHistoryTotalMoney = (TextView) headerView.findViewById(R.id.tv_bid_history_total_money);
        mTvBidHistoryTotalGet = (TextView) headerView.findViewById(R.id.tv_bid_history_total_get);
        mTvBidHistoryTotalGetPre = (TextView) headerView.findViewById(R.id.tv_bid_history_total_get_pre);
        mTvBidHistoryTotalMoneyPre = (TextView) headerView.findViewById(R.id.tv_bid_history_total_money_pre);
        mlLBidHistoryTopbg = (LinearLayout) headerView.findViewById(R.id.ll_bid_history_topbg);
        mlLBidHistoryContent = (LinearLayout) headerView.findViewById(R.id.ll_bid_history_content);
        mlLBidHistory = (RelativeLayout) headerView.findViewById(R.id.ll_bid_history);
        mViewDivider = headerView.findViewById(R.id.view_divider);
        mlLBidHistoryBottom = (LinearLayout) headerView.findViewById(R.id.ll_bid_history_bottom);
        iv_0 = (ImageView)headerView.findViewById(R.id.iv_0);
        iv_1 = (ImageView)headerView.findViewById(R.id.iv_1);
        iv_2 = (ImageView)headerView.findViewById(R.id.iv_2);
        iv_3 = (ImageView)headerView.findViewById(R.id.iv_3);
        mRlPay = (RelativeLayout) headerView.findViewById(R.id.rl_pay_time);
        mRlGet = (RelativeLayout) headerView.findViewById(R.id.rl_get_time);
        tv_order_paytime = (TextView) headerView.findViewById(R.id.tv_order_paytime);
        tv_order_datatime = (TextView) headerView.findViewById(R.id.tv_order_datatime);
        mRlPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTouch=false;
                mPageIndex = 1;
                //设置允许加载更多
                mPullToRefreshView.setLoadMoreEnable(true);
                //移除footer
                mLvHistory.removeFooterView();
                if (isOne) {
                    if (paytime) {
                        order = "0";
                    } else {
                        order = "1";
                    }
                    paytime = !paytime;
                    one=order;
                }else {
                    order=one;
                    isOne=true;
                }
                getContent(String.valueOf(mPageIndex));
            }
        });
        mRlGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTouch=false;
                mPageIndex = 1;
                //设置允许加载更多
                mPullToRefreshView.setLoadMoreEnable(true);
                //移除footer
                mLvHistory.removeFooterView();
                if (isOne){
                    isOne=false;
                    order=two;
                }else {
                    if (gettime){
                        order="3";
                    }else {
                        order="2";
                    }
                    gettime=!gettime;
                    two=order;
                }
                getContent(String.valueOf(mPageIndex));
            }
        });

    }

    private void initListView() {
        mAdapter = new BidHistoryAdapter(mContext, mList);
        mLvHistory.setAdapter(mAdapter);
        mLvHistory.setOnItemClickListener(this);
    }

    private void getContent(final String pageIndex) {
        String status = null;

        if(isHistory){
            status = "1";
            order=null;
        }

        SenderResultModel resultModel = ParamsManager.senderMyBidHistory(pageIndex, "10", status,order);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("历史投资 " + responeJson);
                BidHistoryEntity entity = GsonParser.getParsedObj(responeJson, BidHistoryEntity.class);
                initUI();
                boolean needShowAll = false;
                int listSize = entity.getResult().getList().size();
                if(pageIndex.equals("1")) {
                    if (isHistory) {
                        mTvBidHistoryTotalMoney.setText(entity.getResult().getAccount());
                        mTvBidHistoryTotalGet.setText(entity.getResult().getWait_interest());
                    }else {
                        mTvBidHistoryTotalMoney.setText(entity.getResult().getAll_account()+"元");
                        mTvBidHistoryTotalGet.setText(entity.getResult().getLate_account()+"元");
                    }

                    mList.clear();
                    if(listSize < 10 && listSize >0){
                        needShowAll = true;
                    }
                }
                else{
                    if(listSize < 10){
                        needShowAll = true;
                    }
                }

                if(needShowAll){
                    mLvHistory.addFooterView();
                    //设置不在加载更多
                    mPullToRefreshView.setLoadMoreEnable(false);
                }

                mList.addAll(entity.getResult().getList());
                mAdapter.notifyDataSetChanged();

                if (isTouch){
                    mLvHistory.smoothScrollToPosition(0);
                    mAdapter.notifyDataSetChanged();
                }
                finishRefresh();
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("历史红包失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
                finishRefresh();
            }

        }, mContext);
    }
    //order 0：投资时间倒序 1：投资时间正序 2：回款时间正序 3：汇款时间倒序
    private void initUI() {
        if (order!=null){
            if (order.equals("0")){
                tv_order_paytime.setTextColor(getResources().getColor(R.color.btn_main));
                tv_order_paytime_1.setTextColor(getResources().getColor(R.color.btn_main));
                tv_order_datatime.setTextColor(getResources().getColor(R.color.title_bg_color));
                tv_order_datatime_1.setTextColor(getResources().getColor(R.color.title_bg_color));
                iv_0.setImageResource(R.drawable.arrow_yellow_down_wdtz);
                iv_1.setImageResource(R.drawable.arrow_gray_wdtz);
                iv_2.setImageResource(R.drawable.arrow_gray_wdtz);
                iv_3.setImageResource(R.drawable.arrow_down_wdtz);
                iv_00.setImageResource(R.drawable.arrow_yellow_down_wdtz);
                iv_01.setImageResource(R.drawable.arrow_gray_wdtz);
                iv_02.setImageResource(R.drawable.arrow_gray_wdtz);
                iv_03.setImageResource(R.drawable.arrow_down_wdtz);
            }else if (order.equals("1")){
                tv_order_paytime.setTextColor(getResources().getColor(R.color.btn_main));
                tv_order_paytime_1.setTextColor(getResources().getColor(R.color.btn_main));
                tv_order_datatime.setTextColor(getResources().getColor(R.color.title_bg_color));
                tv_order_datatime_1.setTextColor(getResources().getColor(R.color.title_bg_color));
                iv_0.setImageResource(R.drawable.arrow_down_wdtz);
                iv_1.setImageResource(R.drawable.arrow_wdtz);
                iv_2.setImageResource(R.drawable.arrow_gray_wdtz);
                iv_3.setImageResource(R.drawable.arrow_down_wdtz);
                iv_00.setImageResource(R.drawable.arrow_down_wdtz);
                iv_01.setImageResource(R.drawable.arrow_wdtz);
                iv_02.setImageResource(R.drawable.arrow_gray_wdtz);
                iv_03.setImageResource(R.drawable.arrow_down_wdtz);
            }else if (order.equals("2")){
                tv_order_paytime.setTextColor(getResources().getColor(R.color.title_bg_color));
                tv_order_paytime_1.setTextColor(getResources().getColor(R.color.title_bg_color));
                tv_order_datatime.setTextColor(getResources().getColor(R.color.btn_main));
                tv_order_datatime_1.setTextColor(getResources().getColor(R.color.btn_main));
                iv_0.setImageResource(R.drawable.arrow_down_wdtz);
                iv_1.setImageResource(R.drawable.arrow_gray_wdtz);
                iv_2.setImageResource(R.drawable.arrow_wdtz);
                iv_3.setImageResource(R.drawable.arrow_down_wdtz);
                iv_00.setImageResource(R.drawable.arrow_down_wdtz);
                iv_01.setImageResource(R.drawable.arrow_gray_wdtz);
                iv_02.setImageResource(R.drawable.arrow_wdtz);
                iv_03.setImageResource(R.drawable.arrow_down_wdtz);
            }else if (order.equals("3")){
                tv_order_paytime.setTextColor(getResources().getColor(R.color.title_bg_color));
                tv_order_paytime_1.setTextColor(getResources().getColor(R.color.title_bg_color));
                tv_order_datatime.setTextColor(getResources().getColor(R.color.btn_main));
                tv_order_datatime_1.setTextColor(getResources().getColor(R.color.btn_main));
                iv_0.setImageResource(R.drawable.arrow_down_wdtz);
                iv_1.setImageResource(R.drawable.arrow_gray_wdtz);
                iv_2.setImageResource(R.drawable.arrow_gray_wdtz);
                iv_3.setImageResource(R.drawable.arrow_yellow_down_wdtz);
                iv_00.setImageResource(R.drawable.arrow_down_wdtz);
                iv_01.setImageResource(R.drawable.arrow_gray_wdtz);
                iv_02.setImageResource(R.drawable.arrow_gray_wdtz);
                iv_03.setImageResource(R.drawable.arrow_yellow_down_wdtz);
            }
        }
    }

    private void finishRefresh() {
        mPullToRefreshView.onHeaderRefreshFinish();
        mPullToRefreshView.onFooterLoadFinish();
        if(mList.size() == 0){
            mlLBidHistoryContent.setVisibility(View.GONE);
            mLlTipNone.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position-1==mList.size()||position==0){return;}
        BidHistoryEntity.ResultBean.ListBean bean = mList.get(position-1);
        Intent intent = new Intent(BidHistoryActivity.this, BidHistoryDetailActivity.class);
        intent.putExtra("bid_id",bean.getBorrow_id());
        intent.putExtra("bid_name",bean.getName());
        intent.putExtra("bid_tender",bean.getTender_id());
        intent.putExtra("bid_status",bean.getStatus());
        intent.putExtra("bid_status_name",bean.getStatusname());
        if (bean.getOvertime().equals("1")){
            intent.putExtra("is_yq",true);
        }

        startActivity(intent);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        isTouch=false;
        mPageIndex = 1;
        //设置允许加载更多
        mPullToRefreshView.setLoadMoreEnable(true);
        //移除footer
        mLvHistory.removeFooterView();
        getContent(String.valueOf(mPageIndex));
    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        isTouch=false;
        mPageIndex++;
        getContent(String.valueOf(mPageIndex));
    }

    @OnClick({R.id.btn_left, R.id.btn_right, R.id.tv_none_btn,R.id.rl_1,R.id.rl_2})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;

            case R.id.btn_right:
                intent = new Intent(BidHistoryActivity.this, BidHistoryActivity.class);
                intent.putExtra("tag_history",true);
                startActivity(intent);
                break;

            case R.id.tv_none_btn:
                intent = new Intent(BidHistoryActivity.this, MainActivity.class);
                intent.putExtra("main_index",1);
                BidHistoryActivity.this.startActivity(intent);
                finish();
                break;

            case R.id.rl_1:
                isTouch=true;
                mPageIndex = 1;
                //设置允许加载更多
                mPullToRefreshView.setLoadMoreEnable(true);
                //移除footer
                mLvHistory.removeFooterView();

                if (paytime){
                    order="0";
                }else {
                    order="1";
                }
                paytime=!paytime;
                getContent(String.valueOf(mPageIndex));
                break;

            case R.id.rl_2:
                isTouch=true;
                mPageIndex = 1;
                //设置允许加载更多
                mPullToRefreshView.setLoadMoreEnable(true);
                //移除footer
                mLvHistory.removeFooterView();

                if (gettime){
                    order="3";
                }else {
                    order="2";
                }
                gettime=!gettime;
                getContent(String.valueOf(mPageIndex));
                break;
        }
    }
}
