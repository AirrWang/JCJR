package com.ql.jcjr.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.AppConfig;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.entity.CashServiceEntity;
import com.ql.jcjr.entity.CheckBankEntity;
import com.ql.jcjr.entity.WithdrawalsEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.timer.TimerHandler;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.utils.KeyboardUtil;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.ToastUtil;
import com.ql.jcjr.utils.UrlUtil;
import com.ql.jcjr.view.ActionBar;
import com.ql.jcjr.view.ActionSheet;
import com.ql.jcjr.view.CommonDialog;
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
//    @ViewInject(R.id.tv_bank_info)
//    private TextView mTvQuota;
    @ViewInject(R.id.civ_icon)
    private ImageView mIvBankLogo;
    @ViewInject(R.id.tv_balance)
    private TextView mTvBalance;
    @ViewInject(R.id.ab_header)
    private ActionBar ab_header;

    @ViewInject(R.id.iv_question)
    private ImageView iv_question;

    @ViewInject(R.id.sl_withdrawals)
    private ScrollView sl_withdrawals;

    //免费次数
    @ViewInject(R.id.tv_last_free_time)
    private TextView mTvLastFree;

    @ViewInject(R.id.ll_jianpan)
    private View ll_jianpan;
//    //手续费
//    @ViewInject(R.id.tv_cash_service_tip)
//    private TextView mTvCashServiceTip;
    //说明
    @ViewInject(R.id.tv_tip)
    private TextView mTvTip;

    @ViewInject(R.id.btn_withdrawals)
    private Button mBtnWithdrawals;

    @ViewInject(R.id.tv_finish_bank)
    private TextView tv_finish_bank;

    @ViewInject(R.id.tv_free_intro)
    private TextView tv_free_intro;

    @ViewInject(R.id.tv_get_all)
    private TextView tv_get_all;

    @ViewInject(R.id.tv_1)
    private TextView tv_1;

    private Context mContext;
    private String mAvailableBalance;
    private TimerHandler mTimerHandler;

    private boolean isJumpToRecharge = false;

    private int cashServiceType = 1;
    private int cashServiceCost = 0;
    private float feeApr=0f;
    private float availableAmount=0f;
    private float feeTrob=0.00f;
    private float totalFee=0.00f;


//    private int totalMoney = 100000;
    private String freeTip;
    private int min;
    private int max;
    private String status;
    private PwdEditText mEtPwd;
    private List<String> tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawals);

        ViewUtils.inject(this);
        mContext = this;
        mBtnWithdrawals.setEnabled(false);
        mBtnWithdrawals.setBackgroundResource(R.drawable.btn_pressed_enable);
        iv_question.setVisibility(View.VISIBLE);


    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
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

                        freeTip = "本月剩余" + entity.getResult().getLast_num() + "次免费提现";
//                        int colorId = getResources().getColor(R.color.c_f99903);
//                        SpannableString ss = StringUtils.getSpannableString(freeTip, colorId, 7, freeTip.length() - 1);
                        mTvLastFree.setText(freeTip);

                        cashServiceType = entity.getResult().getType();
                        cashServiceCost = entity.getResult().getFee();

                        min = entity.getResult().getMin();
                        max = entity.getResult().getMax();

                        feeApr=entity.getResult().getFee_apr();
                        availableAmount=entity.getResult().getAvailable_amount();

                        tips = entity.getResult().getRemind();

                        List<String> cash_tips=entity.getResult().getCash_remind();
                        if (cash_tips==null||cash_tips.size()==0){
                            tv_1.setVisibility(View.GONE);
                        }else {
                            StringBuffer sb = new StringBuffer();
                            for (int i=0;i<cash_tips.size();i++){
                                sb.append(cash_tips.get(i));
                                if(i<cash_tips.size()-1){
                                    sb.append("\n");
                                }
                            }
                            mTvTip.setText(sb.toString());
                        }

                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("手续费详情失败 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }


    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mEtAmt.setShowSoftInputOnFocus(false);
        }else {
            mEtAmt.setInputType(InputType.TYPE_NULL);
        }

