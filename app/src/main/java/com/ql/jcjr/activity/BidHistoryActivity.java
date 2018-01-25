package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

    @ViewInject(R.id.ll_bid_history_content)
    private LinearLayout mlLBidHistoryContent;
    @ViewInject(R.id.ll_bid_history)
    private RelativeLayout mlLBidHistory;
    @ViewInject(R.id.ll_bid_history_topbg)
    private LinearLayout mlLBidHistoryTopbg;
    @ViewInject(R.id.tv_bid_history_total_money)
    private TextView mTvBidHistoryTotalMoney;
    @ViewInject(R.id.tv_bid_history_total_get)
    private TextView mTvBidHistoryTotalGet;

    @ViewInject(R.id.tv_bid_history_total_get_pre)
    private TextView mTvBidHistoryTotalGetPre;
    @ViewInject(R.id.tv_bid_history_total_money_pre)
    private TextView mTvBidHistoryTotalMoneyPre;

    @ViewInject(R.id.view_divider)
    private View mViewDivider;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bd_history);
        ViewUtils.inject(this);
        mContext = this;

        init();
    }

    private void init() {
        isHistory = getIntent().getBooleanExtra("tag_history", false);
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

    private void initListView() {
        mAdapter = new BidHistoryAdapter(mContext, mList);
        mLvHistory.setAdapter(mAdapter);
        mLvHistory.setOnItemClickListener(this);
    }

    private void getContent(final String pageIndex) {
        String status = null;
        if(isHistory){
            status = "1";
        }
        SenderResultModel resultModel = ParamsManager.senderMyBidHistory(pageIndex, "10", status);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("历史投资 " + responeJson);
                BidHistoryEntity entity = GsonParser.getParsedObj(responeJson, BidHistoryEntity.class);

                boolean needShowAll = false;
                int listSize = entity.getResult().getList().size();
                if(pageIndex.equals("1")) {
                    mTvBidHistoryTotalMoney.setText(entity.getResult().getAccount());
                    mTvBidHistoryTotalGet.setText(entity.getResult().getWait_interest());

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
        if (position==mList.size()){return;}
        BidHistoryEntity.ResultBean.ListBean bean = mList.get(position);
        Intent intent = new Intent(BidHistoryActivity.this, BidHistoryDetailActivity.class);
        intent.putExtra("bid_id",bean.getBorrow_id());
        intent.putExtra("bid_name",bean.getName());
        intent.putExtra("bid_tender",bean.getTender_id());
        intent.putExtra("bid_status",bean.getStatus());
        intent.putExtra("bid_status_name",bean.getStatusname());
        startActivity(intent);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPageIndex = 1;
        //设置允许加载更多
        mPullToRefreshView.setLoadMoreEnable(true);
        //移除footer
        mLvHistory.removeFooterView();
        getContent(String.valueOf(mPageIndex));
    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        mPageIndex++;
        getContent(String.valueOf(mPageIndex));
    }

    @OnClick({R.id.btn_left, R.id.btn_right, R.id.tv_none_btn})
    public void onClick(View view) {
        Intent intent = null;
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
        }
    }
}
