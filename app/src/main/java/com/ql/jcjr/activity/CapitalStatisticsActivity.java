package com.ql.jcjr.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.ql.jcjr.view.PFMediaText;

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
    @ViewInject(R.id.pftv_total)
    PFMediaText pfMediaText;
    @ViewInject(R.id.pftv_earn)
    PFMediaText mPftvEarn;
    @ViewInject(R.id.ll_1)
    LinearLayout mLL1;
    @ViewInject(R.id.ll_2)
    LinearLayout mLL2;
    @ViewInject(R.id.iv_earn)
    ImageView mIvEarn;
    @ViewInject(R.id.iv_total)
    ImageView mIvTotal;
    @ViewInject(R.id.tv_cash_froze_other)
    TextView mTvCashFrozenOther;
    @ViewInject(R.id.tv_cash_overdue)
    TextView mTvCashOverdue;
    @ViewInject(R.id.sv_myaccount)
    ScrollView mSvMyaccount;
    private Animation rotateUp;
    private Animation rotate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calpital_statistics);
        ViewUtils.inject(this);
        mContext = this;
        getCapital();

        //创建动画
        rotateUp = AnimationUtils.loadAnimation(this, R.anim.rotate_anim_up);
        rotateUp.setInterpolator(new LinearInterpolator());//设置为线性旋转
        rotateUp.setFillAfter(true);

        //创建动画
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        rotate.setInterpolator(new LinearInterpolator());//设置为线性旋转
        rotate.setFillAfter(true);

        mLL1.setVisibility(View.VISIBLE);
        mIvTotal.startAnimation(rotate);
    }

    private void getCapital() {
        SenderResultModel resultModel = ParamsManager.getMyAccount();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("资金统计 " + responeJson);
                MyAccountEntity entity = GsonParser.getParsedObj(responeJson, MyAccountEntity.class);
                MyAccountEntity.ResultBean resultBean = entity.getResult();

                myTotalCapital.setText(resultBean.getTotal()+"元");
                pfMediaText.setText("￥"+resultBean.getTotal());
                myProfit.setText(resultBean.getAllInterest()+"元");
                mPftvEarn.setText("￥"+resultBean.getAllInterest());
                myCapital.setText("￥"+resultBean.getCapital());
                myInterest.setText("￥"+resultBean.getInterest());
                myUseMoney.setText("￥"+resultBean.getUse_money());
                myTenderFrozen.setText("￥"+resultBean.getTenderFrozen());
                myCashFrozen.setText("￥"+resultBean.getCashFrozen());
                myInterestGet.setText("￥"+resultBean.getInterestGet());
                myAwards.setText("￥"+resultBean.getAwards());
                mTvCashFrozenOther.setText("￥"+resultBean.getOther_freeze_money());
                mTvCashOverdue.setText("￥"+resultBean.getLate_account());
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("资金统计 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

    private Boolean isShowTop=true;
    private Boolean isShowBottom=false;
    @OnClick({R.id.btn_left, R.id.tv_official,R.id.ll_overdue,R.id.ll_other_forzen,R.id.ll_cash_total,R.id.ll_cash_earn})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.ll_overdue:
                intent = new Intent(mContext, BidHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_other_forzen:
                intent.setClass(this,OtherCashForzenActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_cash_total:
                if (isShowTop){
                    mIvTotal.startAnimation(rotateUp);
                    mLL1.setVisibility(View.GONE);
                }else {
                    mLL1.setVisibility(View.VISIBLE);
                    mIvTotal.startAnimation(rotate);
                }

                isShowTop=!isShowTop;
                break;
            case R.id.ll_cash_earn:
                if (isShowBottom){
                    mIvEarn.startAnimation(rotateUp);
                    mLL2.setVisibility(View.GONE);
                }else {
                    mLL2.setVisibility(View.VISIBLE);
                    mIvEarn.startAnimation(rotate);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            mSvMyaccount.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }

                isShowBottom=!isShowBottom;
                break;
        }
    }
}
