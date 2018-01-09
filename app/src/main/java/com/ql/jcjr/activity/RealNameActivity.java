package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.entity.CheckRealNameEntity;
import com.ql.jcjr.entity.MineFragmentEntity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.ImageTextHorizontalBarLess;

public class RealNameActivity extends BaseActivity {

    @ViewInject(R.id.ll_not_auth)
    private LinearLayout mLlNotCertified;
    @ViewInject(R.id.ll_auth)
    private LinearLayout mLlCertified;
    @ViewInject(R.id.btn_auth)
    private Button mBtnBind;
    @ViewInject(R.id.ithb_real_name)
    private ImageTextHorizontalBarLess mIthbRealName;
    @ViewInject(R.id.ithb_pid)
    private ImageTextHorizontalBarLess mIthbPid;
    @ViewInject(R.id.et_real_name)
    private EditText mEtRealName;
    @ViewInject(R.id.et_pid)
    private EditText mEtPid;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name);

        ViewUtils.inject(this);
        mContext = this;
        init();
    }

    private void init() {
        mLlNotCertified.setVisibility(View.GONE);
        mBtnBind.setVisibility(View.GONE);
        mLlCertified.setVisibility(View.GONE);
        checkRealName();
    }

    private void checkRealName() {
        SenderResultModel resultModel = ParamsManager.senderCheckRealName();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("检查是否实名认证 " + responeJson);
                CheckRealNameEntity entity = GsonParser.getParsedObj(responeJson, CheckRealNameEntity.class);
                String status = entity.getResult().getStatus();
                switch (status){
                    case Global.STATUS_PASS:
                        getUserData(false);
                        break;
                    case Global.STATUS_UN_PASS:
                        mLlNotCertified.setVisibility(View.VISIBLE);
                        mBtnBind.setVisibility(View.VISIBLE);
                        mLlCertified.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("检查是否实名认证 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

    private void getUserData(final boolean needBindBank) {
        SenderResultModel resultModel = ParamsManager.senderMineFragment();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("实名数据 " + responeJson);
                MineFragmentEntity entity = GsonParser.getParsedObj(responeJson, MineFragmentEntity.class);
                MineFragmentEntity.ResultBean resultBean = entity.getResult();
                mIthbRealName.setTitleDescriptionText(resultBean.getRealname());
                mIthbPid.setTitleDescriptionText(resultBean.getCardId());
                mLlNotCertified.setVisibility(View.GONE);
                mBtnBind.setVisibility(View.GONE);
                mLlCertified.setVisibility(View.VISIBLE);
                UserData.getInstance().setRealName(resultBean.getRealname());
                if(needBindBank){
                    Intent bankIntent = new Intent(mContext, BindBankCardActivity.class);
                    startActivity(bankIntent);
                    finish();
                }
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("实名数据 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

    private boolean checkInfo() {
        if (StringUtils.isBlank(mEtRealName.getText().toString().trim())){
            CommonToast.showHintDialog(mContext, "请输入中文姓名");
            return false;
        }
        if (StringUtils.isBlank(mEtPid.getText().toString().trim())){
            CommonToast.showHintDialog(mContext, "身份证号");
            return false;
        }
        return true;
    }

    private void realName(String pid, String pname) {
        SenderResultModel resultModel = ParamsManager.senderRealName(pid, pname);

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("实名认证 " + responeJson);
                getUserData(true);
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("实名认证 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }

    @OnClick({R.id.btn_left, R.id.btn_auth})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_auth:
                if(checkInfo()) {
                    realName(mEtPid.getText().toString().trim(),mEtRealName.getText().toString().trim());
                }
                break;
        }
    }
}
