package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.entity.BidDetailEntity;
import com.ql.jcjr.entity.MineFragmentEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.UrlUtil;
import com.ql.jcjr.view.ActionSheet;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.ImageTextHorizontalBarLess;
import com.ql.jcjr.view.InputAmountEditText;

import java.io.Serializable;


public class NoviceExclusiveActivity extends BaseActivity {

    @ViewInject(R.id.tv_title)
    private TextView mTvTitle;
    @ViewInject(R.id.tv_apr)
    private TextView mTvApr;
    @ViewInject(R.id.tv_term)
    private TextView mTvTerm;
    @ViewInject(R.id.tv_min_amt)
    private TextView mTvMinAmt;
    @ViewInject(R.id.tv_loan)
    private TextView mTvLoan;
    @ViewInject(R.id.tv_loan_after)
    private View mTvLoanAfter;
//    @ViewInject(R.id.tv_repayment_type)
//    private TextView mTvRepayType;
    @ViewInject(R.id.ithb_bid_record)
    private ImageTextHorizontalBarLess mTvBidRecord;

    @ViewInject(R.id.ll_detail_progress)
    private RelativeLayout mLlDetailProgress;
    @ViewInject(R.id.ll_detail_rest)
    private RelativeLayout mLlDetailRest;
    @ViewInject(R.id.ll_biao_detail_right)
    private LinearLayout mLinearLayoutDetailGain;

    private Context mContext;
    private String mBidId;
    private BidDetailEntity.ResultBean resultBean;

    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novice_exclusive);
        ViewUtils.inject(this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mContext = this;

        mTvApr.setTypeface(JcbApplication.getPingFangBoldTypeFace());

        getIntentData();
    }

    private void getIntentData() {
//        mTvLoanAfter.setText("限购额度");
        mTvLoanAfter.setBackgroundResource(R.drawable.font_icon_xged);
        mLlDetailProgress.setVisibility(View.GONE);
        mLlDetailRest.setVisibility(View.GONE);
        mLinearLayoutDetailGain.setVisibility(View.GONE);
        mBidId = getIntent().getStringExtra("bid_id");
        mTvTitle.setText("新手专享");
        getBidDetailData(mBidId);
    }

    private void getBidDetailData(String bidId) {
        SenderResultModel resultModel = ParamsManager.senderBidDetail(bidId);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("新手专享详情 " + responeJson);
                        BidDetailEntity entity = GsonParser.getParsedObj(responeJson, BidDetailEntity.class);
                        resultBean = entity.getResult();

                        mTvApr.setText(resultBean.getApr());

                        //投资期限
                        switch (resultBean.getIsday()) {
                            case "0":
                                mTvTerm.setText(resultBean.getTime_limit() + "个月");
                                break;
                            case "1":
                                mTvTerm.setText(resultBean.getTime_limit_day() + "天");
                                break;
                        }

                        mTvMinAmt.setText(resultBean.getLowest_account() + "元");
                        mTvLoan.setText(resultBean.getMost_account() + "元");

                        mTvBidRecord.setRightTitleText(resultBean.getTenderNum() + "人");
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("新手专享失败 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }

    /**
     * 计算器
     */
    private void showCalculatorDialog() {

        if (resultBean == null) {
            return;
        }

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.calculator_dialog, null);
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);
        TextView tvApr = (TextView) view.findViewById(R.id.tv_annualized_rate);
        final TextView tvTerm = (TextView) view.findViewById(R.id.tv_term);
        final InputAmountEditText etAmt = (InputAmountEditText) view.findViewById(R.id.et_amt);
        final TextView tvExpectedReturn = (TextView) view.findViewById(R.id.tv_expected_return);

        tvApr.setText("年化 " + resultBean.getApr() + "%");

        //投资期限
        switch (resultBean.getIsday()) {
            case "0":
                tvTerm.setText("期限 " + resultBean.getTime_limit() + " 个月");
                break;
            case "1":
                tvTerm.setText("期限 " + resultBean.getTime_limit_day() + "天");
                break;
        }

        final ActionSheet dialog = new ActionSheet(mContext, ActionSheet.GRAVITY_CENTER);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        etAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                caculate(etAmt, tvExpectedReturn);

            }
        });
    }

    /**
     * 投标
     */
    private void showBidDialog(final String balance) {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.bid_dialog, null);

        ViewUtils.inject(this, view);

        TextView ivClose = (TextView) view.findViewById(R.id.iv_close);
        Button btnBid = (Button) view.findViewById(R.id.btn_bid);
        TextView tvBalance = (TextView) view.findViewById(R.id.tv_balance);
        TextView tvBidApr = (TextView) view.findViewById(R.id.tv_bid_apr);
        //剩余可投jine
        TextView tvMaxBidPre = (TextView) view.findViewById(R.id.tv_max_bid_pre);
        TextView tvMaxBid = (TextView) view.findViewById(R.id.tv_max_bid);
        TextView tvAllSurplus = (TextView) view.findViewById(R.id.tv_all_surplus);
        final InputAmountEditText etBIdAmt =
                (InputAmountEditText) view.findViewById(R.id.et_bid_amt);
        final TextView tvExpectedReturn = (TextView) view.findViewById(R.id.tv_expected_return);

        final ActionSheet dialog = new ActionSheet(mContext, ActionSheet.GRAVITY_BOTTOM);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        tvMaxBidPre.setText("限购额度：");
        tvBidApr.setText(resultBean.getApr()+"%");
        tvBalance.setText(balance + "元");
        tvMaxBid.setText(resultBean.getMost_account() + "元");

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvAllSurplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double myBalance = Double.valueOf(balance);
                double surplus = Double.valueOf(resultBean.getMost_account());
                double money = Math.floor(myBalance>surplus?surplus:myBalance);
