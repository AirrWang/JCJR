package com.ql.jcjr.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.entity.MyAccountEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.CommonToast;

/**
 * Created by Airr on 2018/1/16.
 */

public class CapitalStatisticsActivity extends BaseActivity{

    private Context mContext;
    @ViewInject(R.id.tv_total_capital)
    TextView myTotalCapital;
    @ViewInject(R.id.tv_profit)
    TextView myProfit;
    @ViewInject(R.id.tv_capital)
    TextView myCapital;
    @ViewInject(R.id.tv_interest)
    TextView myInterest;
    @ViewInject(R.id.tv_use_money)
    TextView myUseMoney;
    @ViewInject(R.id.tv_tender_frozen)
    TextView myTenderFrozen;
    @ViewInject(R.id.tv_cash_frozen)
    TextView myCashFrozen;
    @ViewInject(R.id.tv_interest_get)
    TextView myInterestGet;
    @ViewInject(R.id.tv_awards)
    TextView myAwards;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calpital_statistics);
        ViewUtils.inject(this);
        mContext = this;
        getCapital();
    }

    private void getCapital() {
        SenderResultModel resultModel = ParamsManager.getMyAccount();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(String responeJson) {
                MyAccountEntity entity = GsonParser.getParsedObj(responeJson, MyAccountEntity.class);
                MyAccountEntity.ResultBean resultBean = entity.getResult();

                myTotalCapital.setText(resultBean.getTotal()+"元");
                myProfit.setText(resultBean.getAllInterest()+"元");
                myCapital.setText("￥ "+resultBean.getCapital());
                myInterest.setText("￥ "+resultBean.getInterest());
                myUseMoney.setText("￥ "+resultBean.getUse_money());
                myTenderFrozen.setText("￥ "+resultBean.getTenderFrozen());
                myCashFrozen.setText("￥ "+resultBean.getCashFrozen());
                myInterestGet.setText("￥ "+resultBean.getInterestGet());
                myAwards.setText("￥ "+resultBean.getAwards());

            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("资金统计 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }
    @OnClick({R.id.btn_left, R.id.tv_official})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }
}
