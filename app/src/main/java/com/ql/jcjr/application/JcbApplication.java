package com.ql.jcjr.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.ql.jcjr.activity.MainActivity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.receiver.NetworkChangedReceiver;
import com.ql.jcjr.utils.DeviceInfoUtil;
import com.ql.jcjr.utils.LogUtil;
import com.tencent.bugly.Bugly;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.uuch.adlibrary.utils.DisplayUtil;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class JcbApplication extends Application {

    public static Context appContext;
    private static JcbApplication sInstance;
    public List<Activity> activityManager; // 管理Activity栈
    private Context currentActivity = null;//当前activity
    private NetworkChangedReceiver networkChangedReceiver = null;
    public String baiDuPushChannelId = "";//存储百度推：注册绑定后接收服务端返回的channelID

    public static boolean needReloadMyInfo = false;

    private PushAgent mPushAgent;
    public PushAgent getPushAgent(){
        return mPushAgent;
    }

    private static Typeface pingFangBoldTypeFace;
    public static Typeface getPingFangBoldTypeFace(){
        if(null == pingFangBoldTypeFace){
            pingFangBoldTypeFace = Typeface.createFromAsset(appContext.getAssets(), "fonts/pf_medium.ttf");
        }
        return pingFangBoldTypeFace;
    }

    private static Typeface pingFangRegularTypeFace;
    public static Typeface getPingFangRegularTypeFace(){
        if(null == pingFangRegularTypeFace){
            pingFangRegularTypeFace = Typeface.createFromAsset(appContext.getAssets(), "fonts/pf_regular.ttf");
        }
        return pingFangRegularTypeFace;
    }

    public static synchronized JcbApplication getInstance() {
        if (sInstance == null || appContext == null) {
            sInstance = new JcbApplication();
            appContext = sInstance.getApplicationContext();
        }
        //该实例是在onCreate事件中实例化的
        return sInstance;

    }

    private ConnectivityManager connectivity;
    public ConnectivityManager getConnectivity() {
        if(null == connectivity){
            connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        return connectivity;
    }

//    public static boolean checkPermission(Context context, String permission) {
//        boolean result = false;
//        if (Build.VERSION.SDK_INT >= 23) {
//            try {
//                Class<?> clazz = Class.forName("android.content.Context");
//                Method method = clazz.getMethod("checkSelfPermission", String.class);
//                int rest = (Integer) method.invoke(context, permission);
//                if (rest == PackageManager.PERMISSION_GRANTED) {
//                    result = true;
//                } else {
//                    result = false;
//                }
//            } catch (Exception e) {
//                result = false;
//            }
//        } else {
//            PackageManager pm = context.getPackageManager();
//            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
//                result = true;
//            }
//        }
//        return result;
//    }
//    public static String getDeviceInfo(Context context) {
//        try {
//            org.json.JSONObject json = new org.json.JSONObject();
//            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
//                    .getSystemService(Context.TELEPHONY_SERVICE);
//            String device_id = null;
//            if (checkPermission(context, android.Manifest.permission.READ_PHONE_STATE)) {
//                device_id = tm.getDeviceId();
//            }
//            String mac = null;
//            FileReader fstream = null;
//            try {
//                fstream = new FileReader("/sys/class/net/wlan0/address");
//            } catch (FileNotFoundException e) {
//                fstream = new FileReader("/sys/class/net/eth0/address");
//            }
//            BufferedReader in = null;
//            if (fstream != null) {
//                try {
//                    in = new BufferedReader(fstream, 1024);
//                    mac = in.readLine();
//                } catch (IOException e) {
//                } finally {
//                    if (fstream != null) {
//                        try {
//                            fstream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (in != null) {
//                        try {
//                            in.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//            json.put("mac", mac);
//            if (TextUtils.isEmpty(device_id)) {
//                device_id = mac;
//            }
//            if (TextUtils.isEmpty(device_id)) {
//                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
//                        android.provider.Settings.Secure.ANDROID_ID);
//            }
//            json.put("device_id", device_id);
//            return json.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private String getMetaValue(String name){
        ApplicationInfo appInfo = null;
        try {
            appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String msg=appInfo.metaData.getString(name);
            LogUtil.i("getMetaValue "+name+" :"+msg);
            return msg;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        LogUtil.i("getMetaValue "+name+" :null");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        PlatformConfig.setWeixin("wxf22b8c0d7c7c9046", "ece139ef283d7be8453b3bc8a2f1ff55");
        PlatformConfig.setQQZone("1106445851", "fxXfEk4nvdmPeAo2");
        PlatformConfig.setSinaWeibo("903029770", "6a9532807816c2264f1e8da58e60f510", "http://www.jicaibaobao.com/newweixin/index.html");

        sInstance = this;
        appContext = getApplicationContext();

        //初始化设备信息
        DeviceInfoUtil.initDeviceInfo(this);
        //打开统计的调试模式
        UMConfigure.setLogEnabled(false);
        //设置统计模式
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        //初始化统计
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "a79231cbde4d7b2d6f6ba2a5f8664e2b");
        //打开分享调式模式
        Config.DEBUG = false;
        //初始化分享
        UMShareAPI.get(this);
        //初始化推送
        mPushAgent = PushAgent.getInstance(this);
//        mPushAgent.setDebugMode(false);
        //设置最多显示通知的条数
        mPushAgent.setDisplayNotificationNumber(2);
        //1分钟内收到同一个应用的多条通知时，不会重复提醒
//        mPushAgent.setMuteDurationSeconds(60*5);
        //应用在前台时 是否显示通知
        mPushAgent.setNotificaitonOnForeground(false);

        //小米Push初始化
        MiPushRegistar.register(this, "2882303761517637479", "5221763775479");
        //华为Push初始化
        HuaWeiRegister.register(this);

        //其他初始化放入线程中
        new InitThread().start();

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
//                Toast.makeText(context, uMessage.custom, Toast.LENGTH_LONG).show();
                LogUtil.i("---UmengNotificationClickHandler  dealWithCustomAction----");
                String msg = uMessage.custom;
                if(null != msg){
                    try{
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("push_tag",true);
                        intent.putExtra("push_msg",msg);
                        intent.putExtra("main_index",0);
                        startActivity(intent);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        //tencent bugly
        Bugly.init(getApplicationContext(), "89680028f4", false);

        //管理Activity栈
        activityManager = new ArrayList<>();
        //注册网络变化的监听
//        registerNetworkChangedListener();

//        ToastUtil.showToast(getApplicationContext(), getMetaValue("UMENG_CHANNEL"));

        initDisplayOpinion();

        Fresco.initialize(this);
    }

    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
    }

    class InitThread extends Thread{
        public void run(){
            mPushAgent.register(new IUmengRegisterCallback() {

                @Override
                public void onSuccess(String deviceToken) {
                    //注册成功会返回device token
                    LogUtil.i("deviceToken " + deviceToken);

                    String uid = UserData.getInstance().getUSERID();
                    if(null != uid && uid.length()>0){
                        //绑定uid
                        mPushAgent.setAlias(uid, "JC_UID",
                                new UTrack.ICallBack() {
                                    @Override
                                    public void onMessage(boolean isSuccess, String message) {
                                        LogUtil.i("mPushAgent setAlias " + isSuccess);
                                    }
                                });
                    }
                }

                @Override
                public void onFailure(String s, String s1) {
                    LogUtil.i("deviceToken fail");
                }
            });
        }
    }

    public void exit() {
        try {
//            unRegisterNetworkChangedListener();
            setCurrentActivity(null);
            LogUtil.i("activityManager = " + activityManager);
            while (activityManager != null && activityManager.size() > 0) {
                Activity activity = activityManager.get(activityManager.size() - 1);
                if (activity != null) {
                    activity.finish();
                }
                if (activityManager.contains(activity)) {
                    activityManager.remove(activity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addActivity(Activity activity) {
        if (activityManager != null && activity != null) {
            activityManager.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (activityManager != null && activityManager.size() > 0 && activityManager.contains(
                activity)) {
            activityManager.remove(activity);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    public Context getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Context context) {
        currentActivity = context;
    }

    /**
     * 注册网络变化的监听
     */
    private void registerNetworkChangedListener() {
        networkChangedReceiver = new NetworkChangedReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangedReceiver, intentFilter);
    }

    /**
     * 解除网络变化的监听
     */
    private void unRegisterNetworkChangedListener() {
        if (networkChangedReceiver != null) {
            unregisterReceiver(networkChangedReceiver);
            networkChangedReceiver = null;
        }
    }
}
