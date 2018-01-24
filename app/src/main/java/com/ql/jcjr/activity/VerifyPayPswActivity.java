package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.entity.VerificationCodeEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.PwdEditText;


public class VerifyPayPswActivity extends BaseActivity implements
        PwdEditText.OnInputFinishListener{

    @ViewInject(R.id.et_verify_pwd)
    private PwdEditText mEtVerifyPwd;
    @ViewInject(R.id.et_pay_pwd)
    private EditText mEtPwd;

    private Context mContext;

    private static final int CODE_SET = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_pay_psw);

        ViewUtils.inject(this);
        mContext = this;
        mEtVerifyPwd.setOnInputFinishListener(this);
    }

    private void checkPayPwd(final String pwd) {
        SenderResultModel resultModel = ParamsManager.senderCheckPayPwd(pwd);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("验证支付密码 " + responeJson);
                Intent intent = new Intent(mContext, PaymentPasswordActivity.class);
                intent.putExtra("old_pay_psw", pwd);
                startActivityForResult(intent, CODE_SET);
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("验证支付密码 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

    public void getGetVerifyCode(String phone, String url) {

        SenderResultModel resultModel = ParamsManager.senderGetVerifyCode(phone, url);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("获取验证码 " + responeJson);
                VerificationCodeEntity
                        mEntity = GsonParser.getParsedObj(responeJson, VerificationCodeEntity.class);
                getGetVerifyCodesuccess();

            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("获取验证码 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, this);
    }

    private void getGetVerifyCodesuccess() {
        Intent forgetIntent = new Intent(mContext, InputVerifyCodeActivity.class);
        forgetIntent.putExtra("flag", Global.FLAG_FORGET_TRANS);
        forgetIntent.putExtra("phone_num", UserData.getInstance().getPhoneNumber());
        startActivity(forgetIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CODE_SET:
                UserData.getInstance().setIsSetPay("1");
                finish();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkInfo() {
        if (StringUtils.isBlank(mEtPwd.getText().toString().trim())){
            CommonToast.showHintDialog(mContext, "请输入登录密码");
            return false;
        }
        return true;
    }

    @OnClick({R.id.btn_left, R.id.tv_forget_pay_psw, R.id.btn_next})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_left:
                finish();
                break;
            case R.id.tv_forget_pay_psw:
                getGetVerifyCode(UserData.getInstance().getPhoneNumber(), RequestURL.TRANS_PWD_VERIFY_URL);
                break;
            case R.id.btn_next:
                if(checkInfo()) {
                    checkPayPwd(mEtPwd.getText().toString().trim());
                }
                break;
        }
    }

    @Override
    public void onInputFinish(String password) {
        checkPayPwd(password);
    }
}
