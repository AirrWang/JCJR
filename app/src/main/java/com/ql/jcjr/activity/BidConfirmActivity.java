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
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.entity.BidDefaultHbEntity;
import com.ql.jcjr.fragment.MyRedPacketFragment;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.ToastUtil;
import com.ql.jcjr.utils.UrlUtil;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.ImageTextHorizontalBarLess;


public class BidConfirmActivity extends BaseActivity {

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

    private String cashid;
    private String hbType;
    private String hbMoney;

    private boolean canUseHB;

    private final int REQUEST_TYPE_HB=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_confirm);
        ViewUtils.inject(this);

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
        String isDay = getIntent().getStringExtra("is_day");
        String time = getIntent().getStringExtra("time");
        String apr = getIntent().getStringExtra("apr");
        String repayType = getIntent().getStringExtra("repay_type");

        mTvBidConfirmName.setRightTitleText(name);
        mTvApr.setRightTitleText(apr+"%");
        switch (isDay) {
            case "0":
                mTvTerm.setRightTitleText(time+"月");
                break;
            case "1":
                mTvTerm.setRightTitleText(time+" 天");
                break;
        }
        mTvRepayType.setRightTitleText(repayType);
        mTvBidConfirmMoney.setRightTitleText("￥ "+totalMoney);
        setRealPayMoney(totalMoney);
        mTvBidConfirmEarn.setRightTitleText("￥ "+earn);

        getBidHongBaoData(mBidId, totalMoney);
    }

    private void getBidHongBaoData(String bidId, String money) {
        SenderResultModel resultModel = ParamsManager.senderBidHBDetail(bidId, money);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("标的红包详情 " + responeJson);
                        BidDefaultHbEntity entity = GsonParser.getParsedObj(responeJson, BidDefaultHbEntity.class);
                        BidDefaultHbEntity.ResultBean resultBean = entity.getResult();

                        if(null == resultBean){
                            canUseHB = false;
                            setNoUse(false);
                        }
                        else{
                            canUseHB = true;
                            setHbInfo(resultBean.getCashid(), resultBean.getType(), resultBean.getMoney(), resultBean.getCashApr());
                        }
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("标的详情失败 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }

    private void setNoUse(boolean isChooseNoUse){
        this.cashid = null;
        this.hbType = null;
        mTvBidConfirmHB.setRightDescriptionVisibility(View.GONE);
        mTvBidConfirmHB.setRightTitleColor(getResources().getColor(R.color.font_grey_three));
        if(isChooseNoUse){
            mTvBidConfirmHB.setRightTitleText("不使用红包");
        }
        else{
            mTvBidConfirmHB.setRightTitleText("无可用红包");
        }
        //实付金额
        setRealPayMoney(totalMoney);
    }

    private void setHbInfo(String cashid, String hbType, String hbMoney, String cashApr){
        this.cashid = cashid;
        this.hbType = hbType;
        this.hbMoney = hbMoney;
        mTvBidConfirmHB.setRightDescriptionVisibility(View.VISIBLE);
        mTvBidConfirmHB.setRightDescriptionColor(getResources().getColor(R.color.btn_main));
        switch (hbType){
            case Global.HB_TYPE_DK:
                mTvBidConfirmHB.setRightTitleText(hbMoney+"元抵扣券");
                mTvBidConfirmHB.setRightDescriptionText("");
                //实付金额
                setRealPayMoney(StringUtils.subAllZero(Float.parseFloat(totalMoney)-Float.parseFloat(hbMoney)+""));
                break;

            case Global.HB_TYPE_JX:
                mTvBidConfirmHB.setRightTitleText(cashApr+"%加息券");
                mTvBidConfirmHB.setRightDescriptionText("加息收益"+hbMoney+"元");
                setRealPayMoney(totalMoney);
                break;

            case Global.HB_TYPE_FX:
                mTvBidConfirmHB.setRightTitleText(hbMoney+"元返现券");
                mTvBidConfirmHB.setRightDescriptionText("返现收益"+hbMoney+"元");
                setRealPayMoney(totalMoney);
                break;
        }
    }

    private void setRealPayMoney(String money){
        mTvBidConfirmPay.setText(StringUtils.subAllZero(money));
    }

    @OnClick({R.id.btn_left, R.id.ll_bid_confirm_checkbox, R.id.tv_bid_confirm_agreement, R.id.tv_bid, R.id.tv_bid_confirm_hb})
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
                    mTvBid.setBackgroundColor(getResources().getColor(R.color.light_gray));
                    mTvBid.setTextColor(getResources().getColor(R.color.font_grey));
                    isChechAgreement = false;
                }
                else{
                    mIvBidConfirmCheckbox.setImageResource(R.drawable.checkbox_select);
                    mTvBidConfirmRead.setTextColor(getResources().getColor(R.color.font_user_agreement));
                    mTvBidConfirmAgreement.setTextColor(getResources().getColor(R.color.btn_main));

                    mTvBid.setBackgroundColor(getResources().getColor(R.color.btn_main));
                    mTvBid.setTextColor(getResources().getColor(R.color.white));
                    isChechAgreement = true;
                }
                break;
            case R.id.tv_bid_confirm_agreement:
                //打开用户协议
                UrlUtil.showHtmlPage(mContext,"借款协议", RequestURL.JCJR_OFFICIAL_URL+mBidId);
                break;
            case R.id.tv_bid:
                if(isChechAgreement){
                    double costMoney = Double.valueOf(mTvBidConfirmPay.getText().toString());
                    double tempMyMoney = Double.valueOf(myMoney);
                    if (tempMyMoney < costMoney) {
                        CommonToast.setIPositiveButtonEventListener(new CommonToast.IPositiveButtonEvent() {
                            @Override
                            public void oClickEvent() {
//                                Intent intent = new Intent(mContext, RechargeActivity.class);
//                                startActivity(intent);
                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.putExtra("main_index",2);
                                startActivity(intent);
                                finish();

                                CommonToast.unRegisteIPositiveButtonEventListener();
                            }
                        });
                        CommonToast.showUnCancelableDialog(mContext, "可用余额不足！");
                    }
                    else{
                        bid();
                    }
                }
                else{
                    ToastUtil.showToast(BidConfirmActivity.this, "您必须先同意积财金融协议");
                }
                break;

            case R.id.tv_bid_confirm_hb:
                if(canUseHB){
                    //选择红包
                    Intent intent = new Intent(BidConfirmActivity.this, MyRedPacketsActivity.class);
                    intent.putExtra("use_type", MyRedPacketFragment.TYPE_USE_HB);
                    intent.putExtra("borrowid", mBidId);
                    intent.putExtra("money", totalMoney);
                    startActivityForResult(intent, REQUEST_TYPE_HB);
                }
                else{
                    ToastUtil.showToast(BidConfirmActivity.this, "当前没有可用红包");
                }
                break;
        }
    }

    private void bid() {
        SenderResultModel resultModel = ParamsManager.senderBid(mBidId, totalMoney, pwd, "", cashid, hbType);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("客户投标 " + responeJson);
                        Intent intent = new Intent(mContext, BidResultActivity.class);
                        intent.putExtra("isSuccess", true);
                        intent.putExtra("result_text", "投标成功");
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("客户投标 " + entity.errorInfo);
//                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                        Intent intent = new Intent(mContext, BidResultActivity.class);
                        intent.putExtra("isSuccess", false);
                        intent.putExtra("result_text", entity.errorInfo);
                        startActivity(intent);
                        finish();
                    }

                }, mContext);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TYPE_HB){
            if(resultCode == MyRedPacketsActivity.RESULT_CODE_NOUSE){
                setNoUse(true);
            }
            else if (resultCode == MyRedPacketsActivity.RESULT_CODE_USEHB){
                setHbInfo(data.getStringExtra("cashid"), data.getStringExtra("hbType"), data.getStringExtra("hbMoney"), data.getStringExtra("cashApr"));
            }
        }
    }
}
