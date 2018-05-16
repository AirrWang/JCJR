package com.ql.jcjr.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.CommonLoadingDialog;

/**
 * Created by Airr on 2018/5/7.
 */

public class NetNullActivity extends Activity{

    private CommonLoadingDialog loadingDialog;
    private static final int HANDLER_DISMISS = 1;
    /**
     * Handler
     */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_DISMISS:
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置5.0以上系统的状态栏，去掉黑色半透明，变成全透明,但是布局范围会延伸到状态栏
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
        setContentView(R.layout.activity_net_null);
        ViewUtils.inject(this);
    }

    @OnClick({R.id.btn_fresh, R.id.btn_to_set})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fresh:
                loadingDialog = new CommonLoadingDialog(this);
                loadingDialog.show();
                if (isNetworkConnected(this)){
                    getNet();
                }else {
                    handler.sendEmptyMessageDelayed(HANDLER_DISMISS, 888);
                }
                break;

            case R.id.btn_to_set:
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                Uri uri = Uri.fromParts("package", getPackageName(), null);
//                intent.setData(uri);
//                startActivity(intent);
                Intent intent =  new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;

        }
    }

    private void getNet() {
        SenderResultModel resultModel = ParamsManager.senderBanner();

        HttpRequestManager.httpRequestService(resultModel, new HttpSenderController.ViewSenderCallback() {

            @Override
            public void onSuccess(String responeJson) {
                LogUtil.i("广告栏成功 " + responeJson);
                finish();
            }

            @Override
            public void onFailure(ResponseEntity entity) {
                LogUtil.i("广告栏失败 " + entity.errorInfo);
            }

        }, this);
        handler.sendEmptyMessageDelayed(HANDLER_DISMISS, 888);
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
                    .CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空
            if (networkInfo != null)
                return networkInfo.isAvailable();
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK){
//            moveTaskToBack(true);
            return true;//不执行父类点击事件
        }
        return false;
//        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
