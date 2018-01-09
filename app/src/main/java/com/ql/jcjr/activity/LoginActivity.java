package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.LoginEntity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.AppConfigCommon;
import com.ql.jcjr.utils.FileUtil;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.crypt.DesUtil;
import com.ql.jcjr.view.CancelEditTextGrey;
import com.ql.jcjr.view.CommonToast;
import com.umeng.message.UTrack;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class LoginActivity extends BaseActivity{

    @ViewInject(R.id.btn_login)
    private Button loginButton;
    @ViewInject(R.id.et_phone_number)
    private TextView etPhoneNum;
    @ViewInject(R.id.et_password)
    private CancelEditTextGrey etPassword;

    static StringBuffer validateErrorMsg;
    private final String PSW_FILE_NAME = "wjthnfkghj";
    String phoneNumber;
    String password;
    private Context mContext;
    private boolean isPswShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        mContext = this;
        init();
    }

    private void init() {
        phoneNumber = getIntent().getStringExtra("phone_num");
        etPhoneNum.setText(phoneNumber);

        etPassword.getCancelEditText().setTransformationMethod(
                PasswordTransformationMethod.getInstance());

        etPassword.setOnCancelEditEventListener(new CancelEditTextGrey.CancelEditEventListener() {
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
                    etPassword.getCancelEditText()
                            .setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPassword.setRightExtraImageIcon(R.drawable.show_psw_pressed);
                } else {
                    isPswShow = false;
                    etPassword.getCancelEditText()
                            .setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPassword.setRightExtraImageIcon(R.drawable.show_psw_normal);
                }
                etPassword.getCancelEditText().setSelection(
                        etPassword.getEditTextContent().length());
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String flag = intent.getStringExtra("flag");
        if(flag!= null && flag.equals(Global.FLAG_REGISTER)){
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void savePswString() {
        String encrypt = "";
        String password = etPassword.getEditTextContent();
        try {
            byte[] encrypted =
                    DesUtil.encrypt(password.getBytes("utf-8"), AppConfigCommon.ENCRYPT_KEY.getBytes());
            encrypt = Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileUtil.writeObjectToDataFile(mContext, encrypt, PSW_FILE_NAME);
    }

    @OnClick({R.id.btn_left, R.id.btn_login, R.id.tv_forget_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;

            case R.id.btn_login:
                password = etPassword.getEditTextContent();

                if (!validateLogin(phoneNumber, password)) {
                    CommonToast.showHintDialog(mContext, validateErrorMsg.toString());
                    return;
                } else {
                    login(phoneNumber, password);
                }
                break;

            case R.id.tv_forget_password:
                Intent forgetIntent = new Intent(mContext, RegisterActivity.class);
                forgetIntent.putExtra("flag", Global.FLAG_FORGET);
                forgetIntent.putExtra("phone_num", phoneNumber);
                startActivity(forgetIntent);
                break;
        }
    }

    //判断是不是已经填写手机号码和密码
    private boolean validateLogin(String phoneNumber, String password) {
        validateErrorMsg = new StringBuffer();

        if (StringUtils.isBlank(phoneNumber)) {
            validateErrorMsg.append("请输入账号");
            return false;
        }
        if (StringUtils.isBlank(password)) {
            validateErrorMsg.append("密码不能为空");
            return false;
        }
        return true;
    }

    private void login(final String phone, String psw) {
        SenderResultModel resultModel = ParamsManager.senderLogin(phone, psw);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("登录成功 " + responeJson);
                LoginEntity entity = GsonParser.getParsedObj(responeJson, LoginEntity.class);
                UserData.getInstance().setPhoneNumber(phone);
                UserData.getInstance().setUSERID(entity.getResult().getUserId());
                //保存密码，以后token自登录时使用
                savePswString();

                //登录后即调用友盟绑定
                try{
                    String uid = UserData.getInstance().getUSERID();
                    if(null != uid && uid.length()>0){
                        //绑定uid
                        JcbApplication.getInstance().getPushAgent().setAlias(uid, "JC_UID",
                                new UTrack.ICallBack() {
                                    @Override
                                    public void onMessage(boolean isSuccess, String message) {
                                        LogUtil.i("mPushAgent setAlias " + isSuccess);
                                    }
                                });
                    }
                }catch(Exception e){
                }

                finish();
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("登录失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, this);
    }

}
