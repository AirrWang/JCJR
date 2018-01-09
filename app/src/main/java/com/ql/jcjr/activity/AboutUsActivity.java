package com.ql.jcjr.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.AppConfig;
import com.ql.jcjr.utils.CommonUtils;
import com.ql.jcjr.utils.UrlUtil;
import com.ql.jcjr.view.ImageTextHorizontalBarLess;

public class AboutUsActivity extends BaseActivity {

    private Context mContext;

    @ViewInject(R.id.tv_praise)
    private ImageTextHorizontalBarLess mTvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ViewUtils.inject(this);
        mContext = this;
        init();
    }

    private void init() {
        mTvVersion.setRightTitleText("v"+CommonUtils.getAppVersionName());
    }

    @OnClick({R.id.btn_left, R.id.tv_official, R.id.tv_about_us, R.id.tv_praise})
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.tv_official:
                UrlUtil.showHtmlPage(mContext,"官方网站", AppConfig.OFFICIAL_WEBSITE_URL);
                break;
            case R.id.tv_about_us:
                UrlUtil.showHtmlPage(mContext,"关于积财", AppConfig.ABOUT_US_URL);
                break;
            case R.id.tv_praise:
                try{
                    Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
                    Intent intentPraise = new Intent(Intent.ACTION_VIEW,uri);
                    intentPraise.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentPraise);
                }catch(Exception e){
                    Toast.makeText(mContext, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
        }
    }
}
