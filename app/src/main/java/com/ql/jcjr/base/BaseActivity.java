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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.ql.jcjr.R;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.constant.AppConfig;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.utils.ScreenShotListenManager;
import com.ql.jcjr.utils.ShareHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import java.io.File;


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
               Log.d("dimiss","timeover");
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
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        iv = (ImageView) popView.findViewById(R.id.iv_2);

        Button button= (Button) popView.findViewById(R.id.btn_share);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShare.share();
                popupWindow.dismiss();
            }
        });
        Button btn_tokf= (Button) popView.findViewById(R.id.btn_tokf);
        btn_tokf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                String title = "积财客服";
                ConsultSource source = new ConsultSource(AppConfig.OFFICIAL_WEBSITE_URL, "积财金融", "Android");
                source.productDetail = null;
                Unicorn.openServiceActivity(context, title, source);
            }
        });
        manager.setListener(
                new ScreenShotListenManager.OnScreenShotListener() {

                    public void onShot(String imagePath) {
                        Bitmap bitmap  = BitmapFactory.decodeFile(imagePath);
                        File file=new File(imagePath);
                        GlideUtil.displayPic(context,file,R.color.white,iv);
                        mShare.setShareImageView(bitmap);
                        showPop(bitmap);
                    }
                }
        );
    }

    private void showPop(Bitmap bitmap) {
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
        if (popupWindow.isShowing()&&popupWindow!=null){
            Log.d("dimiss","onPause");
            popupWindow.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JcbApplication.getInstance().removeActivity(this);
        manager.stopListen();
        if (popupWindow.isShowing()&&popupWindow!=null){
            Log.d("dimiss","onDestroy");
            popupWindow.dismiss();
        }
    }

}
