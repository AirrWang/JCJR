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
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.view.ImageTextHorizontalBarLess;

/**
 * Created by Airr on 2018/1/11.
 */

public class AccountSecurityActivity extends BaseActivity{

    private static final int CODE_CHANGE_LOGIN_PWD = 0;
    private static final int CODE_GESTURE = 1;
    private Context mContext;
    @ViewInject(R.id.ithb_login_psw)
    private ImageTextHorizontalBarLess mTthbLoginPsw;

    @ViewInject(R.id.ithb_trans_psw)
    private ImageTextHorizontalBarLess mTthbTransPsw;

    @ViewInject(R.id.ithb_gesture_psw)
    private ImageTextHorizontalBarLess mTthbGesturePsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_securite);

        ViewUtils.inject(this);
        mContext = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {

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


    @OnClick({R.id.ithb_login_psw,R.id.ithb_trans_psw,R.id.ithb_gesture_psw,R.id.btn_left})
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
                default:
                    break;

        }

    }


}
