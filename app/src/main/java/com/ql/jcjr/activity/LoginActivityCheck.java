package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.AppIdEntity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.KeyboardUtil;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.ToastUtil;
import com.ql.jcjr.view.CancelEditTextGrey;
import com.ql.jcjr.view.CommonToast;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class LoginActivityCheck extends BaseActivity {

    @ViewInject(R.id.btn_login)
    private Button loginButton;
    @ViewInject(R.id.et_phone_number)
    private CancelEditTextGrey etPhoneNum;
    @ViewInject(R.id.rl_container)
    private RelativeLayout rl_container;
    @ViewInject(R.id.ll_container)
    private LinearLayout ll_container;

    public static LoginActivityCheck instance = null;

    private StringBuffer validateErrorMsg;
    private String phoneNumber;
    private Context mContext;
    private String appid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login_check);
        ViewUtils.inject(this);
        mContext = this;
        instance = this;
        Map<String, String> datas = new HashMap<String, String>();
        MobclickAgent.onEventValue(this, "register_phonenumber", datas, 1);
        init();
        loginButton.setEnabled(false);
        loginButton.setBackgroundResource(R.drawable.btn_pressed_enable);
    }

    private void init() {
        etPhoneNum.getCancelEditText().setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_VARIATION_NORMAL);
//        etPhoneNum.setEditTextContent(UserData.getInstance().getPhoneNumber());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            etPhoneNum.getCancelEditText().setShowSoftInputOnFocus(false);

        }else {
            etPhoneNum.getCancelEditText().setInputType(InputType.TYPE_NULL);

        }
        etPhoneNum.getCancelEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                new KeyboardUtil(mContext, LoginActivityCheck.this, etPhoneNum.getCancelEditText(),0).showKeyboard();
                return false;
            }
        });

        etPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    loginButton.setEnabled(true);
                    loginButton.setBackgroundResource(R.drawable.login_button_selector);
                    if (s.length()>11){
                        s.replace(11,s.length(),"");
                    }
                } else {
                    loginButton.setEnabled(false);
                    loginButton.setBackgroundResource(R.drawable.btn_pressed_enable);
                }
            }
        });

//        //根布局
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
//
//                        scrollToPos(0, scrollHegit);
//                } else {//否则判断为输入法隐藏了
//                    if (rl_container.getScrollY() != 0)
//                        scrollToPos(scrollHegit, 0);
//                }
//            }
//        });
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
        instance=null;
    }

    @OnClick({R.id.iv_close, R.id.btn_login, R.id.btn_register, R.id.tv_forget_password, R.id.iv_show_psw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.btn_login:
                Map<String, String> datas = new HashMap<String, String>();
                MobclickAgent.onEventValue(mContext, "click_phonenumber_next", datas, 1);

                phoneNumber = etPhoneNum.getEditTextContent();

                if (!validateLogin(phoneNumber)) {

                    CommonToast.makeText(validateErrorMsg.toString());
                    return;
                } else {
                    if (isConnect()) {
                        loginCheck(phoneNumber);
                    }else {
                        ToastUtil.showToast(mContext,"请检查网络连接");
                    }

                }
                break;
        }
    }
    private boolean isConnect() {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = JcbApplication.getInstance().getConnectivity();
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
//			System.out.println("获取网络状态异常");
            return false;
        }
        return false;
    }
    //判断是不是已经填写手机号码和密码
    private boolean validateLogin(String phoneNumber) {
        validateErrorMsg = new StringBuffer();

//        if (StringUtils.isBlank(phoneNumber)) {
//            validateErrorMsg.append("请输入手机号码");
//            return false;
//        }
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
                        getAPPID();
                    }
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

    private void getAPPID() {
        SenderResultModel resultModel = ParamsManager.getAppId();
        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {
            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("获取appid成功 " + responeJson);
                AppIdEntity entity = GsonParser.getParsedObj(responeJson, AppIdEntity.class);
                appid=entity.getResult().getAppid();
                Intent intent = new Intent(LoginActivityCheck.this, RegisterActivity.class);
                intent.putExtra("flag", Global.FLAG_REGISTER);
                intent.putExtra("phone_num", phoneNumber);
                intent.putExtra("appid",appid);
                startActivity(intent);
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("获取appid失败 " + entity.errorInfo);
            }

        }, this);

    }


}
