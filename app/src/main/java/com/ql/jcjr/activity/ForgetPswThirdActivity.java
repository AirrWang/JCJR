package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.CancelEditText;
import com.ql.jcjr.view.CommonToast;

public class ForgetPswThirdActivity extends BaseActivity {

    @ViewInject(R.id.cet_psw)
    private CancelEditText mCetPsw;
    @ViewInject(R.id.btn_change_pwd)
    private Button mBtnChange;


    private Context mContext;
    private String mPhoneNum;

    private static final int HANDLER_CHANGE_SUCCESS = 0;

    /**
     * Handler
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_CHANGE_SUCCESS:
                    Intent mIntent = new Intent(mContext, LoginActivity.class);
                    startActivity(mIntent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw_third);

        ViewUtils.inject(this);

        mContext = this;
        mPhoneNum = getIntent().getStringExtra("phone_num");
    }

    private boolean checkInfo() {
        if(StringUtils.isBlank(mCetPsw.getEditTextContent())) {
            CommonToast.showHintDialog(mContext,"请输入新密码");
            return false;
        }
        return true;
    }

    private void change(String phone, String psw) {
        SenderResultModel resultModel = ParamsManager.senderFindPsw(phone, psw);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("找回密码 " + responeJson);
                CommonToast.makeCustomText(mContext, "修改成功");
                mBtnChange.setEnabled(false);
                handler.sendEmptyMessageDelayed(HANDLER_CHANGE_SUCCESS, 2000);
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("找回密码失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, this);
    }

    @OnClick({R.id.btn_left, R.id.btn_change_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_change_pwd:
                if(checkInfo()) {
                    change(mPhoneNum, mCetPsw.getEditTextContent());
                }
                break;
        }
    }
}
