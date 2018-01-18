package com.ql.jcjr.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.timer.TimerHandler;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.ActionBar;
import com.ql.jcjr.view.CancelEditTextWhite;
import com.ql.jcjr.view.CommonToast;

/**
 * Created by Airr on 2018/1/17.
 */

public class SetPayPwdActivity extends BaseActivity{
    @ViewInject(R.id.tv_set_paypsw)
    TextView mySetPayPSW;
    @ViewInject(R.id.ab_header)
    ActionBar myActionbar;
    @ViewInject(R.id.btn_set_pay_pwd)
    Button myButton;
    @ViewInject(R.id.et_pay_psw)
    CancelEditTextWhite myEtpaypwd;
    @ViewInject(R.id.et_sms_code)
    private CancelEditTextWhite mEtSmsCode;

    private String mPhoneNum;
    private boolean isPswShow = false;
    private Context mContext;
    private Boolean isFirst;
    private TimerHandler mTimerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_pay);
        ViewUtils.inject(this);
        mContext=this;
        init();

        getGetVerifyCode(mPhoneNum, RequestURL.TRANS_PWD_VERIFY_URL);
        initEdit();
    }

    private void initEdit() {
        myEtpaypwd.getCancelEditText().setTransformationMethod(
                PasswordTransformationMethod.getInstance());

        myEtpaypwd.setOnCancelEditEventListener(new CancelEditTextWhite.CancelEditEventListener() {
            @Override
            public void onCancelFocusChange(boolean hasFocus) {
            }

            @Override
            public void onClickRightExtraTextView() {
            }

            @Override
            public void onClickRightExtraImageView() {
                if (!isPswShow) {
                    isPswShow = true;
                    myEtpaypwd.getCancelEditText()
                            .setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    myEtpaypwd.setRightExtraImageIcon(R.drawable.show_psw_pressed_1);
                } else {
                    isPswShow = false;
                    myEtpaypwd.getCancelEditText()
                            .setTransformationMethod(PasswordTransformationMethod.getInstance());
                    myEtpaypwd.setRightExtraImageIcon(R.drawable.show_psw_normal_1);
                }
                myEtpaypwd.getCancelEditText().setSelection(
                        myEtpaypwd.getEditTextContent().length());
            }
        });

        mEtSmsCode.setOnCancelEditEventListener(new CancelEditTextWhite.CancelEditEventListener() {
            @Override
            public void onCancelFocusChange(boolean hasFocus) {
            }

            @Override
            public void onClickRightExtraTextView() {
                getGetVerifyCode(mPhoneNum, RequestURL.TRANS_PWD_VERIFY_URL);
            }

            @Override
            public void onClickRightExtraImageView() {
            }
        });
    }

    private void init() {
        switch (UserData.getInstance().getIsSetPay()) {
            case "0":
                isFirst=true;
                break;
            case "1":
                isFirst=false;
                myActionbar.setTitle("修改交易密码");
                myButton.setText("确认修改");
                break;
        }
        mPhoneNum = UserData.getInstance().getPhoneNumber();
        mySetPayPSW.setText("短信验证码已发送至"+ mPhoneNum +"，请查收");

        mTimerHandler = new TimerHandler(60);
        mTimerHandler.setITimerListener(mITimerListener);
    }

    TimerHandler.ITimerListener mITimerListener = new TimerHandler.ITimerListener() {
        @Override
        public void startTimer() {
            mEtSmsCode.setRightExtraTextViewEnable(false);
        }

        @Override
        public void timing(int second) {
            mEtSmsCode.setRightExtraTextViewEnable(false);
            mEtSmsCode.setRightExtraTextViewContent("重新获取（" + second + "s）");
        }

        @Override
        public void finishTimer() {
            mEtSmsCode.setRightExtraTextViewContent("重新获取");
            mEtSmsCode.setRightExtraTextViewEnable(true);
        }
    };
    public void getGetVerifyCode(String phone, String url) {

        SenderResultModel resultModel;
        if (isFirst){
            resultModel = ParamsManager.senderGetVerifyCodeFirst(phone, url,"1");
        }else {
            resultModel = ParamsManager.senderGetVerifyCode(phone, url);
        }

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("获取验证码 " + responeJson);

                mTimerHandler.setITimerListener(mITimerListener);
                mTimerHandler.startTask();

            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("获取验证码 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, this);
    }
    @OnClick({R.id.btn_left,R.id.btn_set_pay_pwd})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_set_pay_pwd:
                if (checkInfo()){
                    validateCode(mPhoneNum, mEtSmsCode.getEditTextContent().toString().trim(), RequestURL.VALIDATE_TRANS_PWD_VERIFY_URL);
                }
                break;
        }
    }

    private void validateCode(String mPhoneNum, String code, String validateTransPwdVerifyUrl) {
        SenderResultModel resultModel= ParamsManager.senderPwdValidateCode(mPhoneNum,code, validateTransPwdVerifyUrl);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("验证验证码 " + responeJson);
                setPayPwd(myEtpaypwd.getEditTextContent().toString().trim());

            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("验证验证码 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, this);
    }
    private void setPayPwd(String pwd) {
        SenderResultModel resultModel = ParamsManager.sendeFindPayPsw(pwd);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("设置支付密码 " + responeJson);
                CommonToast.makeCustomText(mContext, "支付密码设置成功");
                UserData.getInstance().setIsSetPay("1");
                finish();
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("设置支付密码 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);

    }
    private boolean checkInfo() {
        if(StringUtils.isBlank(mEtSmsCode.getEditTextContent().toString())) {
            CommonToast.showHintDialog(mContext,"请输入短信验证码");
            return false;
        }

        if(StringUtils.isBlank(myEtpaypwd.getEditTextContent().toString())||myEtpaypwd.getEditTextContent().toString().trim().length()!=6) {
            CommonToast.showHintDialog(mContext,"请正确输入交易密码");
            return false;
        }
        return true;
    }
}
