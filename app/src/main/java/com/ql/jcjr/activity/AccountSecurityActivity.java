package com.ql.jcjr.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.ImageTextHorizontalBarLess;

/**
 * Created by Airr on 2018/1/11.
 */

public class AccountSecurityActivity extends BaseActivity implements  View.OnClickListener {

    private static final int CODE_CHANGE_LOGIN_PWD = 0;
    private static final int CODE_GESTURE = 1;
    private Context mContext;
    private FingerprintManagerCompat manager;
    private CancellationSignal mCancellationSignal;
    private final int RESTART_FINGER_PRINT = 1;
    private final int CHANGE_STATE = 2;

    @ViewInject(R.id.ithb_login_psw)
    private ImageTextHorizontalBarLess mTthbLoginPsw;

    @ViewInject(R.id.ithb_trans_psw)
    private ImageTextHorizontalBarLess mTthbTransPsw;

    @ViewInject(R.id.ithb_gesture_psw)
    private ImageTextHorizontalBarLess mTthbGesturePsw;

    @ViewInject(R.id.switch_finger)
    private Switch switch_finger;

    @ViewInject(R.id.ll_finger_print)
    private LinearLayout ll_finger_print;
    private TextView tv_status;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_securite);

        ViewUtils.inject(this);
        mContext = this;

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CHANGE_STATE:
                    switch_finger.setChecked(UserData.getInstance().getFingerPrint());
                    break;
                case RESTART_FINGER_PRINT:
                    Log.d("指纹模块", "handleMessage: 重启指纹模块");
                    mCancellationSignal = new CancellationSignal();
                    manager.authenticate(null, 0, mCancellationSignal, new MyCallBack(), handler);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {

        manager=FingerprintManagerCompat.from(this);
        if (manager.isHardwareDetected()&&manager.hasEnrolledFingerprints()){
            ll_finger_print.setVisibility(View.VISIBLE);
        }
        switch_finger.setChecked(UserData.getInstance().getFingerPrint());
        switch_finger.setOnClickListener(this);
        switch (UserData.getInstance().getIsSetPay()) {
            case "0":
                mTthbTransPsw.setRightTitleText("未设置");
                mTthbTransPsw.setRightTitleColor(getResources().getColor(R.color.font_grey_three));
                break;
            case "1":
                mTthbTransPsw.setRightTitleText("修改密码");
                mTthbTransPsw.setRightTitleColor(getResources().getColor(R.color.font_black));
                break;
        }

        boolean isSetGesture = UserData.getInstance().getIsOpenGesture();
        if(isSetGesture){
            mTthbGesturePsw.setRightTitleText("已开启");
            mTthbGesturePsw.setRightTitleColor(getResources().getColor(R.color.font_black));
        }
        else{
            mTthbGesturePsw.setRightTitleText("未开启");
            mTthbGesturePsw.setRightTitleColor(getResources().getColor(R.color.font_grey_three));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CODE_CHANGE_LOGIN_PWD:
                setResult(RESULT_OK);
                finish();
                break;
            case CODE_GESTURE:
                setResult(RESULT_OK);
                finish();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick({R.id.ithb_login_psw,R.id.ithb_trans_psw,R.id.ithb_gesture_psw,R.id.btn_left,R.id.switch_finger})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_left:
                finish();
                break;
            case R.id.ithb_login_psw:
                Intent loginPswIntent = new Intent(mContext, ChangePasswordActivity.class);
                startActivityForResult(loginPswIntent, CODE_CHANGE_LOGIN_PWD);
                break;
            case R.id.ithb_trans_psw:
                Intent transPswIntent = new Intent();
                switch (UserData.getInstance().getIsSetPay()) {
                    case "0":
                        transPswIntent.setClass(mContext, VerifyLoginPwdActivity.class);
                        startActivity(transPswIntent);
                        break;
                    case "1":
                        transPswIntent.setClass(mContext, VerifyPayPswActivity.class);
                        startActivity(transPswIntent);
                        break;
                }
                break;
            case R.id.ithb_gesture_psw:
                Intent gestureIntent = new Intent(mContext, SettingGestureActivity.class);
//                gestureIntent.putExtra("user_icon_url", mUserIconUrl);
                startActivityForResult(gestureIntent, CODE_GESTURE);
                break;
            case R.id.switch_finger:
                boolean b=UserData.getInstance().getFingerPrint();
                if (b){
                    showFingerDialog(b);

                }else {
                    showFingerDialog(b);
                }
                default:
                    break;

        }

    }

    private void showFingerDialog(boolean b) {
        switch_finger.setChecked(b);
        dialog = new Dialog(this, R.style.CommonDialog);
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.finger_dialog, null);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
        tv_status = (TextView) view.findViewById(R.id.tv_status);
        Button btn_dis= (Button) view.findViewById(R.id.btn_dis);
        handler.sendEmptyMessage(RESTART_FINGER_PRINT);
//        manager.authenticate(null, 0, mCancellationSignal, new MyCallBack(), null);


        btn_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mCancellationSignal.cancel();
            }
        });
    }
    private int a =4;

    private class MyCallBack extends FingerprintManagerCompat.AuthenticationCallback {
        private static final String TAG = "MyCallBack";

        // 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            Log.d(TAG, "onAuthenticationError: " + errString);
        }

        // 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
        @Override
        public void onAuthenticationFailed() {
            Log.d(TAG, "onAuthenticationFailed: " + "验证失败");
            tv_status.setText("再试一次");
            a--;
            if (a==0 ){
                dialog.dismiss();
                mCancellationSignal.cancel();
                CommonToast.showHintDialog(mContext,"指纹验证失败");
            }
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            Log.d(TAG, "onAuthenticationHelp: " + helpString);
        }

        // 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult
                                                      result) {
            Log.d(TAG, "onAuthenticationSucceeded: " + "验证成功");
            dialog.dismiss();
            mCancellationSignal.cancel();
            boolean a =UserData.getInstance().getFingerPrint();
            if (a){
                CommonToast.showHintDialog(mContext,"指纹密码已关闭");
            }else {
                CommonToast.showHintDialog(mContext,"指纹密码启用成功");
            }
            UserData.getInstance().setFingerPrint(!a);
            handler.sendEmptyMessage(CHANGE_STATE);
        }
    }
}
