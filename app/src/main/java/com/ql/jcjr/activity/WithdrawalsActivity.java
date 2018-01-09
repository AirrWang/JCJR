package com.ql.jcjr.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.CashServiceEntity;
import com.ql.jcjr.entity.CheckBankEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.timer.TimerHandler;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.ActionSheet;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.InputAmountEditText;
import com.ql.jcjr.view.PwdEditText;

import java.math.BigDecimal;
import java.util.List;


public class WithdrawalsActivity extends BaseActivity {

    private static final int CODE_BIND = 0;

    @ViewInject(R.id.ll_bank_info)
    private LinearLayout mLlBankInfo;

    @ViewInject(R.id.tv_bind_card)
    private TextView mTvBindCard;

    @ViewInject(R.id.et_amt)
    private InputAmountEditText mEtAmt;
    @ViewInject(R.id.tv_bank_name)
    private TextView mTvAcctNum;
    @ViewInject(R.id.tv_bank_info)
    private TextView mTvQuota;
    @ViewInject(R.id.civ_icon)
    private ImageView mIvBankLogo;
    @ViewInject(R.id.tv_balance)
    private TextView mTvBalance;

    //免费次数
    @ViewInject(R.id.tv_last_free_time)
    private TextView mTvLastFree;
    //手续费
    @ViewInject(R.id.tv_cash_service_tip)
    private TextView mTvCashServiceTip;
    //说明
    @ViewInject(R.id.tv_tip)
    private TextView mTvTip;

    @ViewInject(R.id.btn_withdrawals)
    private Button mBtnWithdrawals;

    private Context mContext;
    private String mAvailableBalance;
    private TimerHandler mTimerHandler;

    private boolean isJumpToRecharge = false;

    private int cashServiceType = 1;
    private int cashServiceCost = 0;

