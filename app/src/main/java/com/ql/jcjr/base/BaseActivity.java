package com.ql.jcjr.base;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ql.jcjr.application.JcbApplication;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class BaseActivity extends FragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //设置5.0以上系统的状态栏，去掉黑色半透明，变成全透明,但是布局范围会延伸到状态栏
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                window.setStatusBarColor(Color.TRANSPARENT);
//            }
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarTransparent();
        }
        JcbApplication.getInstance().addActivity(this);
        //友盟推送
        PushAgent.getInstance(this).onAppStart();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarTransparent(){
        //设置5.0以上系统的状态栏，去掉黑色半透明，变成全透明,但是布局范围会延伸到状态栏
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    public void setStatusBarColor(boolean isTransparent, int color){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(isTransparent){
                setStatusBarTransparent();
            }
            else{
                Window window = getWindow();
                window.setStatusBarColor(color);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        JcbApplication.getInstance().setCurrentActivity(this);
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JcbApplication.getInstance().removeActivity(this);
    }

}
