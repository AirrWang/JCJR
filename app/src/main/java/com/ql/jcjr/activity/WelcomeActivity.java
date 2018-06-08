package com.ql.jcjr.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.entity.WelcomeDataEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.GlideUtil;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.SharedPreferencesUtils;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.utils.UrlUtil;
import com.ql.jcjr.view.CircleProgressView;
import com.ql.jcjr.view.CommonDialog;

public class WelcomeActivity extends Activity {

    private static final int HANDLER_WELCOME_PAGE = 1; // 欢迎界面
    private static final int HANDLER_TIME = 2; // 欢迎界面

    private Context mContext;
    private int time=3;

    @ViewInject(R.id.iv_welcome)
    private ImageView iv_welcome;
    @ViewInject(R.id.circle_progress_normal)
    private CircleProgressView cv_progress;
    @ViewInject(R.id.iv_ad)
    private ImageView iv_ad;
    @ViewInject(R.id.tv_welcome_time)
    private TextView tv_welcome_time;

    /**
     * Handler
     */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_WELCOME_PAGE:
                    checkNet();
                    break;
                case HANDLER_TIME:
                    time--;
                    tv_welcome_time.setText("跳过"+time+"s");
                    if (time!=1){

                        handler.sendEmptyMessageDelayed(HANDLER_TIME, 1000);
                    }else {
                        checkJump();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            //设置5.0以上系统的状态栏，去掉黑色半透明，变成全透明,但是布局范围会延伸到状态栏
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                window.setStatusBarColor(Color.TRANSPARENT);
//            }
//        }
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置成全屏模式
        setContentView(R.layout.activity_welcome);
        ViewUtils.inject(this);
        mContext = this;
        handler.sendEmptyMessageDelayed(HANDLER_WELCOME_PAGE, 3000);
        getInternet();

    }

    private void checkNet(){
//        if(isConnect()){
//            checkJump();
//        }
//        else{
//            showNetDialog();
//        }
        if (adShow) {
            iv_welcome.setVisibility(View.GONE);
            handler.sendEmptyMessageDelayed(HANDLER_TIME, 1000);
            cv_progress.setProgressInTime(0, 100, 2200);
        }else {
            checkJump();
        }
    }

    private void showNetDialog(){
        if (mContext==null){
            mContext=JcbApplication.appContext;
        }
        CommonDialog.Builder builder = new CommonDialog.Builder(mContext);
        builder.setTitle("温馨提示")
                .setMessage("您需要连接网络后才能继续使用服务，确认已经连接网络？")
                .setPositiveButton("已连接", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkNet();
                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        WelcomeActivity.this.finish();
                    }
                });
        builder.create().show();
    }

    private void checkJump(){
        boolean isIEClick = SharedPreferencesUtils.getInstance(mContext).getBoolean("isImmediateExperienceClick", false);
        if (isIEClick) {
            jumpToLogin();
        }else {
            jumpToCuide();
        }
    }

    /**
     * 跳转至首页
     */
    private void jumpToLogin() {
        Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    /**
     * 跳转至引导页
     */
    private void jumpToCuide() {
        Intent mainIntent = new Intent(WelcomeActivity.this, GuideActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private boolean isConnect() {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = JcbApplication.getInstance().getConnectivity();
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
//			System.out.println("获取网络状态异常");
            return false;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(HANDLER_TIME);
    }

    @OnClick({R.id.rl_jump, R.id.iv_welcome,R.id.iv_ad})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_jump:
                checkJump();
                break;
            case R.id.iv_welcome:

                break;
            case R.id.iv_ad:
                if (isJump){

                    jumpToLogin();
                    UrlUtil.showHtmlPage(mContext,"详情", url,true);
                    finish();
                }
                break;


        }
    }

    private Boolean adShow=false;
    private Boolean isJump=false;
    private String url="";
    private void getInternet() {
        SenderResultModel resultModel = ParamsManager.getWelcome();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        LogUtil.i("开屏页数据 " + responeJson);
                        WelcomeDataEntity entity = GsonParser.getParsedObj(responeJson, WelcomeDataEntity.class);
                        WelcomeDataEntity.ResultBean resultBean = entity.getResult();
                        if (resultBean.getShow().equals("1")){
                            adShow=true;
                        }
                        GlideUtil.displayPic(mContext,resultBean.getPic(),R.drawable.welcome,iv_ad);
                        if (!StringUtils.isBlank(resultBean.getLink())){
                            isJump=true;
                            url=resultBean.getLink();
                        }
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("开屏页数据 " + entity.errorInfo);
                    }

                }, mContext);

    }
}
