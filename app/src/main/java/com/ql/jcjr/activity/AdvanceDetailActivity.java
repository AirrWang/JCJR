package com.ql.jcjr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.OtherForzenAdapter;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.entity.AdvanceEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.PullToRefreshView;
import com.ql.jcjr.view.XListView;

import java.util.ArrayList;
import java.util.List;

public class AdvanceDetailActivity extends BaseActivity {


    @ViewInject(R.id.lv_advance)
    ListView mAdvance;
    @ViewInject(R.id.tv_total)
    TextView mTvTotal;
    @ViewInject(R.id.tv_yuqi)
    TextView mTvYuqi;
    @ViewInject(R.id.tv_dianfu)
    TextView mTvDianFu;
    private String tender_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_detail);
        ViewUtils.inject(this);
        initListView();
        tender_id = getIntent().getStringExtra("tender_id");
        getData();
    }

    private OtherForzenAdapter mAdapter;
    List<AdvanceEntity.ResultBean2> mRecordeUnforzenList = new ArrayList<>();
    private void initListView() {
        mAdapter = new OtherForzenAdapter(this, mRecordeUnforzenList);
        mAdvance.setAdapter(mAdapter);
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

    public void getData() {
        SenderResultModel resultModel = ParamsManager.senderadvanceList(tender_id);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("垫付列表 " + responeJson);
                        AdvanceEntity entity = GsonParser.getParsedObj(responeJson, AdvanceEntity.class);
                        mTvTotal.setText("￥"+entity.getResult1().getAccount());
                        mTvYuqi.setText("￥"+entity.getResult1().getLateaccount());
                        mTvDianFu.setText("￥"+entity.getResult1().getDianfuaccount());
                        mRecordeUnforzenList.clear();
                        mRecordeUnforzenList.addAll(entity.getResult2());
                        mAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("垫付列表 " + entity.errorInfo);
                    }

                }, this);

    }
}
