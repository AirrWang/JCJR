package com.ql.jcjr.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

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
import com.ql.jcjr.view.PwdEditText;

public class ConfirmPayPwdActivity extends BaseActivity implements
        PwdEditText.OnInputFinishListener{

    @ViewInject(R.id.et_confirm_pwd)
    private PwdEditText mEtConfirmPwd;

    private Context mContext;
    private String mPayPwd;
    private String oldPayPsw;

    private static final int HANDLER_CHANGE_SUCCESS = 0;

    /**
     * Handler
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_CHANGE_SUCCESS:
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pay_pwd);

        ViewUtils.inject(this);
        mContext = this;

        init();
    }

    private void init() {
        mPayPwd = getIntent().getStringExtra("pay_pwd");
        oldPayPsw = getIntent().getStringExtra("old_pay_psw");
        mEtConfirmPwd.setOnInputFinishListener(this);
    }

    private void setPayPwd(String pwdConfirm) {
        SenderResultModel resultModel = ParamsManager.sendeSetPayPwd(mPayPwd, pwdConfirm, oldPayPsw);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("设置支付密码 " + responeJson);
                setResult(RESULT_OK);
                if(StringUtils.isBlank(oldPayPsw)){
                    CommonToast.makeCustomText(mContext, "设置成功");
                }else{
                    CommonToast.makeCustomText(mContext, "修改成功");
                }

                handler.sendEmptyMessageDelayed(HANDLER_CHANGE_SUCCESS, 3000);
                UserData.getInstance().setIsSetPay("1");
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("设置支付密码 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }



    @OnClick({R.id.btn_left})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_left:
                finish();
                break;
        }
    }

    @Override
    public void onInputFinish(String password) {
        setPayPwd(password);
    }
}
