package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.CommonToast;


public class ChangePasswordActivity extends BaseActivity {

    @ViewInject(R.id.et_old_psw)
    private EditText mOldPswET;
    @ViewInject(R.id.et_new_psw)
    private EditText mNewPswET;
    @ViewInject(R.id.et_confirm_psw)
    private EditText mConfirmPswET;
    @ViewInject(R.id.btn_confirm_change)
    private Button mConfirmChange;

    private Context mContext;
    private String mOldPwd;
    private String mNewPwd;
    private String mConfirmPwd;

    private static final int HANDLER_CHANGE_SUCCESS = 0;

    /**
     * Handler
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_CHANGE_SUCCESS:
                    //修改完后  需要重新登录
                    Intent mIntent = new Intent(mContext, LoginActivity.class);
                    mIntent.putExtra("phone_num", UserData.getInstance().getPhoneNumber());
                    startActivity(mIntent);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ViewUtils.inject(this);
        mContext = this;
    }

    private boolean checkInfo() {
        mOldPwd = mOldPswET.getText().toString().trim();
        mNewPwd = mNewPswET.getText().toString().trim();
        mConfirmPwd = mConfirmPswET.getText().toString().trim();
        if(StringUtils.isBlank(mOldPwd)) {
            CommonToast.showHintDialog(mContext,"请输入原密码！");
            return false;
        }
        if(StringUtils.isBlank(mNewPwd)) {
            CommonToast.showHintDialog(mContext,"请输入新密码！");
            return false;
        }
        if(!mNewPwd.equals(mConfirmPwd)) {
            CommonToast.showHintDialog(mContext,"两次密码不一致！");
            return false;
        }
        return true;
    }

    public void changePsw() {
        SenderResultModel resultModel = ParamsManager.senderChangeLoginPwd(mOldPwd, mNewPwd, mConfirmPwd);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("修改登录密码 " + responeJson);
                setResult(RESULT_OK);
                CommonToast.makeCustomText(mContext, "修改成功，请重新登录！");
                mConfirmChange.setEnabled(false);
                handler.sendEmptyMessageDelayed(HANDLER_CHANGE_SUCCESS, 3000);
                UserData.getInstance().setUSERID("");
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("修改登录密码 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

    @OnClick({R.id.btn_left, R.id.btn_confirm_change})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_confirm_change:
                if(checkInfo()) {
                    changePsw();
                }
                break;
        }
    }
}
