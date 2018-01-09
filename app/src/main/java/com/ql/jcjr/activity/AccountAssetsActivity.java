package com.ql.jcjr.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.ReceiptDetailsAdapter;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.entity.AccountAssetsEntity;
import com.ql.jcjr.entity.ReceiptDetailsEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.ImageTextHorizontalBar;
import com.ql.jcjr.view.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

public class AccountAssetsActivity extends BaseActivity implements PullToRefreshView.OnFooterLoadListener{

    @ViewInject(R.id.tv_total_assets)
    private TextView mTvTotalNum;
    private ImageTextHorizontalBar mIthbAvailableBalance;
    private ImageTextHorizontalBar mIthbToBeReceived;
    private ImageTextHorizontalBar mIthbProfitReceived;
    private ImageTextHorizontalBar mIthbFreezingAmt;
    private TextView tvAccountAssetsDS;
    private LinearLayout llAccountAssetsDS;

    @ViewInject(R.id.pull_refresh_view)
    private PullToRefreshView mPullToRefreshView;
    @ViewInject(R.id.lv_receipt_details)
    private ListView mLvReceiptDetails;

    private Context mContext;
    private ReceiptDetailsAdapter mAdapter;

    // 分页加载索引
    private int mPageIndex = 1;

    List<ReceiptDetailsEntity.ResultBean> mReceiptDetailList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_assets_header);

        ViewUtils.inject(this);
        mContext = this;

        initView();
    }

    private void initView() {
        initListView();
        getAccountAssets();
        getReceiptDetails(String.valueOf(mPageIndex));
    }

    private void initListView() {
        LinearLayout header = (LinearLayout)getLayoutInflater().inflate(R.layout.layout_account_assets_first_header,null);
        mIthbAvailableBalance = (ImageTextHorizontalBar)header.findViewById(R.id.ithb_available_balance);
        mIthbToBeReceived = (ImageTextHorizontalBar)header.findViewById(R.id.ithb_to_be_received);
        mIthbProfitReceived = (ImageTextHorizontalBar)header.findViewById(R.id.ithb_profit_received);
        mIthbFreezingAmt = (ImageTextHorizontalBar)header.findViewById(R.id.ithb_freezing_amt);

        tvAccountAssetsDS = (TextView) header.findViewById(R.id.tv_account_assets_ds);
        llAccountAssetsDS = (LinearLayout) header.findViewById(R.id.ll_account_assets_ds);

        mLvReceiptDetails.addHeaderView(header);
        mAdapter = new ReceiptDetailsAdapter(mContext, mReceiptDetailList);
        mLvReceiptDetails.setAdapter(mAdapter);

        mPullToRefreshView.setPullRefreshEnable(false);
        mPullToRefreshView.setOnFooterLoadListener(this);
    }

    private void getAccountAssets() {
        SenderResultModel resultModel = ParamsManager.senderAccountAssets();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("账户资产 " + responeJson);
                AccountAssetsEntity entity = GsonParser.getParsedObj(responeJson, AccountAssetsEntity.class);
                AccountAssetsEntity.ResultBean resultBean = entity.getResult();
                mTvTotalNum.setText(StringUtils.formatMoney(StringUtils.formatMoney(resultBean.getTotal())));
                mIthbAvailableBalance.setDescriptionText(StringUtils.formatMoney(resultBean.getUse_money()));
                mIthbToBeReceived.setDescriptionText(StringUtils.formatMoney(resultBean.getCollection()));
                mIthbProfitReceived.setDescriptionText(StringUtils.formatMoney(resultBean.getInterest()));
                mIthbFreezingAmt.setDescriptionText(StringUtils.formatMoney(resultBean.getNo_use_money()));
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("账户资产失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

    private void getReceiptDetails(final String pageIndex) {
        SenderResultModel resultModel = ParamsManager.senderReceiptDetails(pageIndex);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("待收明细 " + responeJson);
                ReceiptDetailsEntity entity = GsonParser.getParsedObj(responeJson, ReceiptDetailsEntity.class);

                boolean needShowAll = false;
                int listSize = entity.getResult().size();
                if(pageIndex.equals("1")) {
                    mReceiptDetailList.clear();
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
                    //设置不在加载更多
                    mPullToRefreshView.setLoadMoreEnable(false);
                }

                mReceiptDetailList.addAll(entity.getResult());
                mAdapter.notifyDataSetChanged();

                finishRefresh();
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("待收明细失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

    @OnClick({R.id.btn_left})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }

    private void finishRefresh() {
//        mPullToRefreshView.onHeaderRefreshFinish();
        mPullToRefreshView.onFooterLoadFinish();
        if(mReceiptDetailList.size() == 0){
            //隐藏其他
            tvAccountAssetsDS.setVisibility(View.GONE);
            llAccountAssetsDS.setVisibility(View.GONE);
            mPullToRefreshView.setLoadMoreEnable(false);
        }
    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        LogUtil.i("账户资产onFooterLoad");
        mPageIndex++;
        getReceiptDetails(String.valueOf(mPageIndex));
    }
}
