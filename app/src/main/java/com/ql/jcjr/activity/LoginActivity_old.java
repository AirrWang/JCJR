package com.ql.jcjr.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
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
import com.ql.jcjr.service.DownloadApkService;
import com.ql.jcjr.utils.AppConfigCommon;
import com.ql.jcjr.utils.CommonUtils;
import com.ql.jcjr.utils.DownloadAPK;
import com.ql.jcjr.utils.FileUtil;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.SharedPreferencesUtils;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.crypt.DesUtil;
import com.ql.jcjr.view.CancelEditText;
import com.ql.jcjr.view.CommonToast;
import com.umeng.message.UTrack;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class LoginActivity_old extends BaseActivity implements CompoundButton.OnCheckedChangeListener,
        CommonToast.IPositiveButtonEvent, DownloadApkService.DownloadCallBack {

    @ViewInject(R.id.btn_login)
    private Button loginButton;
    @ViewInject(R.id.et_phone_number)
    private CancelEditText etPhoneNum;
    @ViewInject(R.id.et_password)
    private CancelEditText etPassword;
    //    @ViewInject(R.id.cb_remember_password)
//    private CheckBox cbRememberPassword;
    @ViewInject(R.id.btn_register)
    private Button btnRegister;
    @ViewInject(R.id.tv_forget_password)
    private TextView tvForgetPsw;
    @ViewInject(R.id.iv_show_psw)
    private ImageView ivShowPsw;

    private static final int EXCEPTION_CODE_DOWNLOAD_COMPLETED = 416; // apk已下载过
    static StringBuffer validateErrorMsg;
    private final String PSW_FILE_NAME = "wjthnfkghj";
    String phoneNumber;
    String password;
    private Context mContext;
    private boolean isPswShow = false;
    private boolean isRememberPassword = true;
    private String localVersionCode;
    private String mercId;
    private boolean isForceUpdate = false;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        mContext = this;
        init();
        showJpushMessageDialog();
    }

    private void init() {
        CommonToast.setIPositiveButtonEventListener(this);

        localVersionCode = String.valueOf(CommonUtils.getAppVersionCode());
        isNewVersions();
        LogUtil.i("localVersionCode = " + localVersionCode);
        getMerchId();
//        checkUpdate();

        etPhoneNum.getCancelEditText().setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_VARIATION_NORMAL);
        etPhoneNum.setEditTextContent(UserData.getInstance().getPhoneNumber());

        etPassword.getCancelEditText().setTransformationMethod(
                PasswordTransformationMethod.getInstance());
