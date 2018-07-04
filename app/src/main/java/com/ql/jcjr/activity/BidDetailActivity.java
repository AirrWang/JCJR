package com.ql.jcjr.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
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
import com.ql.jcjr.entity.RiskWarningEntity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.DisplayUnitUtils;
import com.ql.jcjr.utils.KeyboardUtil;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.ToastUtil;
import com.ql.jcjr.utils.UrlUtil;
import com.ql.jcjr.view.ActionSheet;
import com.ql.jcjr.view.CommonLoadingDialog;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.ImageTextHorizontalBarLess;
import com.ql.jcjr.view.InputAmountEditText;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.ql.jcjr.utils.DisplayUnitUtils.getDisplayHeight;
import static com.ql.jcjr.utils.DisplayUnitUtils.getDisplayWidth;

//投标详情
public class BidDetailActivity extends BaseActivity {

    @ViewInject(R.id.progressBar)
    private ProgressBar mProgressBar;

    @ViewInject(R.id.iv_progress_light)
    private ImageView mIvProgressLight;

    @ViewInject(R.id.tv_title)
    private TextView mTvTitle;
//    @ViewInject(R.id.tv_title_second)
//    private ImageTextHorizontalBarLess mTvTitleSecond;
    //原年化
    @ViewInject(R.id.tv_apr)
    private TextView mTvApr;
    //活动年化
//    @ViewInject(R.id.tv_apr_gain)
//    private TextView mTvAprGain;
    //活动年化 ll
//    @ViewInject(R.id.ll_biao_detail_right)
//    private LinearLayout mLinearLayoutDetailGain;

    @ViewInject(R.id.tv_term)
    private TextView mTvTerm;
    @ViewInject(R.id.tv_min_amt)
    private TextView mTvMinAmt;
    @ViewInject(R.id.tv_loan)
    private TextView mTvLoan;
    @ViewInject(R.id.tv_surplus)
    private TextView mTvSurplus;

    @ViewInject(R.id.tv_percent)
    private TextView mTvPercent;

    @ViewInject(R.id.tv_bid_detail_title)
    private TextView mTitle;
    @ViewInject(R.id.tv_bid_detail_getmoney)
    private TextView mTvGet;
    @ViewInject(R.id.tv_bid_time)
    private TextView tv_bid_time;

//    @ViewInject(R.id.tv_repayment_type)
//    private ImageTextHorizontalBarLess mTvRepayType;
    @ViewInject(R.id.ithb_bid_record)
    private ImageTextHorizontalBarLess mTvBidRecord;
    @ViewInject(R.id.tv_bid)
    private TextView mTvBid;
    @ViewInject(R.id.iv_calculator)
    private ImageView mIvCalculator;
    @ViewInject(R.id.tv_is_lz)
    private TextView mTvIsLz;
    @ViewInject(R.id.tv_tag_1)
    private TextView tv_tag_1;
    @ViewInject(R.id.tv_tag_2)
    private TextView tv_tag_2;
    @ViewInject(R.id.iv_que)
    private ImageView iv_que;


    private Context mContext;
    private String mBidId;
    private String bidName;
    private String bidPwd = "";
    private boolean isBuyAll;
    private String myMoney;
    private CommonLoadingDialog loadingDialog;

    private BidDetailEntity.ResultBean resultBean;