//        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//         final View view = inflater.inflate(R.layout.activity_withdrawals, null);
        mEtAmt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mEtAmt.setFocusable(true);
                new KeyboardUtil(mContext, WithdrawalsActivity.this, mEtAmt,1).showKeyboard();
                return false;
            }
        });

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
                tv_free_intro.setVisibility(View.GONE);
                if (s.length() > 0) {
                    mEtAmt.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.size_xxxl));

                    if (s.toString().equals("0")|| s.toString().equals("0.")){
                        mEtAmt.setText("");
                        return;
                    }
                    if(mAvailableBalance.equals("0")|| mAvailableBalance.equals("0.0") || mAvailableBalance.equals("0.00")){
                        mEtAmt.setText("");
                        CommonToast.makeText("提现金额不足");
                        return;
                    }
                    if (Float.valueOf(s.toString())>Float.valueOf(mAvailableBalance)){
                        mEtAmt.setText(mAvailableBalance);
                        mEtAmt.setSelection(mAvailableBalance.length());
                    }
//                    LogUtil.i("Editable " + Float.parseFloat(s.toString()));
                        float getCash = Float.parseFloat(mEtAmt.getText().toString().trim());

//                    int colorId = getResources().getColor(R.color.btn_main);
//                    String tip = null;
//                    String getTip = null;
//                    if(cashServiceType == 1){
//
//                        getTip = "到账："+(getCash-cashServiceCost)+"元，";
//                        if (getCash-cashServiceCost<0){
//                            getTip = "到账：0元，";
//                        }
//                        tip = getTip +"所需手续费"+cashServiceCost+"元";
//                        SpannableString ss = StringUtils.getSpannableString(tip, colorId, getTip.length(), tip.length());
//
//                        mTvCashServiceTip.setText(ss);
//                    }
//                    else if(cashServiceType == 2){
//                        getTip = "到账："+(getCash-getCash*cashServiceCost/100)+"元，";
//                        if (getCash-getCash*cashServiceCost/100<0){
//                            getTip = "到账：0元，";
//                        }
//                        tip = getTip +"所需手续费"+getCash*cashServiceCost/100+"元";
//                        SpannableString ss = StringUtils.getSpannableString(tip, colorId, getTip.length(), tip.length());
//
//                        mTvCashServiceTip.setText(ss);
//                    }
                        if(getCash<min){
                            mTvLastFree.setText("单笔最低"+min+"元！");
                            mTvLastFree.setTextColor(getResources().getColor(R.color.font_red));
                            mBtnWithdrawals.setEnabled(false);
                            mBtnWithdrawals.setBackgroundResource(R.drawable.btn_pressed_enable);
                        }else if (getCash>max){
                            mTvLastFree.setText("单笔最高"+max+"元！");
                            mTvLastFree.setTextColor(getResources().getColor(R.color.font_red));
                            mBtnWithdrawals.setEnabled(false);
                            mBtnWithdrawals.setBackgroundResource(R.drawable.btn_pressed_enable);
                        } else{
                            if (getCash>availableAmount){

                                feeTrob=(getCash-availableAmount)*feeApr;
                                BigDecimal bg = new BigDecimal(feeTrob);
                                feeTrob = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

                                totalFee=feeTrob+cashServiceCost;
                            }else {
                                totalFee=cashServiceCost;
                            }
                            tv_free_intro.setVisibility(View.VISIBLE);
                            mTvLastFree.setText("手续费"+totalFee+"元");
                            mTvLastFree.setTextColor(getResources().getColor(R.color.font_red));
                            mBtnWithdrawals.setEnabled(true);
                            mBtnWithdrawals.setBackgroundResource(R.drawable.login_button_selector);
                        }
                } else {
                    mEtAmt.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            getResources().getDimension(R.dimen.size_m));
                    mTvLastFree.setText(freeTip);
                    mBtnWithdrawals.setEnabled(false);
                    mBtnWithdrawals.setBackgroundResource(R.drawable.btn_pressed_enable);
                    mTvLastFree.setTextColor(getResources().getColor(R.color.font_grey));