//        LogUtil.i("getPswString " + getPswString());
//        etPassword.setEditTextContent(getPswString());
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

    private void showJpushMessageDialog() {
        String content = getIntent().getStringExtra("jpush_content");
        if (!StringUtils.isBlank(content)) {
            CommonToast.showUnCancelableDialog(mContext, "新通知",
                    content, "确定", false);
        }
    }

    /**
     * 是不是最新版本
     */
    private void isNewVersions() {
//        String versionName = CommonUtils.getInstance().getAppVersionName();
        if (localVersionCode.equals(SharedPreferencesUtils.getInstance().getString(
                SharedPreferencesUtils.KEY_PHARMACY_VERSIONS, "0"))) {
            return;
        }
        SharedPreferencesUtils.getInstance(mContext, SharedPreferencesUtils.PHARMACY_CLEAR).clearSharedPreferences();
        SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.KEY_PHARMACY_VERSIONS, localVersionCode);
    }

    private void getMerchId() {
    }

    private String getPswString() {
        String decrypt = "";
        String psw = (String) FileUtil.readObjectFromDataFile(mContext, PSW_FILE_NAME);
        if (TextUtils.isEmpty(psw)) {
            return decrypt;
        }

        try {
            byte[] decrypted = Base64.decode(psw.getBytes("utf-8"), Base64.DEFAULT);
            decrypt = new String(DesUtil.decrypt(decrypted, AppConfigCommon.ENCRYPT_KEY.getBytes()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            LogUtil.i("decrypt = " + e);
            e.printStackTrace();
        }
        return decrypt.trim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonToast.unRegisteIPositiveButtonEventListener();

//        if (isRememberPassword) {
//            savePswString();
////            editor.putBoolean("login_remenber_psw", true).apply();
//        } else {
//            FileUtil.writeObjectToDataFile(mContext, "", PSW_FILE_NAME);
////            editor.putBoolean("login_remenber_psw", false).apply();
//        }
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

    @OnClick({R.id.iv_close, R.id.btn_login, R.id.btn_register, R.id.tv_forget_password, R.id.iv_show_psw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.btn_login:
                phoneNumber = etPhoneNum.getEditTextContent();
                password = etPassword.getEditTextContent();

                if (!validateLogin(phoneNumber, password)) {
                    CommonToast.showHintDialog(mContext, validateErrorMsg.toString());
                    return;
                } else {
                    login(phoneNumber, password);
                }
                break;

            case R.id.btn_register:
                Intent registerIntent = new Intent(mContext, InputPhoneNumActivity.class);
                registerIntent.putExtra("flag", Global.FLAG_REGISTER);
                startActivity(registerIntent);
                break;
            case R.id.tv_forget_password:
                Intent forgetIntent = new Intent(mContext, InputPhoneNumActivity.class);
                forgetIntent.putExtra("flag", Global.FLAG_FORGET);
                startActivity(forgetIntent);
                break;
            case R.id.iv_show_psw:
                if (!isPswShow) {
                    isPswShow = true;
                    etPassword.getCancelEditText()
                            .setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivShowPsw.setImageResource(R.drawable.show_psw_pressed);
                } else {
                    isPswShow = false;
                    etPassword.getCancelEditText()
                            .setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivShowPsw.setImageResource(R.drawable.show_psw_normal);
                }
                etPassword.getCancelEditText().setSelection(
                        etPassword.getEditTextContent().length());
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
                UserData.getInstance().setUSERID(entity.getResult().getToken());
//                setResult(RESULT_OK);
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

//    private void showDailPrompt() {
//
//        CommonDialog.Builder builder = new CommonDialog.Builder(mContext);
//        builder.setTitle("温馨提示")
//                .setMessage("是否联系客服？")
//                .setPositiveButton("拨打", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        Intent callIntent = new Intent(Intent.ACTION_CALL);
//                        Uri uri = Uri.parse("tel:" + AppConfig.SERVICE_HOTLINE_NUM);
//                        callIntent.setData(uri);
//                        startActivity(callIntent);
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        builder.create().show();
//    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        isRememberPassword = cbRememberPassword.isChecked();
    }

    @Override
    public void oClickEvent() {

        LogUtil.i("KEY_DOWNLOAD_APK_STATUS" + SharedPreferencesUtils.getInstance(
                getApplicationContext(),
                SharedPreferencesUtils.PHARMACY_CLEAR).getString(
                SharedPreferencesUtils.KEY_DOWNLOAD_APK_STATUS, ""));

        if ("onSuccess".equals(SharedPreferencesUtils.getInstance(getApplicationContext(),
                SharedPreferencesUtils.PHARMACY_CLEAR).getString(
                SharedPreferencesUtils.KEY_DOWNLOAD_APK_STATUS, ""))
                && DownloadAPK.getInstance().isApkExists(DownloadAPK.getInstance().getApkPath())) {
            DownloadAPK.getInstance().installApk(mContext, DownloadAPK.getInstance().getApkPath());
            if (isForceUpdate) {
                finish();
            }
        } else {
            if (isForceUpdate) {
                showProgressDialog();
            }
        }
    }


    private void showProgressDialog() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setTitle("正在下载...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(100);
        mProgressDialog.show();
    }

    @Override
    public void onDownloadStart() {

    }

    @Override
    public void onDownloadSuccess(ResponseInfo<File> responseInfo) {
        DownloadAPK.getInstance().installApk(mContext, DownloadAPK.getInstance().getApkPath());
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            finish();
        }
    }

    @Override
    public void onLoading(long total, long current, boolean isUploading) {
        LogUtil.i("total = " + total);
        LogUtil.i("current = " + current);
        int progress = (int) ((float) current / total * 100);
        if (mProgressDialog != null) {
            mProgressDialog.setProgress(progress);
        }

    }

    /**
     * 是不是最新版本
     */

    @Override
    public void onDownloadFailure(HttpException e, String s) {
        LogUtil.i("HttpException e = " + e);
        switch (e.getExceptionCode()) {
            case EXCEPTION_CODE_DOWNLOAD_COMPLETED:
                //已下载过
                DownloadAPK.getInstance().installApk(mContext,
                        DownloadAPK.getInstance().getApkPath());
                break;
        }
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
