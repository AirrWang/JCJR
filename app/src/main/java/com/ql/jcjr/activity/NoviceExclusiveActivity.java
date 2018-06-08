package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.ql.jcjr.entity.HomeDataEntity;
import com.ql.jcjr.entity.MineFragmentEntity;
import com.ql.jcjr.entity.RiskWarningEntity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.KeyboardUtil;
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
    @ViewInject(R.id.ithb_bid_record)
    private ImageTextHorizontalBarLess mTvBidRecord;

    @ViewInject(R.id.ll_detail_progress)
    private RelativeLayout mLlDetailProgress;
    @ViewInject(R.id.ll_detail_rest)
    private RelativeLayout mLlDetailRest;
    @ViewInject(R.id.ll_biao_detail_right)
    private LinearLayout mLinearLayoutDetailGain;


    @ViewInject(R.id.tv_is_lz)
    private TextView mTvIsLz;
    @ViewInject(R.id.tv_bid_detail_getmoney)
    private TextView mTvGet;
    @ViewInject(R.id.tv_bid_detail_title)
    private TextView mTitle;
    @ViewInject(R.id.ll_bq)
    private LinearLayout ll_bq;
    @ViewInject(R.id.ll_time)
    private LinearLayout ll_time;

    private Context mContext;
    private String mBidId;
    private BidDetailEntity.ResultBean resultBean;
    private String bidName;

    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novice_exclusive);
        ViewUtils.inject(this);
        ll_bq.setVisibility(View.GONE);
        ll_time.setVisibility(View.GONE);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mContext = this;

        mTvApr.setTypeface(JcbApplication.getPingFangBoldTypeFace());

        getIntentData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        getBidDetailData(mBidId);
    }
    private void getRiskWarning() {
        SenderResultModel resultModel = ParamsManager.getRisk();
        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("风险测评 " + responeJson);
                        RiskWarningEntity entity = GsonParser.getParsedObj(responeJson, RiskWarningEntity.class);
                        RiskWarningEntity.ResultBean resultBean = entity.getResult();
                        if(StringUtils.isBlank(resultBean.getType())||resultBean.getType()==null){
                            //未测评
                            UserData.getInstance().setRiskWarning(false);
                        }else {
                            //已测评
                            UserData.getInstance().setRiskWarning(true);
                        }
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("风险测评 " + entity.errorInfo);
                    }
                }, mContext);
    }

    private void getIntentData() {
        mTvLoanAfter.setBackgroundResource(R.drawable.font_icon_xged);
        mLlDetailProgress.setVisibility(View.GONE);
        mLlDetailRest.setVisibility(View.GONE);
        mLinearLayoutDetailGain.setVisibility(View.GONE);
        mBidId = getIntent().getStringExtra("bid_id");
        bidName = getIntent().getStringExtra("bid_title");
        mTvTitle.setText(bidName);
        mTitle.setText(bidName);

    }

    private void getBidDetailData(String bidId) {
        SenderResultModel resultModel = ParamsManager.senderBidDetail(bidId);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        if (!UserData.getInstance().getRiskWarning()){
                            getRiskWarning();
                        }
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

                        //计息方式
                        switch (resultBean.getIs_lz()) {
                            case "0":
                                mTvIsLz.setText("满标复审当日计息");
                                break;
                            case "1":
                                mTvIsLz.setText("投资成功当日计息");
                                break;
                        }


                        mTvMinAmt.setText(resultBean.getLowest_account() + "元");
                        mTvLoan.setText(resultBean.getMost_account() + "元");
                        mTvGet.setText(resultBean.getRepaytype());
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            etAmt.setShowSoftInputOnFocus(false);
        }else {
            etAmt.setInputType(InputType.TYPE_NULL);
        }
        new KeyboardUtil(mContext, view, etAmt,66299).showKeyboard();
        //投资期限
        switch (resultBean.getIsday()) {
            case "0":
                tvTerm.setText("期限 " + resultBean.getTime_limit() + " 个月");
                break;
            case "1":
                tvTerm.setText("期限 " + resultBean.getTime_limit_day() + "天");
                break;
        }

        final ActionSheet dialog = new ActionSheet(mContext, ActionSheet.GRAVITY_BOTTOM);
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

        final View view = inflater.inflate(R.layout.bid_dialog, null);

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            etBIdAmt.setShowSoftInputOnFocus(false);
        }else {
            etBIdAmt.setInputType(InputType.TYPE_NULL);
        }
        new KeyboardUtil(mContext, view, etBIdAmt,0).showKeyboard();
        etBIdAmt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new KeyboardUtil(mContext, view, etBIdAmt,0).showKeyboard();
                return false;
            }
        });

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
                    Intent intent = new Intent(NoviceExclusiveActivity.this, BidConfirmActivity.class);
                    intent.putExtra("money", etBIdAmt.getText().toString());
                    intent.putExtra("earn", tvExpectedReturn.getText().toString());
                    intent.putExtra("bid_id",mBidId);
                    intent.putExtra("name", bidName);
                    intent.putExtra("time",resultBean.getTime_limit_day());
                    intent.putExtra("apr",resultBean.getApr());
                    intent.putExtra("repay_type",resultBean.getRepaytype());
                    intent.putExtra("pwd", "");
                    intent.putExtra("is_day",resultBean.getIsday());

                    intent.putExtra("myMoney", balance);
                    startActivity(intent);
