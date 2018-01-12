package com.ql.jcjr.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.timer.TimerHandler;
import com.ql.jcjr.utils.AppConfigCommon;
import com.ql.jcjr.utils.FileUtil;
import com.ql.jcjr.utils.JsonUtils;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.ToastUtil;
import com.ql.jcjr.utils.UrlUtil;
import com.ql.jcjr.utils.crypt.DesUtil;
import com.ql.jcjr.view.ActionBar;
import com.ql.jcjr.view.CancelEditTextGrey;
import com.ql.jcjr.view.CommonToast;
import com.umeng.message.UTrack;

public class RegisterActivity extends BaseActivity {

    @ViewInject(R.id.ab_header)
    private ActionBar mActionBar;

    @ViewInject(R.id.tv_register_send)
    private TextView mTvRegisterSend;
    @ViewInject(R.id.et_sms_code)
    private CancelEditTextGrey mEtSmsCode;
    @ViewInject(R.id.et_psw)
    private CancelEditTextGrey mEtLoginPsw;
    @ViewInject(R.id.et_inviter)
    private CancelEditTextGrey mEtInviter;
    @ViewInject(R.id.btn_register)
    private Button mBtnRegister;

    //用户协议
    @ViewInject(R.id.ll_register_agreement)
    private LinearLayout mLlRegisterAgreement;
    @ViewInject(R.id.ll_register_checkbox)
    private LinearLayout mLLRegisterCheckbox;
    @ViewInject(R.id.iv_register_checkbox)
    private ImageView mIvRegisterCheckbox;
    @ViewInject(R.id.tv_register_read)
    private TextView mTvRegisterRead;
    @ViewInject(R.id.tv_register_agreement)
    private TextView mTvRegisterAgreement;

    private boolean isChechAgreement = true;

    @ViewInject(R.id.ll_register_888)
    private LinearLayout mLlRegister888;

    private Context mContext;
    private String mPhoneNum;
    private String mFlag;
    private String mRequestCodeUrl;
    private String mCheckCodeUrl;
    private TimerHandler mTimerHandler;

    private boolean isPswShow = false;

    private static final int HANDLER_REG_SUCCESS = 0;
    private static final int HANDLER_FINDPSW_SUCCESS = 1;
    private final String PSW_FILE_NAME = "wjthnfkghj";

    /**
     * Handler
     */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_REG_SUCCESS:
                    startActivity(new Intent().setClass(RegisterActivity.this,RegisterFinishActivity.class));
                    RegisterActivity.this.finish();
//                    if(mFlag.equals(Global.FLAG_REGISTER)){
//                        RegisterActivity.this.finish();
//                    }
//                    else if(mFlag.equals(Global.FLAG_FORGET)){
//                        Intent mIntent = new Intent(mContext, LoginActivityCheck.class);
//                        mIntent.putExtra("flag", Global.FLAG_REGISTER);
//                        startActivity(mIntent);
//                    }
                    break;
                case HANDLER_FINDPSW_SUCCESS:
                    RegisterActivity.this.finish();
                    break;
            }
        }
    };

    TimerHandler.ITimerListener mITimerListener = new TimerHandler.ITimerListener() {
        @Override
        public void startTimer() {
            mEtSmsCode.setRightExtraTextViewEnable(false);
        }

        @Override
        public void timing(int second) {
            mEtSmsCode.setRightExtraTextViewEnable(false);
            mEtSmsCode.setRightExtraTextViewContent("重新获取（" + second + "s）");
        }

        @Override
        public void finishTimer() {
            mEtSmsCode.setRightExtraTextViewContent("重新获取");
            mEtSmsCode.setRightExtraTextViewEnable(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);
        mContext = this;
        init();
        getGetVerifyCode(mPhoneNum, mRequestCodeUrl);
    }

    private void init() {
        mPhoneNum = getIntent().getStringExtra("phone_num");
        mFlag = getIntent().getStringExtra("flag");

        mTvRegisterSend.setText("短信验证码已发送至"+mPhoneNum+"，请查收");
        mEtSmsCode.setOnCancelEditEventListener(new CancelEditTextGrey.CancelEditEventListener() {
            @Override
            public void onCancelFocusChange(boolean hasFocus) {
            }

            @Override
            public void onClickRightExtraTextView() {
                getGetVerifyCode(mPhoneNum, mRequestCodeUrl);
            }

            @Override
            public void onClickRightExtraImageView() {
            }
        });

        mEtLoginPsw.getCancelEditText().setTransformationMethod(
                PasswordTransformationMethod.getInstance());

        mEtLoginPsw.setOnCancelEditEventListener(new CancelEditTextGrey.CancelEditEventListener() {
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
                    mEtLoginPsw.getCancelEditText()
                            .setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mEtLoginPsw.setRightExtraImageIcon(R.drawable.show_psw_pressed_1);
                } else {
                    isPswShow = false;
                    mEtLoginPsw.getCancelEditText()
                            .setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mEtLoginPsw.setRightExtraImageIcon(R.drawable.show_psw_normal_1);
                }
                mEtLoginPsw.getCancelEditText().setSelection(
                        mEtLoginPsw.getEditTextContent().length());
            }
        });

        switch (mFlag){
            case Global.FLAG_REGISTER:
                mActionBar.setTitle("注册");
                mRequestCodeUrl = RequestURL.REG_VERIFY_URL;
                mCheckCodeUrl = RequestURL.VALIDATE_REG_VERIFY_URL;
                break;
            case Global.FLAG_FORGET:
                mActionBar.setTitle("找回密码");
                mRequestCodeUrl = RequestURL.PWD_VERIFY_URL;
                mCheckCodeUrl = RequestURL.VALIDATE_PWD_VERIFY_URL;

                mEtInviter.setVisibility(View.GONE);
                mBtnRegister.setText("设置密码");
                mLlRegisterAgreement.setVisibility(View.GONE);
                mLlRegister888.setVisibility(View.GONE);
                break;
        }

        mTimerHandler = new TimerHandler(60);
        mTimerHandler.setITimerListener(mITimerListener);
