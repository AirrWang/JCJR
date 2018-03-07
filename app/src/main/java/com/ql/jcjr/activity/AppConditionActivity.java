package com.ql.jcjr.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.application.JcbApplication;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.utils.CommonUtils;
import com.ql.jcjr.utils.DeviceInfoUtil;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.view.ImageTextHorizontalBarLess;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by Airr on 2018/2/27.
 */

public class AppConditionActivity extends BaseActivity {

    @ViewInject(R.id.ithb_bind_mobile)
    private ImageTextHorizontalBarLess mItvMobileNum;
    @ViewInject(R.id.ithb_bind_time)
    private ImageTextHorizontalBarLess mItvTime;
    @ViewInject(R.id.ithb_mobile_type)
    private ImageTextHorizontalBarLess mItvMobileType;
    @ViewInject(R.id.ithb_mobile_id)
    private ImageTextHorizontalBarLess mItvMobileId;
    @ViewInject(R.id.ithb_app_version)
    private ImageTextHorizontalBarLess mItvAppVersion;
    @ViewInject(R.id.ithb_net_state)
    private ImageTextHorizontalBarLess mItvNetState;
    @ViewInject(R.id.ithb_ip_address)
    private ImageTextHorizontalBarLess mItvIpAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);
        ViewUtils.inject(this);
        // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
       getIP();
        initData();
    }

    private void getIP() {
        SenderResultModel resultModel = ParamsManager.getIP();

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    private String code;

                    @Override
                    public void onSuccess(String responeJson) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(responeJson);
                            code = (String) object.get("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            code="";
                        }
                        mItvIpAdress.setRightTitleText(code);
                    }

                    @Override
                    public void onFailure(ResponseEntity entity) {
                        LogUtil.i("手续费详情失败 " + entity.errorInfo);
                    }

                }, this);
    }

    private void initData() {
        mItvMobileNum.setRightTitleText(UserData.getInstance().getPhoneNumber());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = sdf.format(System.currentTimeMillis()); ////正确
        mItvTime.setRightTitleText(date);

        mItvMobileType.setRightTitleText(CommonUtils.getDeviceInfo());

        mItvMobileId.setRightTitleText(getImei());

        mItvAppVersion.setRightTitleText("v" + CommonUtils.getAppVersionName());

        if (isConnect()) {
            if (DeviceInfoUtil.isWifi()) {
                mItvNetState.setRightTitleText("Wifi");
            } else {
                mItvNetState.setRightTitleText("移动数据");
            }
        } else {
            mItvNetState.setRightTitleText("无网络");
        }

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

    private String getImei() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
        return telephonyManager.getDeviceId();
    }

    @OnClick({R.id.btn_left})
    private void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }

}