//                    mTvCashServiceTip.setText("所需手续费0元");
                }
            }
        });

        if (mEtPwd!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mEtPwd.setShowSoftInputOnFocus(false);
            }else {
                mEtPwd.setInputType(InputType.TYPE_NULL);
            }
        }
    }

    private String isComlate;
    private void checkBank() {
        SenderResultModel resultModel = ParamsManager.senderCheckBank();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        getServiceCash();
                        LogUtil.i("是否绑定银行卡 " + responeJson);
                        CheckBankEntity entity = GsonParser.getParsedObj(responeJson, CheckBankEntity.class);
                        CheckBankEntity.ResultBean resultBean = entity.getResult();
                        status = resultBean.getStatus();
                        switch (status) {
                            case Global.STATUS_PASS:
                                mLlBankInfo.setVisibility(View.VISIBLE);
                                mTvBindCard.setVisibility(View.GONE);
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
                                isComlate=resultBean.getIscomplete();
                                if (isComlate.equals("1")){
                                    tv_finish_bank.setVisibility(View.GONE);
                                }else {
                                    tv_finish_bank.setVisibility(View.VISIBLE);
                                }

                                break;

                            case Global.STATUS_UN_PASS:
                                mLlBankInfo.setVisibility(View.GONE);
                                mTvBindCard.setVisibility(View.VISIBLE);
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

    @OnClick({R.id.btn_left, R.id.btn_withdrawals, R.id.tv_bind_card, R.id.tv_get_all, R.id.iv_question,R.id.tv_finish_bank,R.id.tv_free_intro})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_withdrawals:
                if (isComlate==null){
                    ToastUtil.showToast(mContext,"请先绑定银行卡");
                    return;
                }
                if (isComlate.equals("0")){
                    //TODO
                    CommonDialog.Builder builder = new CommonDialog.Builder(mContext);
                    builder.setTitle("完善开户行信息")
                            .setMessage("为了保障提现正常到账，请准确填写开户行信息")
                            .setPositiveButton("去完善", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent callIntent = new Intent(mContext,  BindBankCardActivity.class);
                                    mContext.startActivity(callIntent);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                    return;
                }
                if (checkInfo()) {
//                    showPwdDialog();
                    if (status.equals("1")) {
                        getSmsCode();
                    }else {
                        CommonToast.makeText("请绑定银行卡");
                    }
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
            case R.id.iv_question:
                UrlUtil.showHtmlPage(mContext,"常见问题", AppConfig.COMMON_PROBLEM_URL+"?id=2",true);
                break;
            case R.id.tv_finish_bank:
                Intent intent1 = new Intent(mContext, BindBankCardActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_free_intro:
                showToDialog();

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
//        if(input > totalMoney){
//            CommonToast.showHintDialog(mContext,"单笔提现金额上限"+totalMoney+"元！");
//            return false;
//        }
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

        final View view = inflater.inflate(R.layout.input_sms_dialog, null);
        mEtPwd = (PwdEditText) view.findViewById(R.id.et_pwd);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mEtPwd.setShowSoftInputOnFocus(false);
        }else {
            mEtPwd.setInputType(InputType.TYPE_NULL);
        }
        new KeyboardUtil(mContext, view, mEtPwd,66299).showKeyboard();

        TextView tvAmt = (TextView) view.findViewById(R.id.tv_amt);
        final TextView tvReGet = (TextView) view.findViewById(R.id.tv_re_get);
        String amt = mEtAmt.getText().toString().trim();
        String formatAmt = StringUtils.formatMoney(Double.valueOf(amt));
        tvAmt.setText("¥ " + formatAmt);
        ImageView tv_close= (ImageView) view.findViewById(R.id.tv_close);

        final ActionSheet dialog = new ActionSheet(mContext, ActionSheet.GRAVITY_BOTTOM);

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

//        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//        params.width = this.getWindowManager().getDefaultDisplay().getWidth();
//        params.height = this.getWindowManager().getDefaultDisplay().getHeight() ;
//        dialog.getWindow().setAttributes(params);

        tvReGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
                getReGetSmsCode(tvReGet);
            }
        });
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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
                mEtAmt.setFocusable(false);
//                try {
//                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(mEtAmt.getWindowToken(), 0);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
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
                textView.setText( second + "s");
                textView.setTextColor(getResources().getColor(R.color.text_hint_color));
            }

            @Override
            public void finishTimer() {
                textView.setText("重新获取");
                textView.setTextColor(getResources().getColor(R.color.btn_main));
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
                        WithdrawalsEntity entity = GsonParser.getParsedObj(responeJson, WithdrawalsEntity.class);
                        JcbApplication.needReloadMyInfo = true;
//                        Intent intent = new Intent(mContext, WithdrawalsResultActivity.class);
//                        intent.putExtra("fee",entity.getResult().getFee());
//                        intent.putExtra("time",entity.getResult().getTime());
//                        intent.putExtra("amount",mEtAmt.getText().toString().trim());
//                        startActivity(intent);
                        UrlUtil.showHtmlPage(mContext,"提现", RequestURL.BID_GETMONEY_URL+"&account="+entity.getResult().getAccount()+
                                "&fee="+entity.getResult().getFee() +"&credited="+entity.getResult().getCredited(),true);

                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("提现 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
//                        Intent intent = new Intent(mContext, WithdrawalsResultActivity.class);
//                        intent.putExtra("ab_title", "提现失败");
//                        intent.putExtra("is_success", false);
//                        intent.putExtra("result_text", entity.errorInfo);
//                        startActivity(intent);
//                        finish();
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

    private void showToDialog() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.withdrawal_fee_intro, null);
        ViewUtils.inject(this, view);

        TextView tv_fee= (TextView) view.findViewById(R.id.tv_fee);
        TextView tv_trofee= (TextView) view.findViewById(R.id.tv_trofee);
        TextView tv_total= (TextView) view.findViewById(R.id.tv_total);
        TextView tv_rule=(TextView)view.findViewById(R.id.tv_rule);
        tv_fee.setText(cashServiceCost+"元");
        tv_trofee.setText(feeTrob+"元");
        tv_total.setText(totalFee+"元");

        StringBuffer sb = new StringBuffer();
        for (int i=0;i<tips.size();i++){
            sb.append(tips.get(i));
            if(i<tips.size()-1){
                sb.append("\n");
            }
        }
        tv_rule.setText(sb.toString());

        final ImageView iv_dismiss= (ImageView) view.findViewById(R.id.iv_dismiss);

        final ActionSheet dialog = new ActionSheet(mContext, ActionSheet.GRAVITY_CENTER);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        iv_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            LogUtil.d("v:"+v);
            if (isShouldHideKeyboard(v, ev)) {
                new KeyboardUtil(mContext, WithdrawalsActivity.this, mEtAmt,1).hideKeyboard();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText) ) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            int[] y = {0, 0};
            ll_jianpan.getLocationInWindow(y);
            int left1 = y[0],
                    top1 = y[1],
                    bottom1 = top1 + ll_jianpan.getHeight(),
                    right1 = left1 + ll_jianpan.getWidth();
            tv_get_all.getLocationInWindow(y);
            int left2 = y[0],
                    top2 = y[1],
                    bottom2 = top2 + tv_get_all.getHeight(),
                    right2 = left2 + tv_get_all.getWidth();

            if (event.getX() > left1 && event.getX() < right1
                    && event.getY() > top1 && event.getY() < bottom1){
                return false;
            }
            if ((event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom)||(event.getX() > left1 && event.getX() < right1
                    && event.getY() > top1 && event.getY() < bottom1)||(event.getX() > left2 && event.getX() < right2
                    && event.getY() > top2 && event.getY() < bottom2)) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }


}
