package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.AppConfig;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.CheckBankEntity;
import com.ql.jcjr.entity.RechargeEntity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.utils.KeyboardUtil;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.UrlUtil;
import com.ql.jcjr.view.ActionBar;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.InputAmountEditText;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;

public class RechargeActivity extends BaseActivity {

    @ViewInject(R.id.ll_charge_container)
    private LinearLayout mLlChargeContainer;
    @ViewInject(R.id.et_amt)
    private InputAmountEditText mEtAmt;

    @ViewInject(R.id.ll_bank_info)
    private LinearLayout mLlBankInfo;
    @ViewInject(R.id.tv_bank_info)
    private TextView mTvQuota;
    @ViewInject(R.id.civ_icon)
    private ImageView mIvBankLogo;
    @ViewInject(R.id.tv_bank_name)
    private TextView mTvBankName;

    @ViewInject(R.id.tv_bind)
    private TextView mTvBind;
    @ViewInject(R.id.btn_recharge)
    private Button mBtnRecharge;
    @ViewInject(R.id.ab_header)
    private ActionBar ab_header;
    @ViewInject(R.id.tv_tips)
    private TextView mTvTips;

    @ViewInject(R.id.iv_question)
    private ImageView iv_question;

    private Context mContext;
    private static final int CODE_BIND = 0;
    private CheckBankEntity.ResultBean resultBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        ViewUtils.inject(this);
        mContext = this;
        mBtnRecharge.setEnabled(false);

