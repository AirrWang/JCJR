package com.ql.jcjr.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.ViewPagerAdapter;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.fragment.HomePageFragment;
import com.ql.jcjr.fragment.ManageMoneyFragment;
import com.ql.jcjr.fragment.MeFragment;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.utils.ShareHelper;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.UrlUtil;
import com.ql.jcjr.view.ActionBar;
import com.ql.jcjr.view.CircleImageView;
import com.ql.jcjr.view.CommonDialog;
import com.ql.jcjr.view.CommonToast;
import com.ql.jcjr.view.gesturecipher.GestureLockViewGroup;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class MainActivity extends BaseActivity
        implements ViewPager.OnPageChangeListener, View.OnClickListener {

    public final static int INDEX_MANAGE_MONEY = 1;
    private final static int IDEX_HOME_PAGE = 0;
    private final static int INDEX_ME = 2;
    private final static int INDEX_MORE = 3;
    private FingerprintManagerCompat manager;
    private CancellationSignal mCancellationSignal;
    @ViewInject(R.id.viewpager)
    public ViewPager mViewPager;
    @ViewInject(R.id.bottom_tab)
    private RadioGroup mRadioGroup;
    private Context mContext;
    private ActionBar mActionBar;

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    private ShareHelper mShare;

    private long lastPressTime;
    private boolean isMatch;

    private int[] mIndexPage = new int[]{
            R.id.tab_home_page,
            R.id.tab_manage_money,
            R.id.tab_me
            };

    private int[] mTitles = new int[]{
            R.string.home_page_title,
            R.string.manage_money_title,
            R.string.me_title
    };
    private TextView tv_status;
    private Dialog dialog;
    private Dialog dialogGesture;
    private Dialog dialogFinger;
    private int a =5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
//        ShareSDK.initSDK(this);
        initLayout();
        //锁屏展示
        isShowGestureDialog();
        //权限判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            insertDummyContactWrapper();
        }
        //处理推送过来的信息
        dealPush(getIntent());
    }

    private void showFingerDialog() {
        manager=FingerprintManagerCompat.from(this);
        mCancellationSignal = new CancellationSignal();

        dialog = new Dialog(this, R.style.CommonDialog);
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.finger_dialog, null);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
        WindowManager wm=this.getWindowManager();
        dialog.getWindow().setLayout((int) (wm.getDefaultDisplay().getWidth()*0.76),ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_status = (TextView) view.findViewById(R.id.tv_status);
        Button btn_dis= (Button) view.findViewById(R.id.btn_dis);
        manager.authenticate(null, 0, mCancellationSignal, new MyCallBack(), null);


        btn_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mCancellationSignal.cancel();

            }
        });

    }
    private class MyCallBack extends FingerprintManagerCompat.AuthenticationCallback {
        private static final String TAG = "MyCallBack";


        // 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            Log.d(TAG, "onAuthenticationError: " + errString);
            //TODO
            tv_status.setText(""+errString);
            String a =errString+"";

            if (a.contains("次数过多")) {
                CommonToast.makeCustomText(mContext, "指纹验证失败，请重新登录");
                dialog.dismiss();
                UserData.getInstance().setUSERID("");
                UserData.getInstance().setIsOpenGesture(false);
                UserData.getInstance().setGestureCipher("");
                UserData.getInstance().setFingerPrint(false);
                Intent intent = new Intent(mContext, LoginActivityCheck.class);
                startActivity(intent);
                if (dialogFinger != null) {
                    dialogFinger.dismiss();
                }
                if (dialogGesture!=null){
                    dialogGesture.dismiss();
                }
            }
        }

        // 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
        @Override
        public void onAuthenticationFailed() {
            Log.d(TAG, "onAuthenticationFailed: " + "验证失败");
            tv_status.setText("验证失败,请重试");
            a--;
            if (a==0 ){
                dialog.dismiss();
                mCancellationSignal.cancel();
            }
            AnimatorSet animatorSetGroup = new AnimatorSet();
            ObjectAnimator animation = ObjectAnimator.ofFloat(tv_status, "translationX", 0f, 50f);
            ObjectAnimator animation1 = ObjectAnimator.ofFloat(tv_status, "translationX", 0f, -50f);
            animatorSetGroup.setInterpolator(new CycleInterpolator(8));
            animatorSetGroup.setDuration(300);
            animatorSetGroup.play(animation1).after(animation);
            animatorSetGroup.start();
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            Log.d(TAG, "onAuthenticationHelp: " + helpString);
            tv_status.setText(""+helpString);
        }

        // 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult
                                                      result) {
            Log.d(TAG, "onAuthenticationSucceeded: " + "验证成功");
            dialog.dismiss();
            if (dialogGesture!=null) {
                dialogGesture.dismiss();
            }
            if (dialogFinger!=null){
                dialogFinger.dismiss();
            }
            mCancellationSignal.cancel();
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int mainIndex = intent.getIntExtra("main_index",1);
        if(mainIndex==2){
            ((MeFragment)(mFragmentList.get(2))).getMineFragmentData();
        }
        mViewPager.setCurrentItem(mainIndex);

        dealPush(intent);
    }

    //处理push
    private void dealPush(Intent intent){
        if(null != intent){
            boolean isPush = intent.getBooleanExtra("push_tag",false);
            if(isPush){
                String msg = intent.getStringExtra("push_msg");
                try{
                        JSONObject object = new JSONObject(msg);
                        String type = object.optString("type");
                        String value = object.optString("value");
                        if(type.equals("wdxx")){
                            Intent intentStart = new Intent(MainActivity.this, MsgHomeActivity.class);
                            startActivity(intentStart);
                        }
                        else if(type.equals("jxhd")){
                            UrlUtil.showHtmlPage(mContext,"活动详情", value);
                        }
                        else if(type.equals("xwgg")){
                            UrlUtil.showHtmlPage(mContext,"公告详情", RequestURL.NOTICE_DETAILS_URL + value);
                        }
                }catch (Exception e){}
            }
        }
    }

    private void initLayout() {
        mContext = this;
        mActionBar = (ActionBar) findViewById(R.id.ab_header);
        mActionBar.setOnClickListener(this);

        initViewPager();
        initBottomTab();
    }

    private void initViewPager() {
        mFragmentList.clear();

        mFragmentList.add(new HomePageFragment());

        mFragmentList.add(new ManageMoneyFragment());

        mFragmentList.add(new MeFragment());

//        mFragmentList.add(new MoreFragment());

        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList));
        mViewPager.setOffscreenPageLimit(mFragmentList.size() - 1);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(this);
    }

    private void initBottomTab() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tab_home_page:
                        mViewPager.setCurrentItem(IDEX_HOME_PAGE);
                        break;

                    case R.id.tab_manage_money:
                        mViewPager.setCurrentItem(INDEX_MANAGE_MONEY);
                        break;

                    case R.id.tab_me:
                        mViewPager.setCurrentItem(INDEX_ME);
                        break;