//                double money;
//                if(myBalance>surplus){
//                    money = surplus;
//                }
//                else{
//                    money = Math.floor(myBalance);
//                }
                String moneyStr = StringUtils.subAllZero(money+"");
                etBIdAmt.setText(moneyStr);
                etBIdAmt.setSelection(moneyStr.length());
            }
        });

        btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (checkInfo(etBIdAmt, balance)) {
                    bid(mBidId, etBIdAmt.getText().toString(), resultBean.getPwd(),
                            resultBean.getType());
                }
            }
        });

        etBIdAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        s.delete(0, 1);
                    }
                }

                String text = s.toString();
                if(text.indexOf(".")!=-1){
                    s.delete(s.length()-1, s.length());
                }

                caculate(etBIdAmt, tvExpectedReturn);
            }
        });

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                imm.showSoftInput(etBIdAmt,InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);
    }

    private void caculate(InputAmountEditText editText, TextView textView) {

        if (StringUtils.isBlank(editText.getText().toString())) {
            textView.setText("0.00");
            return;
        }

        String expectedReturn = "0.00";
        double amt = Double.valueOf(editText.getText().toString());
        double apr = Double.valueOf(resultBean.getApr());

        //投资期限
        switch (resultBean.getIsday()) {
            case "0":
                //月
                double month = Double.valueOf(resultBean.getTime_limit());
                expectedReturn = String.valueOf(amt * apr * month / 1200);

                break;
            case "1":
                //天
                double term = Double.valueOf(resultBean.getTime_limit_day());
//                int days = DateUtil.getCurrentMonthDays();
                int days = 30;
                expectedReturn = String.valueOf(amt * (apr / 1200 / days) * term);
                break;
        }
        textView.setText(StringUtils.formatMoney(expectedReturn));
    }

    private boolean checkInfo(InputAmountEditText etBIdAmt, String balance) {
        if (StringUtils.isBlank(etBIdAmt.getText().toString().trim())) {
            CommonToast.showHintDialog(mContext, "请输入金额");
            return false;
        }

        double myBalance = Double.valueOf(balance);
        double surplus = Double.valueOf(resultBean.getMost_account());
        double lowestBid = Double.valueOf(resultBean.getLowest_account());
        double myEnter = Double.valueOf(etBIdAmt.getText().toString());

        if (myEnter < lowestBid) {
//            CommonToast.showUnCancelableDialog(mContext, "起投金额 " + resultBean.getLowest_account() + " 元");
            CommonToast.showUnCancelableDialog(mContext, "投资金额低于起投金额！");
            return false;
        }

        if (myBalance < myEnter) {
            CommonToast.setIPositiveButtonEventListener(new CommonToast.IPositiveButtonEvent() {
                @Override
                public void oClickEvent() {
//                    Intent intent = new Intent(mContext, RechargeActivity.class);
//                    startActivity(intent);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("main_index",2);
                    startActivity(intent);
                    finish();

                    CommonToast.unRegisteIPositiveButtonEventListener();
                }
            });
            CommonToast.showUnCancelableDialog(mContext, "可用余额不足！");
            return false;
        }

        if (surplus < myEnter) {
            CommonToast.showHintDialog(mContext, "投资金额超过限额！");
            return false;
        }

//        if ((Double.valueOf(resultBean.getSurplus()) - Double.valueOf(etBIdAmt.getText().toString())) < 0) {
//            CommonToast.showHintDialog(mContext, "投资金额超过剩余额度！");
//            return false;
//        }

        return true;
    }

    private void bid(String tenderId, String amt, String dxbPWD, String lcb) {
        SenderResultModel resultModel = ParamsManager.senderBid(tenderId, amt, dxbPWD, lcb);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("客户投标 " + responeJson);
                        Intent intent = new Intent(mContext, BidResultActivity.class);
                        intent.putExtra("isSuccess", true);
                        intent.putExtra("result_text", "投标成功");
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("客户投标 " + entity.errorInfo);
//                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                        Intent intent = new Intent(mContext, BidResultActivity.class);
                        intent.putExtra("isSuccess", false);
                        intent.putExtra("result_text", entity.errorInfo);
                        startActivity(intent);
                    }

                }, mContext);
    }

    private void getAccountInfo() {
        SenderResultModel resultModel = ParamsManager.senderMineFragment();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("账户信息 " + responeJson);
                        MineFragmentEntity entity =
                                GsonParser.getParsedObj(responeJson, MineFragmentEntity.class);
                        MineFragmentEntity.ResultBean resultBean = entity.getResult();
                        showBidDialog(resultBean.getUse_money());
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("账户信息失败 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }

    @OnClick({R.id.btn_left, R.id.iv_calculator, R.id.tv_bid, R.id.ithb_bid_record, R.id.ithb_bid_reward, R.id.ithb_project_detail})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.iv_calculator:
                showCalculatorDialog();
                break;
            case R.id.tv_bid:
                getAccountInfo();
                break;
            case R.id.ithb_bid_record:
                if(resultBean != null) {
                    Intent intent = new Intent(mContext, TenderRecordActivity.class);
                    intent.putExtra("tender_list", (Serializable) resultBean.getTenders());
                    startActivity(intent);
                }
                break;
            case R.id.ithb_bid_reward:
//                if(resultBean != null) {
//                    Intent intentReward = new Intent(mContext, TenderAwardActivity.class);
//                    intentReward.putExtra("tender_reward", resultBean.getAward());
//                    startActivity(intentReward);
//                }
                UrlUtil.showHtmlPage(mContext,"安全保障", RequestURL.BID_AQBZ_URL);
                break;
            case R.id.ithb_project_detail:
                if(resultBean != null) {
                    UrlUtil.showHtmlPage(mContext,"项目详情", RequestURL.PROJECT_DETAIL_URL + resultBean.getId());
                }
                break;
        }
    }
}
