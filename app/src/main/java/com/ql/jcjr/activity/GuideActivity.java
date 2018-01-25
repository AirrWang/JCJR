package com.ql.jcjr.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.ql.jcjr.R;
import com.ql.jcjr.adapter.GuideAdapter;
import com.ql.jcjr.entity.SendIMEIEntity;
import com.ql.jcjr.http.HttpRequestManager;
import com.ql.jcjr.http.HttpSenderController;
import com.ql.jcjr.http.ParamsManager;
import com.ql.jcjr.http.ResponseEntity;
import com.ql.jcjr.http.SenderResultModel;
import com.ql.jcjr.net.GsonParser;
import com.ql.jcjr.utils.SharedPreferencesUtils;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.CommonToast;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 */
public class GuideActivity extends Activity {

    @ViewInject(R.id.vp_guide_image)
    private ViewPager guideImage;


    private List<View> views;
    private GuideAdapter adapter;
    private Context mContext;

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
        setContentView(R.layout.activity_guide);
        ViewUtils.inject(this);
        setValue();
        guideImage.setOnPageChangeListener(new MyOnPageChangeListener());
        guideImage.setAdapter(adapter);
        getIEMI();

    }
    private void getIEMI() {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Log.d("唯一识别码:","用户没有授权");
            return;
        }
        String imei = telephonyManager.getDeviceId();

        Log.d("唯一识别码:",imei);

        if (StringUtils.isNotBlank(imei)) {
            sendIMEI(imei);
        }
    }

    /**
     * 今日头条所需激活监测
     * @param imei
     */


    private void sendIMEI(String imei) {
        SenderResultModel resultModel = ParamsManager.postIMEI(imei);

        HttpRequestManager.httpRequestService(resultModel,
                new HttpSenderController.ViewSenderCallback() {

                    @Override
                    public void onSuccess(String responeJson) {
                        SendIMEIEntity entity = GsonParser.getParsedObj(responeJson, SendIMEIEntity.class);
                        Log.d("上传IMEI:","success");
                        CommonToast.showHintDialog(mContext, "success");
                    }
                    @Override
                    public void onFailure(ResponseEntity entity) {
                        CommonToast.showHintDialog(mContext, entity.errorInfo);
                    }
                }, mContext);
    }

    /**
     * 设置值
     */
    public void setValue() {
        mContext = this;

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        // 将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        View view1 = mLi.inflate(R.layout.guide_view1, null);
        View view2 = mLi.inflate(R.layout.guide_view2, null);
//        View view3 = mLi.inflate(R.layout.guide_view3, null);
        View view4 = mLi.inflate(R.layout.guide_view_last, null);

        // 每个页面的view数据
        views = new ArrayList();
        views.add(view1);
        views.add(view2);
//        views.add(view3);
        views.add(view4);
        adapter = new GuideAdapter(views);
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        public void onPageSelected(int arg0) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    }

    /**
     * 立即体验
     *
     * @param v
     */
    public void immediateExperience(View v) {
        SharedPreferencesUtils.getInstance(mContext).putBoolean("isImmediateExperienceClick", true);
        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