    private InputMethodManager imm;
    private boolean isOver;
    private PopupWindow popupWindow;
    private View popView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_detail);
        ViewUtils.inject(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mContext = this;

        loadingDialog = new CommonLoadingDialog(mContext);
        loadingDialog.setCancelable(false);// 需要根据是不是允许取消来设置dialog可以不可以取消
        loadingDialog.setCanceledOnTouchOutside(false);

        mTvApr.setTypeface(JcbApplication.getPingFangBoldTypeFace());
//        mTvAprGain.setTypeface(JcbApplication.getPingFangBoldTypeFace());

        initPOP();
    }

    private void initPOP() {
        popupWindow = new PopupWindow(this);
        popView = LayoutInflater.from(this).inflate(R.layout.pop_bid_detail_quesition,null);
        popupWindow.setContentView(popView);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getIntentData();
        if (!UserData.getInstance().getRiskWarning()){
            getRiskWarning();
        }
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
        mBidId = getIntent().getStringExtra("bid_id");
        bidName = getIntent().getStringExtra("bid_title");
        mTvTitle.setText(bidName);
        mTitle.setText(bidName);
        getBidDetailData(mBidId);

        Map<String, String> datas = new HashMap<String, String>();
        datas.put("bid",mBidId);
        MobclickAgent.onEventValue(this, "bid_detail", datas, 1);
    }

    private void getBidDetailData(String bidId) {
        SenderResultModel resultModel = ParamsManager.senderBidDetail(bidId);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("标的详情 " + responeJson);
                        BidDetailEntity entity =
                                GsonParser.getParsedObj(responeJson, BidDetailEntity.class);
                        resultBean = entity.getResult();
                        mTvApr.setText(resultBean.getApr());
                        String cashAddition = resultBean.getCashAddition();
                        if(cashAddition.equals("0") || cashAddition.equals("0.0")){
//                            mLinearLayoutDetailGain.setVisibility(View.GONE);
                        }
                        else{
//                            mLinearLayoutDetailGain.setVisibility(View.VISIBLE);
//                            mTvAprGain.setText(resultBean.getCashAddition());
                        }

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
                        if (StringUtils.isBlank(resultBean.getBremark())){
                            tv_tag_1.setVisibility(View.GONE);
                        }else {
                            tv_tag_1.setVisibility(View.VISIBLE);
                            tv_tag_1.setText(resultBean.getBremark());
                            if (!StringUtils.isBlank(resultBean.getUrl())){
                                tv_tag_1.setText(resultBean.getBremark()+">");
                                tv_tag_1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UrlUtil.showHtmlPage(mContext,"",resultBean.getUrl(),true);
                                    }
                                });
                            }
                        }
                        if (StringUtils.isBlank(resultBean.getBremark1())){
                            tv_tag_2.setVisibility(View.GONE);
                        }else {
                            tv_tag_2.setVisibility(View.VISIBLE);
                            tv_tag_2.setText(resultBean.getBremark1());
                            if (!StringUtils.isBlank(resultBean.getUrl1())){
                                tv_tag_2.setText(resultBean.getBremark1()+">");
                                tv_tag_2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UrlUtil.showHtmlPage(mContext,"",resultBean.getUrl1(),true);
                                    }
                                });
                            }
                        }

                        mTvMinAmt.setText(resultBean.getLowest_account() + "元");
                        mTvLoan.setText(resultBean.getAccount() + "元");
                        String surplus = resultBean.getSurplus();
                        mTvSurplus.setText(surplus);
                        mTvGet.setText(resultBean.getRepaytype());
                        mTvBidRecord.setRightTitleText(resultBean.getTenderNum() + "人");

                        double account = Double.valueOf(resultBean.getAccount());
                        double accountYes = Double.valueOf(resultBean.getAccount_yes());
                        mProgressBar.setMax((int) account);
                        mProgressBar.setProgress((int) accountYes);

                        int width = DisplayUnitUtils.dip2px(26, BidDetailActivity.this);
                        int height = DisplayUnitUtils.dip2px(18, BidDetailActivity.this);
                        int totalW = mProgressBar.getWidth();

                        if(accountYes == 0){
                            mIvProgressLight.setVisibility(View.GONE);
                        }
                        else{
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
                            lp.setMargins((int)(accountYes*totalW/account-width*0.5), 0, 0, 0);
                            mIvProgressLight.setLayoutParams(lp);
                        }

                        if(StringUtils.isBlank(surplus) || "0.00".equals(surplus) || "0".equals(surplus)) {
                            isOver = true;
                            mTvBid.setEnabled(false);
                            mTvBid.setText("已售罄，可选择其他项目购买");
                            mIvCalculator.setVisibility(View.GONE);
                            mTvBid.setBackgroundColor(getResources().getColor(R.color.font_grey));
                            mTvBid.setTextColor(getResources().getColor(R.color.font_grey_four));
                            mTvPercent.setText("100%");
                            mIvProgressLight.setVisibility(View.GONE);
                        }else{
                            isOver=false;
                            mTvBid.setEnabled(true);
                            if(Double.valueOf(resultBean.getSurplus()) <= Double.valueOf(resultBean.getLowest_account())){
                                isBuyAll = true;
                                mTvBid.setText("全部买入");
                            }

                            //处理有人投了标，但是进度还是0的情况
                            int percent = (int)(accountYes*100/account);
                            if(percent == 0 && accountYes>0){
                                percent = 1;
                            }

                            mTvPercent.setText(percent+"%");
                        }

                        chageTime(resultBean.getLasttime());
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("标的详情失败 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }

    /**
     * 计算器
     */
    private void showCalculatorDialog() {

        if(resultBean == null) {
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
                tvExpectedReturn.setText(caculate(etAmt.getText().toString()));
            }
        });
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
                        myMoney = resultBean.getUse_money();
                        if(isBuyAll){
                            double myBalance = Double.valueOf(resultBean.getUse_money());
                            double surplus = Double.valueOf(BidDetailActivity.this.resultBean.getSurplus());
//                            if(myBalance>=surplus){
                                gotoBidConfirm(BidDetailActivity.this.resultBean.getSurplus(), caculate(BidDetailActivity.this.resultBean.getSurplus()));
//                            }
//                            else{
////                                CommonToast.showUnCancelableDialog(mContext, "最少投资金额为"+BidDetailActivity.this.resultBean.getLowest_account()+"元！");
//                                CommonToast.showUnCancelableDialog(mContext, "最少投资金额为"+BidDetailActivity.this.resultBean.getLowest_account()+"元！");
//                            }
                        }
                        else{
                            showBidDialog(resultBean.getUse_money());
                        }
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("账户信息失败 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }

    private String caculate(String money) {

        if (StringUtils.isBlank(money)) {
//            textView.setText("0.00");
            return "0.00";
        }

        String expectedReturn = "0.00";
        double amt = Double.valueOf(money);
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
                expectedReturn = String.valueOf(amt * (apr/1200/days) * term);
                break;
        }
//        textView.setText(StringUtils.formatMoney(expectedReturn));
        return StringUtils.formatMoney(expectedReturn);
    }

    /**
     * 输入密码框
     */

    private void showBidPwdDialog() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.bid_pwd_dialog, null);
        ViewUtils.inject(this, view);

        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);
        Button btnBid = (Button) view.findViewById(R.id.btn_bid_pwd_confirm);
        final InputAmountEditText etBIdAmt = (InputAmountEditText) view.findViewById(R.id.et_bid_pwd);


        final ActionSheet dialog = new ActionSheet(mContext, ActionSheet.GRAVITY_BOTTOM);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String text = etBIdAmt.getText().toString().trim();
                if(text.length()==0){
                    ToastUtil.showToast(BidDetailActivity.this, "请输入密码标密码");
                }
                else{
                    bidPwd = text;
//                    showBidDialog(balance);
                    getAccountInfo();
                }
            }
        });

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                imm.showSoftInput(etBIdAmt, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);
    }

    /**
     * 投标
     */
    private void showBidDialog(final String balance) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = inflater.inflate(R.layout.bid_dialog, null);

        ViewUtils.inject(this, view);

        TextView ivClose = (TextView) view.findViewById(R.id.iv_close);
        Button btnBid = (Button) view.findViewById(R.id.btn_bid);
        TextView tvBalance = (TextView) view.findViewById(R.id.tv_balance);
        TextView tvBidApr = (TextView) view.findViewById(R.id.tv_bid_apr);
        //剩余可投jine
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
        new KeyboardUtil(mContext, view, etBIdAmt,0).showKeyboard();

        tvBidApr.setText(resultBean.getApr()+"%");
        tvBalance.setText(balance + "元");
        tvMaxBid.setText(resultBean.getSurplus() + "元");

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
                double surplus = Double.valueOf(resultBean.getSurplus());
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
                loadingDialog.show();
                if(checkInfo(etBIdAmt, balance)) {
                    gotoBidConfirm(etBIdAmt.getText().toString(), tvExpectedReturn.getText().toString());
                }else {
                    loadingDialog.dismiss();
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
                tvExpectedReturn.setText(caculate(etBIdAmt.getText().toString())+"元");
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

    private void gotoBidConfirm(String money, String earn){
        loadingDialog.dismiss();
        Intent intent = new Intent(BidDetailActivity.this, BidConfirmActivity.class);
        intent.putExtra("money", money);
        intent.putExtra("earn", earn);
        intent.putExtra("bid_id",mBidId);

        intent.putExtra("name", bidName);
        intent.putExtra("is_day",resultBean.getIsday());
        switch (resultBean.getIsday()) {
            case "0":
                intent.putExtra("time",resultBean.getTime_limit());
                break;
            case "1":
                intent.putExtra("time",resultBean.getTime_limit_day());
                break;
        }
        intent.putExtra("apr",resultBean.getApr());
        intent.putExtra("repay_type",resultBean.getRepaytype());
        intent.putExtra("pwd", bidPwd);

        intent.putExtra("myMoney", myMoney);

        BidDetailActivity.this.startActivity(intent);
    }

    private boolean checkInfo(InputAmountEditText etBIdAmt, String balance) {
        if (StringUtils.isBlank(etBIdAmt.getText().toString().trim())){
            CommonToast.showHintDialog(mContext, "请输入金额");
            return false;
        }

        double surplus = Double.valueOf(resultBean.getSurplus());
        double lowestBid = Double.valueOf(resultBean.getLowest_account());
        double myEnter = Double.valueOf(etBIdAmt.getText().toString());
        double mostBid=Double.valueOf(resultBean.getMost_account());

        if(surplus >= lowestBid){
            if (myEnter < lowestBid||isBuyAll) {
                CommonToast.makeCustomText(mContext, "最小投资金额为"+lowestBid+"元！");
                return false;
            }
        }
        if (surplus <myEnter){
            CommonToast.makeCustomText(mContext, "剩余可投金额为"+surplus+"元！");
            return false;
        }
        if (myEnter>mostBid){
            CommonToast.makeCustomText(mContext, "最大投资金额为"+mostBid+"元！");
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

    @OnClick({R.id.btn_left, R.id.iv_calculator, R.id.tv_bid, R.id.ithb_bid_record, R.id.ithb_bid_reward, R.id.ithb_project_detail,R.id.iv_help,R.id.iv_que})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.iv_calculator:
                showCalculatorDialog();
                break;
            case R.id.tv_bid:
                //立即投标
                if(UserData.getInstance().isLogin()) {  //先判断登陆  再实名  最后测评
                    if (StringUtils.isNotBlank(UserData.getInstance().getRealName())) {
                        if (UserData.getInstance().getRiskWarning()) {
                            if (resultBean.getPwd().equals("1")) {
                                showBidPwdDialog();
                            } else {
                                getAccountInfo();
                            }
                        }else {
                            showToTestDialog();
                        }
                    } else {
                        CommonToast.showShiMingDialog(mContext);
                    }

                }else {
                    if (UserData.getInstance().getPhoneNumber().equals("")) {
                        Intent intent = new Intent(mContext, LoginActivityCheck.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.putExtra("phone_num", UserData.getInstance().getPhoneNumber());
                        startActivity(intent);
                    }
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
                    Map<String, String> datas = new HashMap<String, String>();
                    datas.put("bid",mBidId);
                    MobclickAgent.onEventValue(this, "kick_bid_detail", datas, 2);

                    UrlUtil.showHtmlPage(mContext,"项目详情", RequestURL.PROJECT_DETAIL_URL + resultBean.getId(),true);
                }
                break;
            case R.id.iv_help:
                Intent intent=new Intent(mContext, ContactUsActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_que:
                int popupWidth = popView.getMeasuredWidth();    //  获取测量后的宽度
                int popupHeight = popView.getMeasuredHeight();  //获取测量后的高度
                int[] location = new int[2];
                iv_que.getLocationOnScreen(location);

                popupWindow.showAtLocation(iv_que, Gravity.NO_GRAVITY, location[0]+10, location[1] - popupHeight-20);

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
    private  int day=0;
    private int hour=0;
    private int mint=0;
    private  int sed =0;
    private void chageTime(int _ms){
        day=_ms/86400;
        hour=(_ms-day*86400)/3600;
        mint=(_ms-day*86400-hour*3600)/60;
        sed=_ms-day*86400-hour*3600-mint*60;
        if (!isOver) {
            startTime();
        }
            timeplus(sed);
    }

    private Timer timer;
    private TimerTask timerTask;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            startTime();
            timeplus(msg.arg1);
        };
    };

    private void timeplus(int arg1) {
        if (arg1>59){
            sed=0;
            mint++;
        }
        if (mint>59){
            mint=0;
            hour++;
        }
        if (hour>23){
            hour=0;
            day++;
        }
        String hourStr=String.valueOf(hour);
        if(hour<10){
            hourStr="0"+hourStr;
        }
        String mintStr=String.valueOf(mint);
        if(mint<10){
            mintStr="0"+mintStr;
        }
        String sedStr=String.valueOf(sed);
        if(sed<10){
            sedStr="0"+sedStr;
        }
        //TODO 更新UI
        tv_bid_time.setText(day+"天"+hourStr+"时"+mintStr+"分"+sedStr+"秒");
    }

    /**
     * 开始自动加时
     */
    private void startTime() {
        if(timer==null){
            timer = new Timer();
        }

        timerTask = new TimerTask() {

            @Override
            public void run() {
                sed++;//自动加1
                Message message = Message.obtain();
                message.arg1=sed;
                mHandler.sendMessage(message);//发送消息
            }
        };
        timer.schedule(timerTask, 1000);//1000ms执行一次
    }
    /**
     * 停止自动加时
     */
    private void stopTime() {
        if(timer!=null) {
            timer.cancel();
        }
        timer=null;

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTime();
    }
}
