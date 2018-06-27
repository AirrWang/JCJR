package com.ql.jcjr.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends FragmentActivity{

    private ScreenShotListenManager manager;
    private Context context;
    private PopupWindow popupWindow;
    private ImageView iv;
    private ShareHelper mShare;
    /** 内部存储器内容观察者 */
    private MediaContentObserver mInternalObserver;

    /** 外部存储器内容观察者 */
    private MediaContentObserver mExternalObserver;

    /** 运行在 UI 线程的 Handler, 用于运行监听器回调 */
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());

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

        mShare = new ShareHelper(this,null);
        initPop();
    }

    /**
     * 媒体内容观察者(观察媒体数据库的改变)
     */
    private class MediaContentObserver extends ContentObserver {

        public MediaContentObserver(Uri contentUri, Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){

                }else {
                    insertDummyContactWrapper();
                }
            }
        }
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
        // 创建内容观察者
        mInternalObserver = new MediaContentObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI, mUiHandler);
        mExternalObserver = new MediaContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mUiHandler);

        // 注册内容观察者
        context.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                false,
                mInternalObserver
        );
        context.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                false,
                mExternalObserver
        );
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        manager.stopListen();
        if (popupWindow.isShowing()&&popupWindow!=null){
            Log.d("dimiss","onPause");
            popupWindow.dismiss();
        }
        stop();


    }

    private void stop() {
        // 注销内容观察者
        if (mInternalObserver != null) {
            try {
                context.getContentResolver().unregisterContentObserver(mInternalObserver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mInternalObserver = null;
        }
        if (mExternalObserver != null) {
            try {
                context.getContentResolver().unregisterContentObserver(mExternalObserver);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mExternalObserver = null;
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
        stop();
    }
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    @TargetApi(Build.VERSION_CODES.M)
    private boolean insertDummyContactWrapper() {
        //提示用户需要手动开启的权限集合
        List<String> permissionsNeeded = new ArrayList<String>();

        //功能所需权限的集合
        final List<String> permissionsList = new ArrayList<String>();

        //若用户拒绝了该权限申请，则将该申请的提示添加到“用户需要手动开启的权限集合”中
//        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
//            permissionsNeeded.add("保存应用数据");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("读取应用数据");
//        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
//            permissionsNeeded.add("获取当前地理位置");
//        if (!addPermission(permissionsList, Manifest.permission.GET_ACCOUNTS))
//            permissionsNeeded.add("获取通讯录信息");
//        if (!addPermission(permissionsList, Manifest.permission.SYSTEM_ALERT_WINDOW))
//            permissionsNeeded.add("获取应用提示");
//        if(!addPermission(permissionsList,Manifest.permission.USE_FINGERPRINT))
//            permissionsNeeded.add("获取手机指纹信息");
//        if(!addPermission(permissionsList, Manifest.permission.CAMERA))
//            permissionsNeeded.add("获取手机相机信息");
//        if(!addPermission(permissionsList,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS))
//            permissionsNeeded.add("读取手机存储信息");


        //存在未配置的权限
        if (permissionsList.size() > 0) {

            //若用户赋之前拒绝过一部分权限，则需要提示用户开启其余权限并返回，否则该功能将无法执行
            if (permissionsNeeded.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                // Need Rationale
//                String message = "我们需要您授权下列权限：\n";
//                for (int i = 0; i < permissionsNeeded.size(); i++)
//                    message = message + "\n" + permissionsNeeded.get(i);
//
//                //弹出对话框，提示用户需要手动开启的权限
//                try {
//                    final CommonDialog.Builder dialog = new CommonDialog.Builder(this);
//                    dialog.setTitle(message);
//                    dialog.setMessageSize(R.dimen.f03_34);
//                    dialog.setButtonTextSize(R.dimen.f03_34);
//                    dialog.setPositiveButton("确定",
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//
//                                }
//                            });
//                    dialog.create().show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }

            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return false;
        }else {
            return true;
        }

    }
    //判断用户是否授予了所需权限
    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        //若配置了该权限，返回true
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            //若未配置该权限，将其添加到所需权限的集合，返回true
            permissionsList.add(permission);
            // 若用户勾选了“永不询问”复选框，并拒绝了权限，则返回false
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }

        return true;
    }

    /**
     * 点击空白区域隐藏键盘.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
