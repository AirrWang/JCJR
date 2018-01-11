package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.CancelEditTextGrey;
import com.ql.jcjr.view.CommonToast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class LoginActivityCheck extends BaseActivity {

    @ViewInject(R.id.btn_login)
    private Button loginButton;
    @ViewInject(R.id.et_phone_number)
    private CancelEditTextGrey etPhoneNum;

    private StringBuffer validateErrorMsg;
    private String phoneNumber;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_check);
        ViewUtils.inject(this);
        mContext = this;
        init();
    }

    private void init() {
        etPhoneNum.getCancelEditText().setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_VARIATION_NORMAL);
        etPhoneNum.setEditTextContent(UserData.getInstance().getPhoneNumber());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        etPhoneNum.setEditTextContent(UserData.getInstance().getPhoneNumber());

        String flag = intent.getStringExtra("flag");
        if(flag!= null && flag.equals(Global.FLAG_REGISTER)){
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.iv_close, R.id.btn_login, R.id.btn_register, R.id.tv_forget_password, R.id.iv_show_psw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.btn_login:
                phoneNumber = etPhoneNum.getEditTextContent();

                if (!validateLogin(phoneNumber)) {
                    CommonToast.showHintDialog(mContext, validateErrorMsg.toString());
                    return;
                } else {
                    loginCheck(phoneNumber);
                }
                break;
        }
    }

    //判断是不是已经填写手机号码和密码
    private boolean validateLogin(String phoneNumber) {
        validateErrorMsg = new StringBuffer();

        if (StringUtils.isBlank(phoneNumber)) {
            validateErrorMsg.append("请输入手机号码");
            return false;
        }
        if (!StringUtils.isPhoneNumber(phoneNumber)){
            validateErrorMsg.append("手机号码格式错误");
            return false;
        }
        return true;
    }

    private void loginCheck(final String phone) {
        SenderResultModel resultModel = ParamsManager.senderLoginCheck(phone);
        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {
            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("登录检查成功 " + responeJson);
                try {
                    JSONObject object = new JSONObject(responeJson);
                    int result = object.optInt("result", 0);
                    if(result==1){
                        //已注册 去登录
                        Intent intent = new Intent(LoginActivityCheck.this, LoginActivity.class);
                        intent.putExtra("phone_num", phone);
                        startActivity(intent);
                    }
                    else{
                        //未注册 去注册
                        Intent intent = new Intent(LoginActivityCheck.this, RegisterActivity.class);
                        intent.putExtra("flag", Global.FLAG_REGISTER);
                        intent.putExtra("phone_num", phone);
                        startActivity(intent);
                    }
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("登录检查成功 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, this);
    }



}
