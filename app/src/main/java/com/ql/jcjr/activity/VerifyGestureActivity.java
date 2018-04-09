package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.view.CircleImageView;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.gesturecipher.GestureLockViewGroup;

import java.util.List;

public class VerifyGestureActivity extends BaseActivity implements
        GestureLockViewGroup.OnGestureLockViewListener{

    @ViewInject(R.id.iv_gesture_view)
    private GestureLockViewGroup mGestureView;
    @ViewInject(R.id.tv_indicator)
    private TextView mTvIndicator;
    @ViewInject(R.id.user_icon)
    private CircleImageView mUserIcon;

    private Context mContext;
    private String mAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_gesture);
        ViewUtils.inject(this);
        mContext = this;
        init();
    }

    private void init() {
        mGestureView.setOnGestureLockViewListener(this);
        GlideUtil.displayPic(mContext, UserData.getInstance().getUserIconUrl(), R.drawable.gesture_user_icon, mUserIcon);

        mAnswer = UserData.getInstance().getGestureCipher();
    }

    @Override
    public void onBlockSelected(int cId) {

    }

    @Override
    public void onGestureEvent(boolean matched) {

    }

    @Override
    public void onUnmatchedExceedBoundary() {
        if(!isMatch) {
            setResult(RESULT_OK);
            CommonToast.makeCustomText(mContext, "解锁失败，重新登录");
            UserData.getInstance().setUSERID("");
            UserData.getInstance().setIsOpenGesture(false);
            UserData.getInstance().setGestureCipher("");
            Intent intent = new Intent(mContext, LoginActivityCheck.class);
            startActivity(intent);
            finish();
        }

    }

    private boolean isMatch;
    @Override
    public void onFinshInput(List<Integer> mChoose, int tryTimes) {
        isMatch = mGestureView.checkAnswer(mChoose.toString(), mAnswer);
        if(isMatch) {
            Intent intent = new Intent(mContext, GestureCipherActivity.class);
            finish();
            startActivityForResult(intent, 0);
        }else{
            mTvIndicator.setText("绘制错误，还可以输入" +tryTimes + "次");
            mTvIndicator.setTextColor(getResources().getColor(R.color.red));
        }

        mGestureView.reset();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 0:
                finish();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.btn_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }
}
