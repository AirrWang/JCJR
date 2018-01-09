package com.ql.jcjr.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.TenderRecordAdapter;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.entity.BidDetailEntity;

import java.util.ArrayList;
import java.util.List;

public class TenderRecordActivity extends BaseActivity {

    @ViewInject(R.id.lv_tender_record)
    private ListView mLvTenderRecord;

    private Context mContext;

    private List<BidDetailEntity.ResultBean.TendersBean> mTenderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tender_record);

        ViewUtils.inject(this);
        mContext = this;
        List<BidDetailEntity.ResultBean.TendersBean> list =
                (List<BidDetailEntity.ResultBean.TendersBean>) getIntent().getSerializableExtra("tender_list");
        mTenderList.addAll(list) ;
        initListView();
    }

    private void initListView() {
        TenderRecordAdapter mTenderRecordAdapter = new TenderRecordAdapter(mContext, mTenderList);
        mLvTenderRecord.setAdapter(mTenderRecordAdapter);
    }

    @OnClick({R.id.btn_left})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_left:
                finish();
                break;
        }
    }
}
