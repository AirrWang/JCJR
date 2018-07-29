package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.OtherForzenAdapter;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.entity.AdvanceEntity;
import com.ql.jcjr.entity.OtherForzenEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.PFMediaText;
import com.ql.jcjr.view.PullToRefreshView;
import com.ql.jcjr.view.XListView;

import java.util.ArrayList;
import java.util.List;

import static com.taobao.accs.ACCSManager.mContext;

public class OtherCashForzenActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterLoadListener {

    @ViewInject(R.id.lv_other_forzen)
    XListView mOtherForzen;
    @ViewInject(R.id.pull_refresh_view)
    private PullToRefreshView mPullRefresh;
    @ViewInject(R.id.ll_top)
    LinearLayout mLlTop;

    private Context mContext;
    private OtherForzenAdapter mAdapter;
    List<AdvanceEntity.ResultBean2> mRecordeUnforzenList = new ArrayList<>();
    private View headerView;
    private PFMediaText mTvOtherForzen;
    // 分页加载索引
    private int mPageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_cash_forzen);
        ViewUtils.inject(this);
        mContext = this;
        initListView();
        initId();
        mPullRefresh.setOnHeaderRefreshListener(this);
        mPullRefresh.setOnFooterLoadListener(this);
        getData();
    }

    private void getData() {
        SenderResultModel resultModel = ParamsManager.senderUnforzen(mPageIndex+"");

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("解冻列表" + responeJson);
                        OtherForzenEntity entity = GsonParser.getParsedObj(responeJson, OtherForzenEntity.class);
                        mTvOtherForzen.setText(entity.getLast_freeze_money()+"元");

                        if(mPageIndex==1) {
                            mRecordeUnforzenList.clear();
                        }
                        boolean needShowAll = false;
                        if (entity.getResult()==null||entity.getResult().size()==0){
                            needShowAll = true;
                        }
                        if(needShowAll){
                            mOtherForzen.addFooterView();
                            //设置不在加载更多
                            mPullRefresh.setLoadMoreEnable(false);
                        }else {
                            mOtherForzen.removeFooterView();
                            //设置可以加载更多
                            mPullRefresh.setLoadMoreEnable(true);
                        }
                        mRecordeUnforzenList.addAll(entity.getResult());
                        mAdapter.notifyDataSetChanged();
                        finishRefresh();
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("解冻列表" + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                        finishRefresh();
                    }

                }, mContext);

    }

    private void initId() {
        mTvOtherForzen = (PFMediaText) headerView.findViewById(R.id.rftv_other_forzen);

    }

    private void initListView() {
        headerView = LayoutInflater.from(this).inflate(R.layout.head_other_forzen_view,null);
        mOtherForzen.addHeaderView(headerView);

        mAdapter = new OtherForzenAdapter(this, mRecordeUnforzenList);
        mOtherForzen.setAdapter(mAdapter);
    }

    @OnClick({R.id.btn_left})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        mPageIndex = 1;

        //设置允许加载更多
        mPullRefresh.setLoadMoreEnable(true);
        //移除footer
        mOtherForzen.removeFooterView();
        getData();
    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {
        mPageIndex++;
        getData();
    }

    private void finishRefresh() {
        mPullRefresh.onHeaderRefreshFinish();
        mPullRefresh.onFooterLoadFinish();
    }
}
