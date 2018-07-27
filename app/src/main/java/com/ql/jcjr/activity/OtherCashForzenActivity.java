package com.ql.jcjr.activity;

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
import com.ql.jcjr.view.PullToRefreshView;
import com.ql.jcjr.view.XListView;

import java.util.ArrayList;
import java.util.List;

public class OtherCashForzenActivity extends BaseActivity implements PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterLoadListener {

    @ViewInject(R.id.lv_other_forzen)
    XListView mOtherForzen;
    @ViewInject(R.id.pull_refresh_view)
    private PullToRefreshView mPullRefresh;
    @ViewInject(R.id.ll_top)
    LinearLayout mLlTop;

    private OtherForzenAdapter mAdapter;
    List<String> mRecordeUnforzenList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_cash_forzen);
        ViewUtils.inject(this);
        initListView();
        mPullRefresh.setOnHeaderRefreshListener(this);
        mPullRefresh.setOnFooterLoadListener(this);
    }

    private void initListView() {
        View headerView = LayoutInflater.from(this).inflate(R.layout.head_other_forzen_view,null);
        mOtherForzen.addHeaderView(headerView);
        for (int i=0;i<50;i++){
            mRecordeUnforzenList.add(i+"");
        }

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

    }

    @Override
    public void onFooterLoad(PullToRefreshView view) {

    }
}
