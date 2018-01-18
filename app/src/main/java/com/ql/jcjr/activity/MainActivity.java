package com.ql.jcjr.activity;

import android.Manifest;
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
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.ViewPagerAdapter;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.Global;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.entity.ApkInfoEntity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.fragment.HomePageFragment;
import com.ql.jcjr.fragment.ManageMoneyFragment;
import com.ql.jcjr.fragment.MeFragment;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.CommonUtils;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.utils.LogUtil;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
//        ShareSDK.initSDK(this);
        getAppInfo();
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

    private void getAppInfo() {
        SenderResultModel resultModel = ParamsManager.senderGetAppInfo();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("获取apk信息成功 " + responeJson);
                ApkInfoEntity entity = GsonParser.getParsedObj(responeJson, ApkInfoEntity.class);
                if(entity.getRSPCODE().equals(Global.RESULT_SUCCESS)){
                    //设置分享信息
                    CommonUtils.shareUrl = entity.getResult().getHome_url();
                    CommonUtils.shareIcon = entity.getResult().getIcon_url();
                    CommonUtils.shareTitle = entity.getResult().getShare_title();
                    CommonUtils.shareContent = entity.getResult().getShare_content();
                    //判断新版本
                }
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("获取apk信息失败 " + entity.errorInfo);
                CommonToast.showHintDialog(mContext, entity.errorInfo);
            }

        }, this);
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
        if (UserData.getInstance().isLogin() && UserData.getInstance().getIsOpenGesture()) {
            showGestureDialog();
        }
    }

//    public void setCurrentItem(int index){
//        mViewPager.setCurrentItem(1);
//        ManageMoneyFragment lll = (ManageMoneyFragment) mFragmentList.get(1);
//        lll.setCurrentItem(index);
//    }

    private void showGestureDialog() {

        final Dialog dialog = new Dialog(this, R.style.DialogFullScreen);
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.gesture_dialog, null);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        CircleImageView mCircleImageView = (CircleImageView) view.findViewById(R.id.user_icon);
        TextView mTvTel = (TextView) view.findViewById(R.id.tv_phone_num);
        final TextView mTvIndicator = (TextView) view.findViewById(R.id.tv_indicator);
        final GestureLockViewGroup mGestureView =
                (GestureLockViewGroup) view.findViewById(R.id.gesture_view);
        TextView mTvLogin = (TextView) view.findViewById(R.id.tv_login);

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
                            dialog.dismiss();
                            CommonToast.makeCustomText(mContext, "解锁失败，重新登录");
                            UserData.getInstance().setUSERID("");
                            UserData.getInstance().setIsOpenGesture(false);
                            UserData.getInstance().setGestureCipher("");
                            Intent intent = new Intent(mContext, LoginActivityCheck.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFinshInput(List<Integer> mChoose, int tryTimes) {
                        if (mGestureView.checkAnswer(mChoose.toString(), UserData.getInstance().getGestureCipher())) {
                            isMatch = true;
                            dialog.dismiss();
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
                dialog.dismiss();
                UserData.getInstance().setUSERID("");
                UserData.getInstance().setIsOpenGesture(false);
                UserData.getInstance().setGestureCipher("");
                Intent intent = new Intent(mContext, LoginActivityCheck.class);
                startActivity(intent);
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
        if (!addPermission(permissionsList, Manifest.permission.SYSTEM_ALERT_WINDOW))
            permissionsNeeded.add("系统提示");

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
