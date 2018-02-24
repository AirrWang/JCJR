package com.ql.jcjr.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.ql.jcjr.R;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.utils.DisplayUnitUtils;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.utils.ScreenShotListenManager;
import com.ql.jcjr.utils.ShareHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class BaseActivity extends FragmentActivity{

    private ScreenShotListenManager manager;
    private Context context;
    private PopupWindow popupWindow;
    private ImageView iv;
    private ShareHelper mShare;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
           if (popupWindow.isShowing()&&popupWindow!=null){
               popupWindow.dismiss();
           }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
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

        mShare = new ShareHelper(this);
        initPop();
    }

    private void initPop() {
        manager = ScreenShotListenManager.newInstance(this);
        popupWindow = new PopupWindow(context);
        View popView= LayoutInflater.from(context).inflate(R.layout.pop_view,null);
        popupWindow.setContentView(popView);
        popupWindow.setWidth(DisplayUnitUtils.getDisplayWidth(this));
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        iv = (ImageView) popView.findViewById(R.id.iv_2);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        Button button= (Button) popView.findViewById(R.id.btn_share);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShare.share();
                popupWindow.dismiss();
            }
        });
        manager.setListener(
                new ScreenShotListenManager.OnScreenShotListener() {

                    public void onShot(String imagePath) {
                        //TODO
                        FileInputStream fis = null;
                        try {
                            fis = new FileInputStream(imagePath);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Bitmap bitmap  = BitmapFactory.decodeStream(fis);
                        mShare.setShareImageView(bitmap);
                        GlideUtil.displayPic(context,imagePath,R.color.white, iv);
                        showPop();
                    }
                }
        );
    }

    private void showPop() {
        popupWindow.showAtLocation(this.findViewById(android.R.id.content), Gravity.TOP,0,0);
        handler.postDelayed(runnable,6000);
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
        manager.startListen();
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        manager.stopListen();
        popupWindow.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JcbApplication.getInstance().removeActivity(this);
        manager.stopListen();
        popupWindow.dismiss();
    }

}