//        mTimerHandler.startTask();
    }

    private boolean checkInfo() {
        if(StringUtils.isBlank(mEtSmsCode.getEditTextContent().toString())) {
            CommonToast.showHintDialog(mContext,"请输入短信验证码");
            return false;
        }
        if(StringUtils.isBlank(mEtLoginPsw.getEditTextContent().toString())) {
            CommonToast.showHintDialog(mContext,"请输入登录密码");
            return false;
        }
        return true;
    }

    /**
     * 获取短信验证码
     * @param phone
     * @param url
     */
    public void getGetVerifyCode(String phone, String url) {

        SenderResultModel resultModel = ParamsManager.senderGetVerifyCode(phone, url);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("获取验证码 " + responeJson);
                mTimerHandler.setITimerListener(mITimerListener);
                mTimerHandler.startTask();
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("获取验证码 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, this);
    }

    /**
     * 验证短信验证码
     * @param phone
     * @param code
     * @param url
     */
    public void validateCode(String phone, String code, String url) {

        SenderResultModel resultModel = null;

        switch (mFlag){
            case Global.FLAG_REGISTER:
                resultModel = ParamsManager.senderRegValidateCode(phone, code, url);
                break;
            case Global.FLAG_FORGET:
                resultModel = ParamsManager.senderPwdValidateCode(phone, code, url);
                break;
        }

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("验证验证码 " + responeJson);

                String psw = mEtLoginPsw.getEditTextContent().toString();
                switch (mFlag){
                    case Global.FLAG_REGISTER:
                        String inviter = mEtInviter.getEditTextContent().toString().trim();
                        register(mPhoneNum, psw, inviter, getChannel());
                        break;
                    case Global.FLAG_FORGET:
                        changePwd(mPhoneNum, psw);
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

    /**
     * 找回密码
     * @param phone
     * @param psw
     */
    private void changePwd(String phone, String psw) {
        SenderResultModel resultModel = ParamsManager.senderFindPsw(phone, psw);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("找回密码 " + responeJson);
                CommonToast.makeCustomText(mContext, "找回密码成功");
                mBtnRegister.setEnabled(false);
                handler.sendEmptyMessageDelayed(HANDLER_FINDPSW_SUCCESS, 2000);
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("找回密码失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, this);
    }

    /**
     * 注册
     * @param phone
     * @param psw
     * @param inviter
     * @param appSing
     */
    public void register(final String phone, String psw, String inviter, String appSing) {
        SenderResultModel resultModel = ParamsManager.senderRegister(phone, psw, inviter, appSing);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("注册成功 " + responeJson);
//                CommonToast.makeCustomText(mContext, "注册成功！");
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
                handler.sendEmptyMessageDelayed(HANDLER_REG_SUCCESS, 2000);
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
        String password =  mEtLoginPsw.getEditTextContent().toString();
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

    @OnClick({R.id.btn_left, R.id.btn_register, R.id.ll_register_checkbox, R.id.tv_register_agreement})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_left:
                finish();
                break;

            case R.id.btn_register:
                if(checkInfo()) {
                    if(mFlag.equals(Global.FLAG_REGISTER) && !isChechAgreement){
                        ToastUtil.showToast(RegisterActivity.this, "您必须先同意积财金融协议");
                        return;
                    }
                    validateCode(mPhoneNum, mEtSmsCode.getEditTextContent().toString().trim(), mCheckCodeUrl);
                }
                break;

            case R.id.ll_register_checkbox:
                //切换状态
                if(isChechAgreement){
                    mIvRegisterCheckbox.setImageResource(R.drawable.checkbox_normal);
                    mTvRegisterRead.setTextColor(getResources().getColor(R.color.font_grey));
                    mTvRegisterAgreement.setTextColor(getResources().getColor(R.color.font_grey));
                    isChechAgreement = false;
                }
                else{
                    mIvRegisterCheckbox.setImageResource(R.drawable.checkbox_select);
                    mTvRegisterRead.setTextColor(getResources().getColor(R.color.font_user_agreement));
                    mTvRegisterAgreement.setTextColor(getResources().getColor(R.color.btn_main));
                    isChechAgreement = true;
                }
                break;
            case R.id.tv_register_agreement:
                //打开用户协议
                UrlUtil.showHtmlPage(mContext,"注册协议", RequestURL.REGISTE_URL);
        }
    }
}
