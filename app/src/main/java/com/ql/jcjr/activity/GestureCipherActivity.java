package com.ql.jcjr.activity;

import android.content.Context;
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
import com.ql.jcjr.view.gesturecipher.GestureLockViewGroup;

import java.util.ArrayList;
import java.util.List;

public class GestureCipherActivity extends BaseActivity implements
        GestureLockViewGroup.OnGestureLockViewListener {

    @ViewInject(R.id.iv_gesture_view)
    private GestureLockViewGroup mGestureView;
    @ViewInject(R.id.tv_indicator)
    private TextView mTvIndicator;
//    @ViewInject(R.id.tv_phone_num)
//    private TextView mTvPhoneNum;
    @ViewInject(R.id.user_icon)
    private CircleImageView mUserIcon;

    private Context mContext;
    private String mUserIconUrl;

    private List<Integer> mFirstInput = new ArrayList<>();
    private List<Integer> mSecondInput = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_cipher);

        ViewUtils.inject(this);
        mContext = this;

        init();
    }

    private void init() {
        mUserIconUrl = getIntent().getStringExtra("user_icon_url");
        GlideUtil.displayPic(mContext, mUserIconUrl, R.drawable.gesture_user_icon, mUserIcon);
//        mTvPhoneNum.setText(StringUtils.getHidePhoneNum(UserData.getInstance().getPhoneNumber()));
        mGestureView.setOnGestureLockViewListener(this);
    }

    @OnClick({R.id.btn_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }

    @Override
    public void onBlockSelected(int cId) {

    }

    @Override
    public void onGestureEvent(boolean matched) {

    }

    @Override
    public void onUnmatchedExceedBoundary() {

    }

    @Override
    public void onFinshInput(List<Integer> mChoose, int tryTimes) {

        if (mFirstInput.size() == 0) {

            if(mChoose.size() < 4) {
                mTvIndicator.setText("至少连接4个点，请重新绘制");
                mTvIndicator.setTextColor(getResources().getColor(R.color.red));
                mGestureView.reset();
                return;
            }

            mFirstInput.addAll(mChoose);
            mTvIndicator.setText("请再次输入手势密码");
            mTvIndicator.setTextColor(getResources().getColor(R.color.font_pink_light));
            mGestureView.reset();
            return;
        }
        mSecondInput.clear();
        mSecondInput.addAll(mChoose);
        if (mGestureView.checkAnswer(mSecondInput.toString(), mFirstInput.toString())) {
            UserData.getInstance().setGestureCipher(mFirstInput.toString());
            UserData.getInstance().setIsOpenGesture(true);
            finish();
        } else {
            mTvIndicator.setText("两次绘制不一致，请重新绘制");
            mTvIndicator.setTextColor(getResources().getColor(R.color.red));
        }

        mGestureView.reset();


    }
}
