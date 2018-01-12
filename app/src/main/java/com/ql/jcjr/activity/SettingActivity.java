package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

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
import com.ql.jcjr.utils.AppConfigCommon;
import com.ql.jcjr.utils.CommonUtils;
import com.ql.jcjr.utils.FileUtil;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.crypt.DesUtil;
import com.ql.jcjr.view.ActionSheet;
import com.ql.jcjr.view.CircleImageView;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.ImageTextHorizontalBarLess;

public class SettingActivity extends BaseActivity {

    public static final int PICK_CODE = 2;
    public static final int TAKE_PHOTO_CODE = 3;
    private static final int CODE_CHANGE_LOGIN_PWD = 0;
    private static final int CODE_GESTURE = 1;
//    @ViewInject(R.id.ithb_avatar)
//    private ImageTextHorizontalBar mIthbAvatar;
    @ViewInject(R.id.ithb_bind_mobile)
    private ImageTextHorizontalBarLess mIthbMobile;
    @ViewInject(R.id.ithb_real_name)
    private ImageTextHorizontalBarLess mTthbRealName;

    @ViewInject(R.id.ithb_bank)
    private ImageTextHorizontalBarLess mTthbBank;

    @ViewInject(R.id.ithb_account_security)
    private ImageTextHorizontalBarLess mSecurity;

//    @ViewInject(R.id.ithb_login_psw)
//    private ImageTextHorizontalBarLess mTthbLoginPsw;

//    @ViewInject(R.id.ithb_trans_psw)
//    private ImageTextHorizontalBarLess mTthbTransPsw;
//
//    @ViewInject(R.id.ithb_gesture_psw)
//    private ImageTextHorizontalBarLess mTthbGesturePsw;

    @ViewInject(R.id.ithb_about_us)
    private ImageTextHorizontalBarLess mTthbAboutUs;

    private Context mContext;
    private String mIsSetPay;
    private String mUserIconUrl;
    private CircleImageView mCircleImageView;
    private String mTakePhotoPath;
    private Bitmap mPhotoBitmap;