//                    bid(mBidId, etBIdAmt.getText().toString(), resultBean.getPwd(),
//                            resultBean.getType());
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

//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                imm.showSoftInput(etBIdAmt,InputMethodManager.SHOW_IMPLICIT);
//            }
//        }, 200);
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
        double mostBid=Double.valueOf(resultBean.getMost_account());


        if (myEnter < lowestBid) {
            CommonToast.makeCustomText(mContext, "最小投资金额为"+lowestBid+"元！");
            return false;
        }

//        if (myBalance < myEnter) {
//            CommonToast.setIPositiveButtonEventListener(new CommonToast.IPositiveButtonEvent() {
//                @Override
//                public void oClickEvent() {
//                    Intent intent = new Intent(mContext, RechargeActivity.class);
//                    startActivity(intent);
//                    finish();
//                    CommonToast.unRegisteIPositiveButtonEventListener();
//                }
//            });
//            CommonToast.showBidDetailDialog(mContext, "可用余额不足！");
//            return false;
//        }

        if (myEnter>mostBid){
            CommonToast.makeCustomText(mContext, "最大投资金额为"+mostBid+"元！");
            return false;
        }

        if (surplus <myEnter){
            CommonToast.makeCustomText(mContext, "剩余可投金额为"+surplus+"元！");
            return false;
        }

        return true;
    }

    private void bid(String tenderId, String amt, String dxbPWD, String lcb) {
        SenderResultModel resultModel = ParamsManager.senderBid(tenderId, amt, dxbPWD, lcb);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("客户投标 " + responeJson);
//                        Intent intent = new Intent(mContext, BidResultActivity.class);
//                        intent.putExtra("isSuccess", true);
//                        intent.putExtra("result_text", "投标成功");
//                        startActivity(intent);
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
                if (!UserData.getInstance().isLogin()){
                    if (UserData.getInstance().getPhoneNumber().equals("")) {
                        Intent intent = new Intent(mContext, LoginActivityCheck.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.putExtra("phone_num", UserData.getInstance().getPhoneNumber());
                        startActivity(intent);
                    }
                    break;
                }
                if (StringUtils.isNotBlank(UserData.getInstance().getRealName())) {
                    if (UserData.getInstance().getRiskWarning()) {
                            getAccountInfo();
                    }else {
                        showToTestDialog();
                    }
                } else {
                    CommonToast.showShiMingDialog(mContext);
                }
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
                UrlUtil.showHtmlPage(mContext,"安全保障", RequestURL.BID_AQBZ_URL,true);
                break;
            case R.id.ithb_project_detail:
                if(resultBean != null) {
                    UrlUtil.showHtmlPage(mContext,"项目详情", RequestURL.PROJECT_DETAIL_URL + resultBean.getId(),true);
                }
                break;
        }
    }

    private void showToTestDialog() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.bid_totest_dialog, null);
        ViewUtils.inject(this, view);

        Button btnBid = (Button) view.findViewById(R.id.btn_bid_to_test);
        LinearLayout ll_close= (LinearLayout) view.findViewById(R.id.ll_close);


        final ActionSheet dialog = new ActionSheet(mContext, ActionSheet.GRAVITY_CENTER);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UrlUtil.showHtmlPage(mContext,"风险测评", RequestURL.RISKTEST_URL,true);
                dialog.dismiss();
            }
        });
    }

    private void getData() {
        SenderResultModel resultModel = ParamsManager.getHomeData();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("首页数据获取成功 " + responeJson);
                HomeDataEntity entity = GsonParser.getParsedObj(responeJson, HomeDataEntity.class);
                 if (entity.getResult().getResult1().getCode().equals("3")){
                    finish();
                }

            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("广告栏失败 " + entity.errorInfo);
            }

        }, mContext);

    }
}