        checkBank();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mEtAmt.setShowSoftInputOnFocus(false);
        }else {
            mEtAmt.setInputType(InputType.TYPE_NULL);
        }

        mEtAmt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new KeyboardUtil(mContext, RechargeActivity.this, mEtAmt,0).showKeyboard();
                return false;
            }
        });
        mBtnRecharge.setBackgroundResource(R.drawable.btn_pressed_enable);
        mEtAmt.addTextChangedListener(new TextWatcher() {
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
                if (s.length() > 0) {
                    mEtAmt.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.size_xxxl));
                    float getCash = Float.parseFloat(s.toString());



                    if(getCash<100){
                        mTvTips.setVisibility(View.VISIBLE);
                        mTvTips.setText("单笔最低100元！");
                        mBtnRecharge.setEnabled(false);
                        mBtnRecharge.setBackgroundResource(R.drawable.btn_pressed_enable);
                    }else if (getCash>resultBean.getTotalMoney()){
                        mTvTips.setVisibility(View.VISIBLE);
                        mTvTips.setText("单笔最高"+resultBean.getTotalMoney()+"元！");
                        mBtnRecharge.setEnabled(false);
                        mBtnRecharge.setBackgroundResource(R.drawable.btn_pressed_enable);
                    } else{
                        mTvTips.setVisibility(View.GONE);
                        mBtnRecharge.setEnabled(true);
                        mBtnRecharge.setBackgroundResource(R.drawable.login_button_selector);
                    }
                } else {
                    mEtAmt.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            getResources().getDimension(R.dimen.size_m));
                    mTvTips.setVisibility(View.GONE);
                    mBtnRecharge.setEnabled(false);
                    mBtnRecharge.setBackgroundResource(R.drawable.btn_pressed_enable);
                }
            }
        });
        iv_question.setVisibility(View.VISIBLE);
    }

    private void checkBank() {
        SenderResultModel resultModel = ParamsManager.senderCheckBank();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("是否绑定银行卡 " + responeJson);
                        CheckBankEntity entity = GsonParser.getParsedObj(responeJson, CheckBankEntity.class);
                        resultBean = entity.getResult();
                        String status = resultBean.getStatus();
                        switch (status){
                            case Global.STATUS_PASS:
                                mTvBind.setVisibility(View.GONE);
                                mLlBankInfo.setVisibility(View.VISIBLE);
                                mLlChargeContainer.setVisibility(View.VISIBLE);
//                                mBtnRecharge.setEnabled(true);
//                                mBtnRecharge.setBackgroundResource(R.drawable.login_button_selector);
                                GlideUtil.displayPic(mContext, resultBean.getImgUrl(), R.drawable.ic_bank_logo, mIvBankLogo);

                                String lastFour;
                                String cardNum =  resultBean.getBankno();
                                if (cardNum.length() > 4){
                                    lastFour = cardNum.substring(cardNum.length() - 4, cardNum.length());
                                }else {
                                    lastFour = cardNum;
                                }
                                mTvBankName.setText(resultBean.getBankname()+" ("+lastFour+")");


                                int colorId = getResources().getColor(R.color.font_black);
                                String max=resultBean.getTotalMoney()+"";
                                String oneDay=resultBean.getOneday()+"";
                                if (max==null||oneDay==null){
                                    return;
                                }
                                String getTip = "单笔限额："+max+"元，";
                                SpannableString ss = StringUtils.getSpannableString(getTip, colorId, 5, max.length());

                                String tip = "每日限额："+oneDay+"元";
                                SpannableString sss = StringUtils.getSpannableString(tip, colorId, 5, oneDay.length());

                                mTvQuota.setText(ss+""+sss);
                                break;
                            case Global.STATUS_UN_PASS:
                                mTvBind.setVisibility(View.VISIBLE);
                                mLlBankInfo.setVisibility(View.GONE);
                                mLlChargeContainer.setVisibility(View.GONE);
                                mBtnRecharge.setEnabled(false);
                                mBtnRecharge.setBackgroundResource(R.drawable.btn_pressed_enable);
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

    private void recharge(String amt) {
        SenderResultModel resultModel = ParamsManager.sendeRecharge(amt);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("充值 " + responeJson);
                        RechargeEntity entity = GsonParser.getParsedObj(responeJson, RechargeEntity.class);
                        RechargeEntity.ResultBean resultBean = entity.getResult();
                        String ENCTP = "ENCTP=" + resultBean.getENCTP();
                        String VERSION = "VERSION=" + resultBean.getVERSION();
                        String MCHNTCD = "MCHNTCD=" + resultBean.getMCHNTCD();
                        String LOGOTP = "LOGOTP=" + resultBean.getLOGOTP();
                        String FM = null;
                        try {
                            FM = "FM=" + URLEncoder.encode(resultBean.getFM(), "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        String postData = ENCTP + "&" + VERSION + "&" + MCHNTCD+ "&" + LOGOTP+ "&" + FM;
                        Intent intent = new Intent(mContext, RechargePayActivity.class);
                        intent.putExtra("pay_url", resultBean.getFuioupayurl());
                        intent.putExtra("post_data", postData);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("充值 " + entity.errorInfo);
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

    private boolean checkInfo() {
        if(StringUtils.isBlank(mEtAmt.getText().toString())) {
            CommonToast.showHintDialog(mContext,"请输入金额！");
            return false;
        }

        double input = Double.parseDouble(mEtAmt.getText().toString());
        double limtiAmt = 100;
        BigDecimal data1 = new BigDecimal(input);
        BigDecimal data2 = new BigDecimal(limtiAmt);
        if(data1.compareTo(data2) == -1){
            CommonToast.showHintDialog(mContext,"最低充值金额100元！");
            return false;
        }
        return true;
    }

    @OnClick({R.id.btn_left, R.id.tv_bind, R.id.btn_recharge,R.id.iv_question,R.id.tv_bank_list})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.tv_bind:
                if (UserData.getInstance().getRealName().equals("")) {
                    CommonToast.showShiMingDialog(mContext);
                }else {
                    Intent intent = new Intent(mContext, BindBankCardActivity.class);
                    startActivityForResult(intent, CODE_BIND);
                }
                break;
            case R.id.btn_recharge:
                if(checkInfo()) {
                    recharge(mEtAmt.getText().toString());
                }
                break;
            case R.id.iv_question:
                UrlUtil.showHtmlPage(mContext,"常见问题", AppConfig.COMMON_PROBLEM_URL+"?id=3",true);

                break;
            case R.id.tv_bank_list:
                Intent intent = new Intent(this, BankListActivity.class);
                startActivityForResult(intent, 1);
                break;
        }
    }
}
