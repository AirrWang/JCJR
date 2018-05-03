package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.ToastUtil;
import com.ql.jcjr.utils.UrlUtil;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.ImageTextHorizontalBarLess;


public class NoviceExclusiveConfirmActivity extends BaseActivity {

    //名称
    @ViewInject(R.id.tv_bid_confirm_name)
    private ImageTextHorizontalBarLess mTvBidConfirmName;
    //年化
    @ViewInject(R.id.tv_annualized_rate)
    private ImageTextHorizontalBarLess mTvApr;
    //期限
    @ViewInject(R.id.tv_term)
    private ImageTextHorizontalBarLess mTvTerm;
    //还款方式
    @ViewInject(R.id.tv_repayment_type)
    private ImageTextHorizontalBarLess mTvRepayType;
    //投资金额
    @ViewInject(R.id.tv_bid_confirm_money)
    private ImageTextHorizontalBarLess mTvBidConfirmMoney;
    //预期收益
    @ViewInject(R.id.tv_bid_confirm_earn)
    private ImageTextHorizontalBarLess mTvBidConfirmEarn;
    //红包
    @ViewInject(R.id.tv_bid_confirm_hb)
    private ImageTextHorizontalBarLess mTvBidConfirmHB;

    //用户协议
    @ViewInject(R.id.ll_bid_confirm_checkbox)
    private LinearLayout mLLBidConfirmCheckbox;
    @ViewInject(R.id.iv_bid_confirm_checkbox)
    private ImageView mIvBidConfirmCheckbox;
    @ViewInject(R.id.tv_bid_confirm_read)
    private TextView mTvBidConfirmRead;
    @ViewInject(R.id.tv_bid_confirm_agreement)
    private TextView mTvBidConfirmAgreement;
    @ViewInject(R.id.tv_1)
    private TextView mTv1;
    @ViewInject(R.id.tv_bid_danger_agreement)
    private TextView mTvBidDangerAgreement;

    private boolean isChechAgreement = true;

    @ViewInject(R.id.tv_bid_confirm_pay)
    private TextView mTvBidConfirmPay;
    @ViewInject(R.id.tv_bid)
    private TextView mTvBid;

    private Context mContext;
    private String mBidId;
    private String totalMoney;
    private String earn;
    private String pwd;
    private String myMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_confirm);
        ViewUtils.inject(this);
        mTvBidConfirmHB.setVisibility(View.GONE);
        mContext = this;
        getIntentData();

    }

    private void getIntentData() {
        String name = getIntent().getStringExtra("name");
        mBidId = getIntent().getStringExtra("bid_id");
        totalMoney = getIntent().getStringExtra("money");
        myMoney = getIntent().getStringExtra("myMoney");
        earn = getIntent().getStringExtra("earn");
        pwd = getIntent().getStringExtra("pwd");
        String time = getIntent().getStringExtra("time");
        String apr = getIntent().getStringExtra("apr");
        String repayType = getIntent().getStringExtra("repay_type");

        mTvBidConfirmName.setRightTitleText(name);
        mTvApr.setRightTitleText(apr+"%");
        mTvTerm.setRightTitleText(time+" 天");

        mTvRepayType.setRightTitleText(repayType);
        mTvBidConfirmMoney.setRightTitleText("￥ "+totalMoney);
        setRealPayMoney(totalMoney);
        mTvBidConfirmEarn.setRightTitleText("￥ "+earn);

    }

    private void setRealPayMoney(String money){
        mTvBidConfirmPay.setText(StringUtils.subAllZero(money));
    }

    @OnClick({R.id.btn_left, R.id.ll_bid_confirm_checkbox, R.id.tv_bid_confirm_agreement, R.id.tv_bid, R.id.tv_bid_confirm_hb,R.id.tv_bid_danger_agreement})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.ll_bid_confirm_checkbox:
                //切换状态
                if(isChechAgreement){
                    mIvBidConfirmCheckbox.setImageResource(R.drawable.checkbox_normal);
                    mTvBidConfirmRead.setTextColor(getResources().getColor(R.color.font_grey));
                    mTvBidConfirmAgreement.setTextColor(getResources().getColor(R.color.font_grey));
                    mTv1.setTextColor(getResources().getColor(R.color.font_grey));
                    mTvBidDangerAgreement.setTextColor(getResources().getColor(R.color.font_grey));
                    mTvBid.setBackgroundColor(getResources().getColor(R.color.light_gray));
                    mTvBid.setTextColor(getResources().getColor(R.color.font_grey));
                    isChechAgreement = false;
                }
                else{
                    mIvBidConfirmCheckbox.setImageResource(R.drawable.checkbox_select);
                    mTvBidConfirmRead.setTextColor(getResources().getColor(R.color.font_user_agreement));
                    mTvBidConfirmAgreement.setTextColor(getResources().getColor(R.color.btn_main));
                    mTv1.setTextColor(getResources().getColor(R.color.btn_main));
                    mTvBidDangerAgreement.setTextColor(getResources().getColor(R.color.btn_main));
                    mTvBid.setBackgroundColor(getResources().getColor(R.color.btn_main));
                    mTvBid.setTextColor(getResources().getColor(R.color.white));
                    isChechAgreement = true;
                }
                break;
            case R.id.tv_bid_confirm_agreement:
                //打开用户协议
                UrlUtil.showHtmlPage(mContext,"借款协议", RequestURL.JCJR_OFFICIAL_URL+mBidId,true);
                break;
            case R.id.tv_bid_danger_agreement:
                //打开用户协议
                UrlUtil.showHtmlPage(mContext,"风险提示书", RequestURL.JCJR_DANGER_URL,true);
                break;

            case R.id.tv_bid:
                if(isChechAgreement){
                    double costMoney = Double.valueOf(mTvBidConfirmPay.getText().toString());
                    double tempMyMoney = Double.valueOf(myMoney);
                    if (tempMyMoney < costMoney) {
                        CommonToast.setIPositiveButtonEventListener(new CommonToast.IPositiveButtonEvent() {
                            @Override
                            public void oClickEvent() {
                                Intent intent = new Intent(mContext, RechargeActivity.class);
                                startActivity(intent);
                                finish();

                                CommonToast.unRegisteIPositiveButtonEventListener();
                            }
                        });
                        CommonToast.showBidDetailDialog(mContext, "可用余额不足！");
                    }
                    else{
                        bid();
                    }
                }
                else{
                    ToastUtil.showToast(NoviceExclusiveConfirmActivity.this, "您必须先同意积财金融协议");
                }
                break;
        }
    }

    private void bid() {
        SenderResultModel resultModel = ParamsManager.senderBid(mBidId, totalMoney, pwd, "");

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("客户投标 " + responeJson);
//                        Intent intent = new Intent(mContext, BidResultActivity.class);
//                        intent.putExtra("isSuccess", true);
//                        intent.putExtra("result_text", "投标成功");
//                        startActivity(intent);
//                        finish();
                        UrlUtil.showHtmlPage(mContext,"投标成功",RequestURL.BID_SUCCESS_URL,true);
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("客户投标 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
//                        Intent intent = new Intent(mContext, BidResultActivity.class);
//                        intent.putExtra("isSuccess", false);
//                        intent.putExtra("result_text", entity.errorInfo);
//                        startActivity(intent);
//                        finish();
                    }

                }, mContext);
    }

}
