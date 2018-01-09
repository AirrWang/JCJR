package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.BankListBigAdapter;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.entity.BankListEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.CommonToast;

import java.util.ArrayList;
import java.util.List;

public class BankListActivity extends BaseActivity{

    @ViewInject(R.id.lv_list)
    private ListView mLvList;

    private Context mContext;
    //银行列表
    private List<BankListEntity.ResultBean> bankList = new ArrayList<>();
    private BankListBigAdapter mBankCardListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_list);
        ViewUtils.inject(this);
        mContext = this;
        initListView();
        getList();
    }

    private void initListView() {
        mBankCardListAdapter = new BankListBigAdapter(mContext, bankList);
        mLvList.setAdapter(mBankCardListAdapter);
        mLvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BankListEntity.ResultBean bean = bankList.get(position);
                Intent intent = new Intent();
                intent.putExtra("bank_id",bean.getId());
                intent.putExtra("bank_name",bean.getName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void getList() {
        SenderResultModel resultModel = ParamsManager.senderGetBankList();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("银行列表 " + responeJson);
                        BankListEntity entity = GsonParser.getParsedObj(responeJson, BankListEntity.class);
                        bankList.addAll(entity.getResult());
                        mBankCardListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("银行列表 " + entity.errorInfo);
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
}
