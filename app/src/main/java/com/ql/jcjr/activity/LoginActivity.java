package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.ApkInfoEntity;
import com.ql.jcjr.entity.LoginEntity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.AppConfigCommon;
import com.ql.jcjr.utils.CommonUtils;
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
    @ViewInject(R.id.rl_container)
    private RelativeLayout rl_container;
    @ViewInject(R.id.ll_container)
    private LinearLayout ll_container;


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

//    private void scrollToPos(int start, int end) {
//        ValueAnimator animator = ValueAnimator.ofInt(start, end);
//        animator.setDuration(250);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                rl_container.scrollTo(0, (Integer) valueAnimator.getAnimatedValue());
//            }
//        });
//        animator.start();
//    }

    private void init() {

//        rl_container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            private int[] sc;
//            private int scrollHegit;
//
//            @Override
//            public void onGlobalLayout() {
//                Rect r = new Rect();
//                rl_container.getWindowVisibleDisplayFrame(r);
//                if (sc == null) {
//                    sc = new int[2];
//                    ll_container.getLocationOnScreen(sc);
//                }
//                //r.top 是状态栏高度
//                int screenHeight = rl_container.getRootView().getHeight();
//                int softHeight = screenHeight - r.bottom;
//
//                if (softHeight > 140) {//当输入法高度大于100判定为输入法打开了  设置大点，有虚拟键的会超过100
//                    scrollHegit = sc[1] +ll_container.getHeight() -(screenHeight-softHeight);//可以加个5dp的距离这样，按钮不会挨着输入法
//                    if (rl_container.getScrollY() != scrollHegit&&scrollHegit>0)
//                        scrollToPos(0, scrollHegit);
//                } else {//否则判断为输入法隐藏了
//                    if (rl_container.getScrollY() != 0)
//                        scrollToPos(scrollHegit, 0);
//                }
//            }
//        });
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
                    etPassword.setRightExtraImageIcon(R.drawable.show_psw_pressed_1);
                } else {
                    isPswShow = false;
                    etPassword.getCancelEditText()
                            .setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etPassword.setRightExtraImageIcon(R.drawable.show_psw_normal_1);
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

    @OnClick({R.id.btn_left, R.id.btn_login, R.id.tv_forget_password,R.id.btn_login_other})
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
            case R.id.btn_login_other:
                Intent intent=new Intent(mContext,LoginActivityCheck.class);
                startActivity(intent);
                finish();

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
                String device_token=JcbApplication.getInstance().getPushAgent().getRegistrationId();
                //登录后上传设备信息
                getAppInfo();
                if (LoginActivityCheck.instance!=null){
                    LoginActivityCheck.instance.finish();
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

    private void getAppInfo() {
        SenderResultModel resultModel = ParamsManager.senderGetAppInfo();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("获取apk信息成功 " + responeJson);
                ApkInfoEntity entity = GsonParser.getParsedObj(responeJson, ApkInfoEntity.class);
                if(entity.getRSPCODE().equals(Global.RESULT_SUCCESS)){
                    //设置分享信息
                    CommonUtils.shareUrl = entity.getResult().getHome_url();
                    CommonUtils.shareIcon = entity.getResult().getIcon_url();
                    CommonUtils.shareTitle = entity.getResult().getShare_title();
                    CommonUtils.shareContent = entity.getResult().getShare_content();
                    //判断新版本
                }
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("获取apk信息失败 " + entity.errorInfo);
//                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, this);
    }


}