    private boolean hasShiMing = false;
    private boolean hasBindBank = false;
    private boolean hasSetTransPwd = false;
    private boolean needLoadInfo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ViewUtils.inject(this);
        mContext = this;
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(needLoadInfo){
            needLoadInfo = false;
            getMineFragmentData();
        }
//        initGestureInfo();
    }

    private void init() {
        //关于
        mTthbAboutUs.setRightTitleText("v"+ CommonUtils.getAppVersionName());

        mUserIconUrl = getIntent().getStringExtra("user_icon_url");
//        mIthbMobile.setRightTitleText(UserData.getInstance().getPhoneNumber());
//        getMineFragmentData();
    }

    public void getMineFragmentData() {
        SenderResultModel resultModel = ParamsManager.senderMineFragment();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("我的信息 " + responeJson);
                        MineFragmentEntity entity = GsonParser.getParsedObj(responeJson, MineFragmentEntity.class);
                        MineFragmentEntity.ResultBean resultBean = entity.getResult();

                        //手机号
                        mIthbMobile.setRightTitleText(resultBean.getUsername());
                        //实名
                        if (StringUtils.isBlank(resultBean.getRealname())) {
                            hasShiMing = false;
                            mTthbRealName.setRightTitleText("未认证");
                            mTthbRealName.setRightTitleColor(getResources().getColor(R.color.font_grey_three));
                        } else {
                            hasShiMing = true;
                            mTthbRealName.setRightTitleText(resultBean.getRealname());
                            mTthbRealName.setRightTitleColor(getResources().getColor(R.color.font_black));
                        }
                        //银行卡
                        if (StringUtils.isBlank(resultBean.getBank())) {
                            hasBindBank = false;
                            mTthbBank.setRightTitleText("未绑定");
                            mTthbBank.setRightTitleColor(getResources().getColor(R.color.font_grey_three));
                        } else {
                            hasBindBank = true;
                            mTthbBank.setRightTitleText(resultBean.getBank());
                            mTthbBank.setRightTitleColor(getResources().getColor(R.color.font_black));
                        }
                        //交易密码
//                        if(resultBean.getIssetPay().equals("0")){
//                            UserData.getInstance().setHasSetTransPwd(false);
//                            hasSetTransPwd = false;
//                            mTthbTransPsw.setRightTitleText("未设置");
//                            mTthbTransPsw.setRightTitleColor(getResources().getColor(R.color.font_grey_three));
//                        }else{
//                            UserData.getInstance().setHasSetTransPwd(true);
//                            hasSetTransPwd = true;
//                            mTthbTransPsw.setRightTitleText("修改密码");
//                            mTthbTransPsw.setRightTitleColor(getResources().getColor(R.color.font_black));
//                        }

                        UserData.getInstance().setRealName(resultBean.getRealname());
                        UserData.getInstance().setIsSetPay(resultBean.getIssetPay());
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("我的信息失败 " + entity.errorInfo);
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }

                }, mContext);
    }

    private void checkRealName() {
        SenderResultModel resultModel = ParamsManager.senderCheckRealName();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("检查是否实名认证 " + responeJson);
                        CheckRealNameEntity
                                entity =
                                GsonParser.getParsedObj(responeJson, CheckRealNameEntity.class);
                        String status = entity.getResult().getStatus();
                        switch (status) {
                            case Global.STATUS_PASS:
                                mTthbRealName.setRightTitleText("已认证");
                                break;
                            case Global.STATUS_UN_PASS:
                                mTthbRealName.setRightTitleText("未认证");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CODE_CHANGE_LOGIN_PWD:
                finish();
                break;
            case CODE_GESTURE:
                finish();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({
            R.id.btn_left, R.id.ithb_real_name, R.id.ithb_trans_psw, R.id.ithb_bank, R.id.tv_exit, R.id.ithb_about_us, R.id.ithb_feedback,R.id.ithb_account_security
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.ithb_real_name:
                if(!hasShiMing){
                    needLoadInfo = true;
                }
                Intent realNameIntent = new Intent(mContext, RealNameActivity.class);
                startActivity(realNameIntent);
                break;
//            case R.id.ithb_login_psw:
//                Intent loginPswIntent = new Intent(mContext, ChangePasswordActivity.class);
//                startActivityForResult(loginPswIntent, CODE_CHANGE_LOGIN_PWD);
//                break;
//            case R.id.ithb_trans_psw:
//                if(!hasSetTransPwd){
//                    needLoadInfo = true;
//                }
//                Intent transPswIntent = new Intent();
//                switch (UserData.getInstance().getIsSetPay()) {
//                    case "0":
//                        transPswIntent.setClass(mContext, VerifyLoginPwdActivity.class);
//                        startActivity(transPswIntent);
//                        break;
//                    case "1":
//                        transPswIntent.setClass(mContext, VerifyPayPswActivity.class);
//                        startActivity(transPswIntent);
//                        break;
//                }
//                break;
            case R.id.ithb_bank:
                if(!hasBindBank){
                    needLoadInfo = true;
                }
                if(UserData.getInstance().getRealName().length()==0){
                    CommonToast.showShiMingDialog(mContext);
                }
                else{
                    Intent bankIntent = new Intent(mContext, BindBankCardActivity.class);
                    startActivity(bankIntent);
                }
                break;
            case R.id.ithb_account_security:
                Intent securityIntent = new Intent(mContext, AccountSecurityActivity.class);
                startActivityForResult(securityIntent,CODE_CHANGE_LOGIN_PWD);
                break;
            case R.id.tv_exit:
                showExitDialog();
                break;
//            case R.id.ithb_gesture_psw:
//                Intent gestureIntent = new Intent(mContext, SettingGestureActivity.class);
//                gestureIntent.putExtra("user_icon_url", mUserIconUrl);
//                startActivityForResult(gestureIntent, CODE_GESTURE);
//                break;

            case R.id.ithb_about_us:
                Intent aboutUsIntent = new Intent(mContext, AboutUsActivity.class);
                startActivity(aboutUsIntent);
                break;

            case  R.id.ithb_feedback:
                Intent feedBackIntent = new Intent(mContext, FeedbackActivity.class);
                startActivity(feedBackIntent);
                break;
        }
    }

    private final String PSW_FILE_NAME = "wjthnfkghj";
    private void savePswString(String password) {
        String encrypt = "";
        try {
            byte[] encrypted =
                    DesUtil.encrypt(password.getBytes("utf-8"), AppConfigCommon.ENCRYPT_KEY.getBytes());
            encrypt = Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileUtil.writeObjectToDataFile(mContext, encrypt, PSW_FILE_NAME);
    }

    private void showExitDialog() {
        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.exit_dialog, null);
        Button btnExit = (Button) view.findViewById(R.id.btn_exit);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        final ActionSheet dialog = new ActionSheet(mContext, ActionSheet.GRAVITY_BOTTOM);
        dialog.setContentView(view);
        dialog.show();

        //退出
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserData.getInstance().setUSERID("");
                savePswString("");
                finish();
            }
        });

        //取消
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
