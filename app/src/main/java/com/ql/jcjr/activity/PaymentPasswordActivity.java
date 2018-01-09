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
import com.ql.jcjr.view.PwdEditText;

public class PaymentPasswordActivity extends BaseActivity implements PwdEditText.OnInputFinishListener{

    @ViewInject(R.id.et_pwd)
    private PwdEditText mEtPwd;

    private Context mContext;
    private String oldPayPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_password);

        ViewUtils.inject(this);
        mContext = this;
        init();
    }

    private void init() {
        oldPayPsw = getIntent().getStringExtra("old_pay_psw");
        mEtPwd.setOnInputFinishListener(this);
    }

    private void setPayPwd(String pwd) {
//        SenderResultModel resultModel = ParamsManager.sendeSetPayPwd(pwd);
//
//        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {
//
//            @Override
//            public void onSuccess(String responeJson) {
//                LogUtil.i("设置支付密码 " + responeJson);
//                setResult(RESULT_OK);
//                CommonToast.makeCustomText(mContext, "设置成功");
//                handler.sendEmptyMessageDelayed(HANDLER_CHANGE_SUCCESS, 3000);
//                UserData.getInstance().setIsSetPay("1");
//            }
//
//            @Override
//            public void onFailure(ResponseEntity entity) {
//                LogUtil.i("设置支付密码 " + entity.errorInfo);
//                CommonToast.showHintDialog(mContext, entity.errorInfo);
//            }
//
//        }, mContext);

        Intent intent = new Intent(mContext, ConfirmPayPwdActivity.class);
        intent.putExtra("pay_pwd", pwd);
        intent.putExtra("old_pay_psw", oldPayPsw);
        startActivityForResult(intent, CODE_SET);

    }

    private static final int CODE_SET = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CODE_SET:
                setResult(RESULT_OK);
                finish();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.btn_left})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_left:
                finish();
                break;
        }
    }

    @Override
    public void onInputFinish(String password) {
        setPayPwd(password);
    }
}
