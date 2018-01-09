package com.ql.jcjr.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseFragment;
import com.ql.jcjr.entity.MineFragmentEntity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.SharedPreferencesUtils;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.CircleImageView;
import com.ql.jcjr.view.CommonToast;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class MoreFragment extends BaseFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

//    @ViewInject(R.id.rl_not_login)
//    private RelativeLayout mRlContainer;
//    @ViewInject(R.id.ll_not_login)
//    private LinearLayout mLlNotLogin;
//    @ViewInject(R.id.ll_login_info)
//    private LinearLayout mLlLoginInfo;
//    @ViewInject(R.id.ll_login)
//    private LinearLayout mLlLogin;
    @ViewInject(R.id.ll_not_auth)
//    private LinearLayout mLlNotAuth;
//    @ViewInject(R.id.ll_already_auth)
    private LinearLayout mLlAuth;
    @ViewInject(R.id.user_icon)
    private CircleImageView mCivUserIcon;
//    @ViewInject(R.id.tv_phone)
//    private TextView mTvTel;
    @ViewInject(R.id.tv_user_name)
    private TextView mTvUserName;

    private Context mContext;
    private View mView;

    private static final int CODE_REAL_NAME = 0;

    @Override
    protected int getContentView() {
        return R.layout.fragment_more;
    }

    @Override
    protected void initView(View view) {
        ViewUtils.inject(this, view);
        mContext = getActivity();
        SharedPreferencesUtils.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
//        mLlNotLogin.setVisibility(View.VISIBLE);
//        mLlLogin.setVisibility(View.GONE);
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
//        isLoginOrNot();
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if(isVisible) {
            isLoginOrNot();
        }
    }

    private void getUserData() {
        SenderResultModel resultModel = ParamsManager.senderMineFragment();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("我的页面 " + responeJson);
                MineFragmentEntity entity = GsonParser.getParsedObj(responeJson, MineFragmentEntity.class);
                MineFragmentEntity.ResultBean resultBean = entity.getResult();

//                mLlNotLogin.setVisibility(View.GONE);
//                mLlLogin.setVisibility(View.VISIBLE);
//                mTvTel.setText(resultBean.getUsername());
                UserData.getInstance().setUserIconUrl(resultBean.getHeadImgUrl());
                GlideUtil.displayPic(mContext,resultBean.getHeadImgUrl(), R.drawable.default_user_icon, mCivUserIcon);

                if(StringUtils.isBlank(resultBean.getRealname())) {
//                    mLlNotAuth.setVisibility(View.VISIBLE);
                    mLlAuth.setVisibility(View.GONE);
                    UserData.getInstance().setRealName("");
                }else {
//                    mLlNotAuth.setVisibility(View.GONE);
                    mLlAuth.setVisibility(View.VISIBLE);
                    mTvUserName.setText(resultBean.getRealname());
                    UserData.getInstance().setRealName(resultBean.getRealname());
                }

            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("我的页面失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, mContext);
    }


//    @OnClick({R.id.tv_service, R.id.tv_login_regist, R.id.tv_common_issue, R.id.tv_about_us, R.id.tv_to_auth, R.id.tv_praise, R.id.ll_login_info, R.id.iv_message})
//    public void onClick(View v) {
//        Intent intent = new Intent();
//        switch (v.getId()) {
//            case R.id.tv_to_auth:
//                intent.setClass(mContext, RealNameActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.tv_service:
//                intent.setClass(mContext, ContactUsActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.tv_login_regist:
//                intent.setClass(mContext, LoginActivityCheck.class);
//                startActivity(intent);
////                startActivityForResult(intent, CODE_LOGIN);
//                break;
//            case R.id.tv_common_issue:
//                UrlUtil.showHtmlPage(mContext,"常见问题", AppConfig.COMMON_PROBLEM_URL);
//                break;
//            case R.id.tv_about_us:
//                UrlUtil.showHtmlPage(mContext,"关于我们", AppConfig.ABOUT_US_URL);
//                break;
//            case R.id.ll_login_info:
//                intent.setClass(mContext, RealNameActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.iv_message:
//                intent.setClass(mContext, MessageCenterActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.tv_praise:
//                try{
//                    Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
//                    Intent intentPraise = new Intent(Intent.ACTION_VIEW,uri);
//                    intentPraise.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intentPraise);
//                }catch(Exception e){
//                    Toast.makeText(mContext, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//                break;
//        }
//    }

    private void isLoginOrNot() {
        if (UserData.getInstance().isLogin()) {
            getUserData();
        } else {
//            mLlNotLogin.setVisibility(View.VISIBLE);
//            mLlLogin.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        LogUtil.i("1111111111111111111 " + key);
        if (key.equals(SharedPreferencesUtils.KEY_USER_ID) || key.equals(SharedPreferencesUtils.KEY_USER_ICON_URL) || key.equals(SharedPreferencesUtils.KEY_REAL_NAME)) {
            LogUtil.i("222222222222222222 " + isAdded());
            if (isAdded()) {
                LogUtil.i("33333333333333333333333333333 ");
                isLoginOrNot();
            }
        }
    }
}
