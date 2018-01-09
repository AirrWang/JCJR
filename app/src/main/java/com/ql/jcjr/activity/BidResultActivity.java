package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.utils.NotificationsUtils;
import com.ql.jcjr.view.ActionBar;
import com.ql.jcjr.view.CommonToast;

public class BidResultActivity extends BaseActivity {

    @ViewInject(R.id.ab_header)
    private ActionBar mActionBar;

    @ViewInject(R.id.ll_result)
    private LinearLayout mLlResult;

    @ViewInject(R.id.tv_tel)
    private TextView mTvTel;
    @ViewInject(R.id.iv_result)
    private ImageView mIvResult;
    @ViewInject(R.id.tv_result)
    private TextView mTvResult;
    @ViewInject(R.id.tv_des)
    private TextView mTvDes;

    private Context mContext;
    private boolean isBidSuccess;
    private String mResultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_result);

        ViewUtils.inject(this);
        mContext = this;

        mActionBar.setTitle("");

        isBidSuccess = getIntent().getBooleanExtra("isSuccess", false);
        mResultText = getIntent().getStringExtra("result_text");
        initView();
    }

    private void initView() {
        if(isBidSuccess) {

            mLlResult.setBackgroundColor(getResources().getColor(R.color.btn_main));

            mActionBar.setBackgroundColor(getResources().getColor(R.color.btn_main));
            mIvResult.setImageResource(R.drawable.ic_bid_success);
//            mTvResult.setText("投标成功");
            mTvDes.setText("投标成功, 如有疑问请拨打积财金融客服热线");

            //投标成功后检查是否有通知栏权限
            //检查通知栏权限
            if(!NotificationsUtils.isNotificationEnabled(this)){
                CommonToast.showNotificationDialog(this, new CommonToast.IPositiveButtonEvent() {
                    @Override
                    public void oClickEvent() {
                                    requestPermission(100);
                    }
                });
            }

        }else{
            mLlResult.setBackgroundColor(getResources().getColor(R.color.font_black));

            mActionBar.setBackgroundColor(getResources().getColor(R.color.font_black));
            mIvResult.setImageResource(R.drawable.ic_bid_fail);
//            mTvResult.setText("投标失败");
            mTvDes.setText("投标失败, 如有疑问请拨打积财金融客服热线");
            mTvResult.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.dimen_34px));
        }

        mTvResult.setText(mResultText);
    }

    protected void requestPermission(int requestCode) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getPackageName());
            intent.putExtra("app_uid", getApplicationInfo().uid);
            startActivity(intent);
        } else{
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
    }

    @OnClick({R.id.btn_left, R.id.tv_tel, R.id.btn_bid, R.id.btn_recharge})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.tv_tel:
                CommonToast.showDailPrompt(mContext, mTvTel.getText().toString());
                break;
            case R.id.btn_bid:
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_recharge:
                Intent rechargeIntent = new Intent(mContext, RechargeActivity.class);
                startActivity(rechargeIntent);
                break;
        }
    }
}
