package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.timer.TimerHandler;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.ActionBar;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.PwdEditText;

public class InputVerifyCodeActivity extends BaseActivity implements PwdEditText.OnInputFinishListener{

    @ViewInject(R.id.ab_header)
    private ActionBar mActionBar;
    @ViewInject(R.id.et_pwd)
    private PwdEditText mEtPwd;
    @ViewInject(R.id.tv_des)
    private TextView mTvDes;
    @ViewInject(R.id.tv_get_verify_code)
    private TextView mTvReGet;

    private Context mContext;

    private String mFlag;
    private String mPhoneNum;
    public String mCheckUrl;
    public String mGetUrl;
    private TimerHandler mTimerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw_second);

        ViewUtils.inject(this);

        mContext = this;

        mEtPwd.setOnInputFinishListener(this);
        getIntentData();
        init();
    }

    private void getIntentData() {
        mFlag = getIntent().getStringExtra("flag");
        mPhoneNum = getIntent().getStringExtra("phone_num");

        mTvDes.setText(getString(R.string.input_verify_code_des, mPhoneNum));

        switch (mFlag){
            case Global.FLAG_REGISTER:
                mActionBar.setTitle("注册");
                mCheckUrl = RequestURL.VALIDATE_REG_VERIFY_URL;
                mGetUrl = RequestURL.REG_VERIFY_URL;
                break;
            case Global.FLAG_FORGET:
                mActionBar.setTitle("找回密码");
                mCheckUrl = RequestURL.VALIDATE_PWD_VERIFY_URL;
                mGetUrl = RequestURL.PWD_VERIFY_URL;
                break;
            case Global.FLAG_FORGET_TRANS:
                mActionBar.setTitle("找回支付密码");
                mCheckUrl = RequestURL.VALIDATE_TRANS_PWD_VERIFY_URL;
                mGetUrl = RequestURL.TRANS_PWD_VERIFY_URL;
                break;
        }
    }

    private void init() {
        mTimerHandler = new TimerHandler(60);
        mTimerHandler.setITimerListener(mITimerListener);
        mTimerHandler.startTask();
    }

//    public void getVerifyCode(String phone, String url) {
//
//        SenderResultModel resultModel = ParamsManager.senderGetVerifyCode(phone, url);
//
//        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {
//
//            @Override
//            public void onSuccess(String responeJson) {
//                LogUtil.i("获取验证码 " + responeJson);
//                mTimerHandler.setITimerListener(mITimerListener);
//                mTimerHandler.startTask();
//            }
//
//            @Override
//            public void onFailure(ResponseEntity entity) {
//                LogUtil.i("获取验证码 " + entity.errorInfo);
//                CommonToast.showHintDialog(mContext, entity.errorInfo);
//            }
//
//        }, this);
//    }

    public void validateCode(String phone, String code, String url) {

        SenderResultModel resultModel = null;

        switch (mFlag){
            case Global.FLAG_REGISTER:
                resultModel = ParamsManager.senderRegValidateCode(phone, code, url);
                break;
            case Global.FLAG_FORGET:
                resultModel = ParamsManager.senderPwdValidateCode(phone, code, url);
                break;
            case Global.FLAG_FORGET_TRANS:
                resultModel = ParamsManager.senderPwdValidateCode(phone, code, url);
                break;
        }

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("验证验证码 " + responeJson);

                switch (mFlag){
                    case Global.FLAG_REGISTER:
                        handleReg();
                        break;
                    case Global.FLAG_FORGET:
                        handleForget();
                        break;
                    case Global.FLAG_FORGET_TRANS:
                        handleForgetTrans();
                        break;
                }

            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("验证验证码失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, this);
    }

    private void handleReg() {
        Intent regIntent = new Intent(mContext, RegisterActivity.class);
        regIntent.putExtra("phone_num", mPhoneNum);
        startActivity(regIntent);

    }

    private void handleForget() {
        Intent forgetIntent = new Intent(mContext, ForgetPswThirdActivity.class);
        forgetIntent.putExtra("phone_num", mPhoneNum);
        startActivity(forgetIntent);

    }

    private void handleForgetTrans() {
        Intent forgetTransIntent = new Intent(mContext, FindPayPswActivity.class);
        startActivity(forgetTransIntent);

    }

    @OnClick({R.id.btn_left, R.id.tv_get_verify_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.tv_get_verify_code:
//                getVerifyCode(mPhoneNum, mGetUrl);
                break;
        }
    }

    TimerHandler.ITimerListener mITimerListener = new TimerHandler.ITimerListener() {
        @Override
        public void startTimer() {
            mTvReGet.setEnabled(false);
        }

        @Override
        public void timing(int second) {
            mTvReGet.setEnabled(false);
            mTvReGet.setText("重新获取（" + second + "s）");
        }

        @Override
        public void finishTimer() {
            mTvReGet.setText("重新获取");
            mTvReGet.setEnabled(true);
        }
    };

    @Override
    public void onInputFinish(String code) {
        validateCode(mPhoneNum, code, mCheckUrl);
    }
}
