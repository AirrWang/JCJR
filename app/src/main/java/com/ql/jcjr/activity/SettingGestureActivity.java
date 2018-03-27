package com.ql.jcjr.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.CircleImageView;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.ImageTextHorizontalBarLess;
import com.ql.jcjr.view.gesturecipher.GestureLockViewGroup;

import java.util.List;

public class SettingGestureActivity extends BaseActivity implements
        CompoundButton.OnCheckedChangeListener {

    @ViewInject(R.id.switch_gesture)
    private Switch mSwitch;
    @ViewInject(R.id.divider)
    private View mDivider;
    @ViewInject(R.id.ll_setting)
    private ImageTextHorizontalBarLess mLlSetting;
//    @ViewInject(R.id.tv_setting_type)
//    private TextView mSettingType;
    @ViewInject(R.id.tv_des)
    private TextView mTvDes;

    private Context mContext;
    private boolean mHasSet = false;
    private boolean isMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_gesture);
        ViewUtils.inject(this);

        mContext = this;
        initView();
    }

    private void initView() {
        mSwitch.setOnCheckedChangeListener(this);
        getGesturePwd();
        boolean isSetGesture = UserData.getInstance().getIsOpenGesture();
        if (isSetGesture) {
            mSwitch.setChecked(true);
            mDivider.setVisibility(View.VISIBLE);
            mLlSetting.setVisibility(View.VISIBLE);
            mTvDes.setText("已开启，进入积财金融需验证手势密码");
        } else {
            mSwitch.setChecked(false);
            UserData.getInstance().setIsOpenGesture(false);
            mDivider.setVisibility(View.GONE);
            mLlSetting.setVisibility(View.GONE);
            mTvDes.setText("开启后，进入积财金融需验证手势密码");
        }


    }

    private void getGesturePwd() {

        String answer = UserData.getInstance().getGestureCipher();
        if (StringUtils.isBlank(answer)) {
//            mSettingType.setText("设置手势密码");
            mHasSet = false;
        } else {
//            mSettingType.setText("修改手势密码");
            mHasSet = true;
        }
    }

    @OnClick({R.id.btn_left, R.id.ll_setting})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.ll_setting:
//                if (mHasSet) {
//                    Intent intent = new Intent(mContext, VerifyGestureActivity.class);
//                    intent.putExtra("user_icon_url", getIntent().getStringExtra("user_icon_url"));
//                    startActivityForResult(intent, 1);
//                } else {
//                    Intent intent = new Intent(mContext, GestureCipherActivity.class);
//                    intent.putExtra("user_icon_url", getIntent().getStringExtra("user_icon_url"));
//                    startActivityForResult(intent, 0);
//                }

                Intent intent = new Intent(mContext, VerifyGestureActivity.class);
                startActivityForResult(intent, 1);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 0:
                getGesturePwd();
                mSwitch.setChecked(true);
                UserData.getInstance().setIsOpenGesture(true);
                mDivider.setVisibility(View.VISIBLE);
                mLlSetting.setVisibility(View.VISIBLE);
                mTvDes.setText("已开启，进入积财金融需验证手势密码");
                break;
            case 1:
                setResult(RESULT_OK);
                finish();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (!mHasSet) {
                mSwitch.setChecked(false);
                Intent intent = new Intent(mContext, GestureCipherActivity.class);
                intent.putExtra("user_icon_url", getIntent().getStringExtra("user_icon_url"));
                startActivityForResult(intent, 0);
            } else {
                UserData.getInstance().setIsOpenGesture(true);
                mDivider.setVisibility(View.VISIBLE);
                mLlSetting.setVisibility(View.VISIBLE);
                mTvDes.setText("已开启，进入积财金融需验证手势密码");
            }

        } else {
            //TODO
            showGestureDialog();

        }
    }


    private void showGestureDialog() {
        mSwitch.setChecked(true);
        final Dialog dialog = new Dialog(this, R.style.DialogFullScreen);
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.gesture_dialog, null);
        dialog.setContentView(view);
//        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        CircleImageView mCircleImageView = (CircleImageView) view.findViewById(R.id.user_icon);
        TextView mTvTel = (TextView) view.findViewById(R.id.tv_phone_num);
        final TextView mTvIndicator = (TextView) view.findViewById(R.id.tv_indicator);
        final GestureLockViewGroup mGestureView =
                (GestureLockViewGroup) view.findViewById(R.id.gesture_view);
        TextView mTvLogin = (TextView) view.findViewById(R.id.tv_login);
        mTvLogin.setVisibility(View.GONE);
        mGestureView.setUnMatchExceedBoundary(5);
        GlideUtil.displayPic(mContext, UserData.getInstance().getUserIconUrl(),
                R.drawable.gesture_user_icon, mCircleImageView);
        mTvTel.setText(""+StringUtils.getHidePhoneNum(UserData.getInstance().getPhoneNumber()));

        mGestureView.setOnGestureLockViewListener(
                new GestureLockViewGroup.OnGestureLockViewListener() {
                    @Override
                    public void onBlockSelected(int cId) {

                    }

                    @Override
                    public void onGestureEvent(boolean matched) {

                    }

                    @Override
                    public void onUnmatchedExceedBoundary() {
                        if(!isMatch) {
                            dialog.dismiss();
                            CommonToast.makeCustomText(mContext, "解锁失败,暂时无法关闭");
                            finish();
                        }
                    }

                    @Override
                    public void onFinshInput(List<Integer> mChoose, int tryTimes) {
                        if (mGestureView.checkAnswer(mChoose.toString(), UserData.getInstance().getGestureCipher())) {
                            isMatch = true;
                            dialog.dismiss();
                            UserData.getInstance().setIsOpenGesture(false);
                            mDivider.setVisibility(View.GONE);
                            mLlSetting.setVisibility(View.GONE);
                            mTvDes.setText("开启后，进入积财金融需验证手势密码");
                            finish();
                        } else {
                            isMatch = false;
                            mTvIndicator.setText("绘制错误，还可以输入" + tryTimes + "次");
                            mTvIndicator.setTextColor(getResources().getColor(R.color.red));
                        }

                        mGestureView.reset();
                    }
                });

//        mTvLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                UserData.getInstance().setUSERID("");
//                UserData.getInstance().setIsOpenGesture(false);
//                UserData.getInstance().setGestureCipher("");
//                Intent intent = new Intent(mContext, LoginActivityCheck.class);
//                startActivity(intent);
//            }
//        });
    }
}
