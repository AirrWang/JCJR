package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.ActionBar;
import com.ql.jcjr.view.CancelEditText;
import com.ql.jcjr.view.CommonToast;

public class InputPhoneNumActivity extends BaseActivity {

    @ViewInject(R.id.ab_header)
    private ActionBar mActionBar;
    @ViewInject(R.id.cet_phone_num)
    private CancelEditText mCetPhone;

    private Context mContext;

    private String mFlag;
    public String mRequestUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw_first);

        ViewUtils.inject(this);

        mContext = this;
        getIntentData();
    }

    private void getIntentData() {
        mFlag = getIntent().getStringExtra("flag");

        switch (mFlag){
            case Global.FLAG_REGISTER:
                mActionBar.setTitle("注册");
                mRequestUrl = RequestURL.REG_VERIFY_URL;
                break;
            case Global.FLAG_FORGET:
                mActionBar.setTitle("找回密码");
                mRequestUrl = RequestURL.PWD_VERIFY_URL;
                break;
        }
    }

    private boolean checkInfo() {
        if(StringUtils.isBlank(mCetPhone.getEditTextContent())) {
            CommonToast.showHintDialog(mContext,"请输入手机号码");
            return false;
        }

        return true;
    }

//    public void getGetVerifyCode(String phone, String url) {
//
//        SenderResultModel resultModel = ParamsManager.senderGetVerifyCode(phone, url);
//
//        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {
//
//            @Override
//            public void onSuccess(String responeJson) {
//                LogUtil.i("获取验证码 " + responeJson);
//                VerificationCodeEntity mEntity = GsonParser.getParsedObj(responeJson, VerificationCodeEntity.class);
//                getGetVerifyCodesuccess();
//
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

    private void getGetVerifyCodesuccess() {
        switch (mFlag){
            case Global.FLAG_REGISTER:
                Intent registerIntent = new Intent(mContext, InputVerifyCodeActivity.class);
                registerIntent.putExtra("flag", Global.FLAG_REGISTER);
                registerIntent.putExtra("phone_num", mCetPhone.getEditTextContent());
                startActivity(registerIntent);
                break;
            case Global.FLAG_FORGET:
                Intent forgetIntent = new Intent(mContext, InputVerifyCodeActivity.class);
                forgetIntent.putExtra("flag", Global.FLAG_FORGET);
                forgetIntent.putExtra("phone_num", mCetPhone.getEditTextContent());
                startActivity(forgetIntent);
                break;
        }
    }

    @OnClick({R.id.btn_left, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_next:
                if(checkInfo()) {
//                    getGetVerifyCode(mCetPhone.getEditTextContent(), mRequestUrl);
                }
                break;
        }
    }
}
