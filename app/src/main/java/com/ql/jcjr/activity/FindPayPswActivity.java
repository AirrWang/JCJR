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
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.PwdEditText;

public class FindPayPswActivity extends BaseActivity implements PwdEditText.OnInputFinishListener{

    @ViewInject(R.id.et_pwd)
    private PwdEditText mEtPwd;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pay_psw);
        ViewUtils.inject(this);
        mContext = this;
        mEtPwd.setOnInputFinishListener(this);
    }

    private void setPayPwd(String pwd) {
        SenderResultModel resultModel = ParamsManager.sendeFindPayPsw(pwd);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("设置支付密码 " + responeJson);
                CommonToast.makeCustomText(mContext, "支付密码设置成功");
                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("设置支付密码 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);

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
