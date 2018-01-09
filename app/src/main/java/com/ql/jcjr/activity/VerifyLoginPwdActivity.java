package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.CommonToast;


public class VerifyLoginPwdActivity extends BaseActivity {

    @ViewInject(R.id.et_login_pwd)
    private EditText mEtLoginPwd;
    @ViewInject(R.id.btn_next)
    private Button mBtnNext;

    private Context mContext;

    private static final int CODE_SET = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_login_pwd);

        ViewUtils.inject(this);
        mContext = this;
    }

    private void checkLoginPwd(String pwd) {
        SenderResultModel resultModel = ParamsManager.senderCheckLoginPwd(pwd);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("验证登录密码 " + responeJson);
                Intent intent = new Intent(mContext, PaymentPasswordActivity.class);
                startActivityForResult(intent, CODE_SET);
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("验证登录密码 " + entity.errorInfo);
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
            case CODE_SET:
                finish();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean checkInfo() {
        if (StringUtils.isBlank(mEtLoginPwd.getText().toString().trim())){
            CommonToast.showHintDialog(mContext, "请输入登录密码");
            return false;
        }
        return true;
    }

    @OnClick({R.id.btn_left, R.id.btn_next})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_next:
                if(checkInfo()) {
                    checkLoginPwd(mEtLoginPwd.getText().toString().trim());
                }
                break;
        }
    }
}