//                    case R.id.tab_more:
//                        mViewPager.setCurrentItem(INDEX_MORE);
//                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        UMShareAPI.get(this).release();
        JcbApplication.getInstance().exit();
    }

    private void isShowGestureDialog() {
        if (UserData.getInstance().isLogin() && UserData.getInstance().getFingerPrint()&& UserData.getInstance().getIsOpenGesture()){
            showGestureDialog(true);
            showFingerDialog();
        }else {
            if (UserData.getInstance().isLogin() && UserData.getInstance().getIsOpenGesture()) {
                showGestureDialog(false);
            }
            if (UserData.getInstance().isLogin() && UserData.getInstance().getFingerPrint()) {
                showFingerFullScreenDialog();
                showFingerDialog();
            }
        }
    }

    private void showFingerFullScreenDialog() {
        dialogFinger=new Dialog(this, R.style.DialogFullScreen);
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.finger_full_dialog, null);
        dialogFinger.setContentView(view);
        dialogFinger.setCancelable(false);
        dialogFinger.show();
        dialogFinger.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        CircleImageView mCircleImageView = (CircleImageView) view.findViewById(R.id.user_icon);
        TextView mTvTel = (TextView) view.findViewById(R.id.tv_phone_num);
        TextView mTvLogin = (TextView) view.findViewById(R.id.tv_login);
        TextView mTvCallFinger= (TextView) view.findViewById(R.id.tv_gesture_finger);
        LinearLayout mLlCall= (LinearLayout) view.findViewById(R.id.ll_call_finger);
        GlideUtil.displayPic(mContext, UserData.getInstance().getUserIconUrl(),
                R.drawable.gesture_user_icon, mCircleImageView);
        mTvTel.setText("欢迎回来，"+StringUtils.getHidePhoneNum(UserData.getInstance().getPhoneNumber()));
        mTvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFinger.dismiss();
                UserData.getInstance().setUSERID("");
                UserData.getInstance().setIsOpenGesture(false);
                UserData.getInstance().setGestureCipher("");
                UserData.getInstance().setFingerPrint(false);
                Intent intent = new Intent(mContext, LoginActivityCheck.class);
                startActivity(intent);
            }
        });
        mLlCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFingerDialog();
            }
        });
        mTvCallFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFingerDialog();
            }
        });
    }

    private void showGestureDialog(boolean b) {

        dialogGesture = new Dialog(this, R.style.DialogFullScreen);
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.gesture_dialog_main, null);
        dialogGesture.setContentView(view);
        dialogGesture.setCancelable(false);
        dialogGesture.show();
        dialogGesture.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        CircleImageView mCircleImageView = (CircleImageView) view.findViewById(R.id.user_icon);
        TextView mTvTel = (TextView) view.findViewById(R.id.tv_phone_num);
        TextView mTvCallFinger= (TextView) view.findViewById(R.id.tv_gesture_finger);
        if (!b){
            mTvCallFinger.setVisibility(View.GONE);
        }
        final TextView mTvIndicator = (TextView) view.findViewById(R.id.tv_indicator);
        final GestureLockViewGroup mGestureView =
                (GestureLockViewGroup) view.findViewById(R.id.gesture_view);
        TextView mTvLogin = (TextView) view.findViewById(R.id.tv_login);
        mGestureView.setUnMatchExceedBoundary(5);
        GlideUtil.displayPic(mContext, UserData.getInstance().getUserIconUrl(),
                R.drawable.gesture_user_icon, mCircleImageView);
        mTvTel.setText("欢迎回来，"+StringUtils.getHidePhoneNum(UserData.getInstance().getPhoneNumber()));

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
                            dialogGesture.dismiss();
                            CommonToast.makeCustomText(mContext, "解锁失败，重新登录");
                            UserData.getInstance().setUSERID("");
                            UserData.getInstance().setIsOpenGesture(false);
                            UserData.getInstance().setFingerPrint(false);
                            UserData.getInstance().setGestureCipher("");
                            Intent intent = new Intent(mContext, LoginActivityCheck.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFinshInput(List<Integer> mChoose, int tryTimes) {
                        if (mGestureView.checkAnswer(mChoose.toString(), UserData.getInstance().getGestureCipher())) {
                            isMatch = true;
                            dialogGesture.dismiss();
                        } else {
                            isMatch = false;
                            mTvIndicator.setText("绘制错误，还可以输入" + tryTimes + "次");
                            mTvIndicator.setTextColor(getResources().getColor(R.color.red));
                        }

                        mGestureView.reset();
                    }
                });

        mTvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGesture.dismiss();
                UserData.getInstance().setUSERID("");
                UserData.getInstance().setIsOpenGesture(false);
                UserData.getInstance().setGestureCipher("");
                UserData.getInstance().setFingerPrint(false);
                Intent intent = new Intent(mContext, LoginActivityCheck.class);
                startActivity(intent);
            }
        });
        mTvCallFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFingerDialog();
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mViewPager.setCurrentItem(position);
        mRadioGroup.check(mIndexPage[position]);
        mActionBar.setTitle(mTitles[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent
                .ACTION_DOWN) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPressTime > 2000) {
                Toast.makeText(getBaseContext(), R.string.home_confirm_exit,
                        Toast.LENGTH_SHORT).show();
                lastPressTime = currentTime;
            } else {
                finish();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @TargetApi(Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {
        //提示用户需要手动开启的权限集合
        List<String> permissionsNeeded = new ArrayList<String>();

        //功能所需权限的集合
        final List<String> permissionsList = new ArrayList<String>();

        //若用户拒绝了该权限申请，则将该申请的提示添加到“用户需要手动开启的权限集合”中
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("保存应用数据");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("读取应用数据");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("获取当前地理位置");
        if (!addPermission(permissionsList, Manifest.permission.GET_ACCOUNTS))
            permissionsNeeded.add("获取通讯录信息");
//        if (!addPermission(permissionsList, Manifest.permission.SYSTEM_ALERT_WINDOW))
//            permissionsNeeded.add("获取应用提示");
        if(!addPermission(permissionsList,Manifest.permission.USE_FINGERPRINT))
            permissionsNeeded.add("获取手机指纹信息");


        //存在未配置的权限
        if (permissionsList.size() > 0) {

            //若用户赋之前拒绝过一部分权限，则需要提示用户开启其余权限并返回，否则该功能将无法执行
            if (permissionsNeeded.size() > 0) {

                // Need Rationale
                String message = "我们需要您授权下列权限：\n";
                for (int i = 0; i < permissionsNeeded.size(); i++)
                    message = message + "\n" + permissionsNeeded.get(i);

                //弹出对话框，提示用户需要手动开启的权限
                try {
                    final CommonDialog.Builder dialog = new CommonDialog.Builder(this);
                    dialog.setTitle(message);
                    dialog.setMessageSize(R.dimen.f03_34);
                    dialog.setButtonTextSize(R.dimen.f03_34);
                    dialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    dialog.create().show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }

            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                //初始化Map集合，其中Key存放所需权限，Value存放该权限是否被赋予
                Map<String, Integer> perms = new HashMap<String, Integer>();

                // 向Map集合中加入元素，初始时所有权限均设置为被赋予（PackageManager.PERMISSION_GRANTED）
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.GET_ACCOUNTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.SYSTEM_ALERT_WINDOW,PackageManager.PERMISSION_GRANTED);

                // 将第二个参数回传的所需权限及第三个参数回传的权限结果放入Map集合中，由于Map集合要求Key值不能重复，所以实际的权限结果将覆盖初始值
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                // 若所有权限均被赋予，则执行方法
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                }
                //否则弹出toast，告知用户需手动赋予权限
                else {
                    // Permission Denied
//                    insertDummyContactWrapper();
                }
            }
            break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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

//            Intent localIntent = new Intent();
//            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
//            startActivity(localIntent);
        }
    }
}
