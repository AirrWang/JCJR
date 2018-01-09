package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.utils.AppConfigCommon;
import com.ql.jcjr.utils.FileUtil;
import com.ql.jcjr.utils.JsonUtils;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.crypt.DesUtil;
import com.ql.jcjr.view.CommonToast;
import com.umeng.message.UTrack;

public class RegisterActivity_old extends BaseActivity {

    @ViewInject(R.id.et_psw)
    private EditText mEtLoginPsw;
    @ViewInject(R.id.et_inviter)
    private EditText mEtInviter;
    @ViewInject(R.id.btn_register)
    private Button mBtnRegister;

    private Context mContext;
    private String mPhoneNum;

    private static final int HANDLER_REG_SUCCESS = 0;

    private final String PSW_FILE_NAME = "wjthnfkghj";

    /**
     * Handler
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_REG_SUCCESS:
                    Intent mIntent = new Intent(mContext, LoginActivityCheck.class);
                    mIntent.putExtra("flag", Global.FLAG_REGISTER);
                    startActivity(mIntent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);
        mContext = this;

        mPhoneNum = getIntent().getStringExtra("phone_num");
    }

    private boolean checkInfo() {
        if(StringUtils.isBlank(mEtLoginPsw.getText().toString())) {
            CommonToast.showHintDialog(mContext,"请输入登录密码");
            return false;
        }
        return true;
    }

    public void register(final String phone, String psw, String inviter, String appSing) {
        SenderResultModel resultModel = ParamsManager.senderRegister(phone, psw, inviter, appSing);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("注册成功 " + responeJson);
                CommonToast.makeCustomText(mContext, "注册成功！");
                UserData.getInstance().setPhoneNumber(phone);
                UserData.getInstance().setUSERID(JsonUtils.getJSONObject(responeJson).optString("result"));
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

                mBtnRegister.setEnabled(false);
                handler.sendEmptyMessageDelayed(HANDLER_REG_SUCCESS, 3000);
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("注册失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, this);
    }

    private void savePswString() {
        String encrypt = "";
        String password =  mEtLoginPsw.getText().toString().trim();
        try {
            byte[] encrypted =
                    DesUtil.encrypt(password.getBytes("utf-8"), AppConfigCommon.ENCRYPT_KEY.getBytes());
            encrypt = Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileUtil.writeObjectToDataFile(mContext, encrypt, PSW_FILE_NAME);
    }

    private String getChannel() {
        try {
            PackageManager pm = getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "";
    }

    @OnClick({R.id.btn_left, R.id.btn_register})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_register:
                if(checkInfo()) {
                    String psw = mEtLoginPsw.getText().toString().trim();
                    String inviter = mEtInviter.getText().toString().trim();

                    register(mPhoneNum, psw, inviter, getChannel());
                }
                break;
        }
    }
}