    private int totalMoney = 100000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals);

        ViewUtils.inject(this);
        mContext = this;

        init();
        getServiceCash();
    }

    //获取手续费数据
    private void getServiceCash() {
        SenderResultModel resultModel = ParamsManager.senderServiceCash();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("手续费详情 " + responeJson);
                        CashServiceEntity entity = GsonParser.getParsedObj(responeJson, CashServiceEntity.class);
//                        LogUtil.i("手续费详情 " + entity.getResult().getRemind().toString());

                        String freeTip = "剩余" + entity.getResult().getLast_num() + "次免费提现";
//                        int colorId = getResources().getColor(R.color.c_f99903);
//                        SpannableString ss = StringUtils.getSpannableString(freeTip, colorId, 7, freeTip.length() - 1);
                        mTvLastFree.setText(freeTip);

                        cashServiceType = entity.getResult().getType();
                        cashServiceCost = entity.getResult().getFee();

                        List<String> tips = entity.getResult().getRemind();
                        StringBuffer sb = new StringBuffer();
                        for (int i=0;i<tips.size();i++){
                            sb.append(tips.get(i));
                            if(i<tips.size()-1){
                                sb.append("\n");
                            }
                        }
                        mTvTip.setText(sb.toString());

                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("手续费详情失败 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }

    private void init() {
        mTimerHandler = new TimerHandler(60);
        //设置提款金额
        mAvailableBalance = getIntent().getStringExtra("account_balance");
//        String result = "可用余额 " + mAvailableBalance + " 元";
//        int colorId = getResources().getColor(R.color.c_f99903);
//        SpannableString ss = StringUtils.getSpannableString(result, colorId, 5, result.length() - 1);
        mTvBalance.setText(mAvailableBalance);
        //设置提款人
//        mTvRealName.setText(UserData.getInstance().getRealName());
        //设置提款账户
        mTvBindCard.setVisibility(View.VISIBLE);
        mLlBankInfo.setVisibility(View.GONE);
        mBtnWithdrawals.setEnabled(false);
//        getCashInfo();
        checkBank();

        mEtAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mEtAmt.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.size_xl));
//                    LogUtil.i("Editable " + Float.parseFloat(s.toString()));
                        float getCash = Float.parseFloat(s.toString());
                        if(getCash<100){
                            mTvCashServiceTip.setText("提现金额不能小于100元");
                        }
                        else{
                            int colorId = getResources().getColor(R.color.btn_main);
                            String tip = null;
                            String getTip = null;
                            if(cashServiceType == 1){
                                getTip = "到账："+(getCash-cashServiceCost)+"元，";
                                tip = getTip +"所需手续费"+cashServiceCost+"元";
                                SpannableString ss = StringUtils.getSpannableString(tip, colorId, getTip.length(), tip.length());

                                mTvCashServiceTip.setText(ss);
                            }
                            else if(cashServiceType == 2){
                                getTip = "到账："+(getCash-getCash*cashServiceCost/100)+"元，";
                                tip = getTip +"所需手续费"+getCash*cashServiceCost/100+"元";
                                SpannableString ss = StringUtils.getSpannableString(tip, colorId, getTip.length(), tip.length());

                                mTvCashServiceTip.setText(ss);
                            }
                        }
                } else {
                    mEtAmt.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            getResources().getDimension(R.dimen.size_m));
                }
            }
        });
    }

    private void checkBank() {
        SenderResultModel resultModel = ParamsManager.senderCheckBank();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("是否绑定银行卡 " + responeJson);
                        CheckBankEntity entity = GsonParser.getParsedObj(responeJson, CheckBankEntity.class);
                        CheckBankEntity.ResultBean resultBean = entity.getResult();
                        String status = resultBean.getStatus();
                        switch (status) {
                            case Global.STATUS_PASS:
                                mLlBankInfo.setVisibility(View.VISIBLE);
                                mTvBindCard.setVisibility(View.GONE);
                                mBtnWithdrawals.setEnabled(true);
                                mBtnWithdrawals.setBackgroundResource(R.drawable.login_button_selector);
                                GlideUtil.displayPic(mContext,resultBean.getImgUrl(), R.drawable.ic_bank_logo, mIvBankLogo);

                                String lastFour;
                                String cardNum = resultBean.getBankno();
                                if (cardNum.length() > 4) {
                                    lastFour = cardNum.substring(cardNum.length() - 4,
                                            cardNum.length());
                                } else {
                                    lastFour = cardNum;
                                }
                                mTvAcctNum.setText(resultBean.getBankname() + " (" + lastFour + ")");
                                mTvQuota.setText("单笔限额" + resultBean.getTotalMoney() + "元，单日无限额，单月无限额");

                                totalMoney = resultBean.getTotalMoney();
                                break;

                            case Global.STATUS_UN_PASS:
                                mLlBankInfo.setVisibility(View.GONE);
                                mTvBindCard.setVisibility(View.VISIBLE);
                                mBtnWithdrawals.setEnabled(false);
                                mBtnWithdrawals.setBackgroundResource(R.drawable.btn_bg_enable);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("是否绑定银行卡 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CODE_BIND:
                checkBank();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.btn_left, R.id.btn_withdrawals, R.id.tv_bind_card, R.id.tv_get_all})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_withdrawals:
                if (checkInfo()) {
//                    showPwdDialog();
                    getSmsCode();
                }
                break;

            case R.id.tv_get_all:
                //提取全部
                if(mAvailableBalance.equals("0")|| mAvailableBalance.equals("0.0") || mAvailableBalance.equals("0.00")){
                    CommonToast.makeText("提现金额不足");
                }
                else{
                    mEtAmt.setText(mAvailableBalance);
                    mEtAmt.setSelection(mAvailableBalance.length());
                }
                break;

            case R.id.tv_bind_card:
                Intent intent = new Intent(mContext, BindBankCardActivity.class);
                startActivityForResult(intent, CODE_BIND);
                break;
        }
    }

    private boolean checkInfo() {
        if (StringUtils.isBlank(mTvAcctNum.getText().toString())) {
            CommonToast.showHintDialog(mContext, "请先绑定银行卡");
            return false;
        }

        if (StringUtils.isBlank(mEtAmt.getText().toString().trim())) {
            CommonToast.showHintDialog(mContext, "请输入提现金额");
            return false;
        }

        double input = Double.parseDouble(mEtAmt.getText().toString());
        double limtiAmt = 100;
        BigDecimal data1 = new BigDecimal(input);
        BigDecimal data2 = new BigDecimal(limtiAmt);
        if(data1.compareTo(data2) == -1){
            CommonToast.showHintDialog(mContext,"提现金额需大于100元！");
            return false;
        }
        if(input > totalMoney){
            CommonToast.showHintDialog(mContext,"单笔提现金额上限"+totalMoney+"元！");
            return false;
        }
        if(Double.parseDouble(mAvailableBalance) < input){
            CommonToast.showHintDialog(mContext,"可用余额不足！");
            isJumpToRecharge = true;
            return false;
        }
//        BigDecimal data3 = new BigDecimal(mAvailableBalance);
//        if(data1.compareTo(data3) == 1){
//            CommonToast.showHintDialog(mContext,"可用余额不足！");
//            isJumpToRecharge = true;
//            return false;
//        }
        return true;
    }

    private void getSmsCode() {
        SenderResultModel resultModel = ParamsManager.sendeCashSms();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("提现获取验证码 " + responeJson);
                        showSmsCodeDialog();
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("提现获取验证码 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }

    /**
     * 输入验证码框
     */
    private void showSmsCodeDialog() {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.input_sms_dialog, null);
        PwdEditText mEtPwd = (PwdEditText) view.findViewById(R.id.et_pwd);

        TextView tvAmt = (TextView) view.findViewById(R.id.tv_amt);
        final TextView tvReGet = (TextView) view.findViewById(R.id.tv_re_get);
        String amt = mEtAmt.getText().toString().trim();
        String formatAmt = StringUtils.formatMoney(Double.valueOf(amt));
        tvAmt.setText("¥ " + formatAmt);

        final ActionSheet dialog = new ActionSheet(mContext, ActionSheet.GRAVITY_CENTER);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        tvReGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
                getReGetSmsCode(tvReGet);
            }
        });

        mEtPwd.setOnInputFinishListener(new PwdEditText.OnInputFinishListener() {
            @Override
            public void onInputFinish(String password) {
                dialog.dismiss();
                cash(mEtAmt.getText().toString().trim(), password);
            }
        });


        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mTimerHandler.stopTask();
            }
        });

        timerListener(tvReGet);

    }

    private void timerListener(final TextView textView) {
        TimerHandler.ITimerListener mITimerListener = new TimerHandler.ITimerListener() {
            @Override
            public void startTimer() {
                textView.setEnabled(false);
            }

            @Override
            public void timing(int second) {
                textView.setEnabled(false);
                textView.setText("重新获取（" + second + "s）");
            }

            @Override
            public void finishTimer() {
                textView.setText("重新获取");
                textView.setEnabled(true);
            }
        };

        mTimerHandler.setITimerListener(mITimerListener);
        mTimerHandler.startTask();
    }


    private void getReGetSmsCode(final TextView textView) {
        SenderResultModel resultModel = ParamsManager.sendeCashSms();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("提现获取验证码 " + responeJson);
                        timerListener(textView);
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("提现获取验证码 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }

    /**
     * 提现
     */
    private void cash(final String money, String paypassword) {
        SenderResultModel resultModel = ParamsManager.senderSmsCash(money, paypassword);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("提现 " + responeJson);
                        JcbApplication.needReloadMyInfo = true;
//                CommonToast.showHintDialog(mContext, "您的提现已经提交，我们将尽快审核打款");
                        Intent intent = new Intent(mContext, WithdrawalsResultActivity.class);
                        intent.putExtra("ab_title", "提现成功");
                        intent.putExtra("is_success", true);
                        intent.putExtra("result_text", "您的提现已经提交，我们将尽快审核打款");
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("提现 " + entity.errorInfo);
                        Intent intent = new Intent(mContext, WithdrawalsResultActivity.class);
                        intent.putExtra("ab_title", "提现失败");
                        intent.putExtra("is_success", false);
                        intent.putExtra("result_text", entity.errorInfo);
                        startActivity(intent);
                        finish();
                    }

                }, mContext);
    }

    /**
     * 输入密码框
     */
    private void showPwdDialog() {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.input_pwd_dialog, null);
        PwdEditText mEtPwd = (PwdEditText) view.findViewById(R.id.et_pwd);

        TextView tvAmt = (TextView) view.findViewById(R.id.tv_amt);
        String amt = mEtAmt.getText().toString().trim();
        String formatAmt = StringUtils.formatMoney(Double.valueOf(amt));
        tvAmt.setText("¥ " + formatAmt);

        final ActionSheet dialog = new ActionSheet(mContext, ActionSheet.GRAVITY_CENTER);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        mEtPwd.setOnInputFinishListener(new PwdEditText.OnInputFinishListener() {
            @Override
            public void onInputFinish(String password) {
                dialog.dismiss();
                cash(mEtAmt.getText().toString().trim(), password);
            }
        });
    }
}
