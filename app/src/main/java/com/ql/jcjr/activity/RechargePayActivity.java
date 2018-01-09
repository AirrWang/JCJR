package com.ql.jcjr.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.manager.CommonDialogManager;
import com.ql.jcjr.utils.LogUtil;

import org.apache.http.util.EncodingUtils;

public class RechargePayActivity extends BaseActivity {

    @ViewInject(R.id.webview)
    private WebView myWebView;

    private CommonDialogManager mCommonDialogManager;
    private String mPostData;
    private String mPayUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_pay);
        ViewUtils.inject(this);
        getIntentData();
        webviewSet();
        initView();
//        initData();
        myWebView.postUrl(mPayUrl, EncodingUtils.getBytes(mPostData, "base64"));
    }

    private void getIntentData() {
        Intent intent = getIntent();
        mPayUrl = intent.getStringExtra("pay_url");
        mPostData = intent.getStringExtra("post_data");
    }

    private void webviewSet() {
        //启用支持javascript
        WebSettings settings = myWebView.getSettings();
        //使页面适应屏幕大小
        settings.setUseWideViewPort(true);
        //使页面适应屏幕大小
        settings.setLoadWithOverviewMode(true);
        //设置后可以放大缩小
        settings.setBuiltInZoomControls(true);
        //隐藏缩小放大控件
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    private void initView() {
        mCommonDialogManager = new CommonDialogManager(this);
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.e("shouldOverrideUrlLoading " );
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url != null && url.equals(RequestURL.INTERCEPT_XSFL_URL)) {
                    Intent intent = new Intent(RechargePayActivity.this, MainActivity.class);
                    RechargePayActivity.this.startActivity(intent);
                    finish();
                } else if(url != null && url.equals(RequestURL.INTERCEPT_RECHARGE_URL)){
                    Intent intent = new Intent(RechargePayActivity.this, RechargeActivity.class);
                    RechargePayActivity.this.startActivity(intent);
                    finish();
                }
                else if(url != null && url.indexOf(RequestURL.INTERCEPT_GOINGACCOUNT_URL)!=-1){
                    Intent intent = new Intent(RechargePayActivity.this, MainActivity.class);
                    intent.putExtra("main_index",2);
                    RechargePayActivity.this.startActivity(intent);
                    finish();
                }else {
                    view.loadUrl(url);
                }

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                LogUtil.e("onPageStarted  " );
                mCommonDialogManager.showLoading("正在加载...");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                LogUtil.e("onPageFinished  " );
                // 页面跳转后，可回退到跳转前页面
                mCommonDialogManager.dismiss();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                LogUtil.e("onReceivedError");
                mCommonDialogManager.dismiss();
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                LogUtil.e("onReceivedSslError");
                mCommonDialogManager.dismiss();
                super.onReceivedSslError(view, handler, error);
            }
        });
    }

//    private void initData() {
//        LinkedHashMap parameMap = new LinkedHashMap();
//        parameMap.put("inscode", AppConfig.INSCODE);
//        parameMap.put("mercid", UserData.getInstance().getUserInfo().getMERCID());
//        parameMap.put("mercname", UserData.getInstance().getUserInfo().getMERCNAME());
//        parameMap.put("tradetype", mTradeType);
//        parameMap.put("transubType", mT0OrT1);
//        parameMap.put("amount", mAmount);
//        parameMap.put("transcode", "1");
//        parameMap.put("tranaccno", mVerifiedCardNum);
//
//        try {
//            String code = "code=" + "2061";
//            String requestParam = JsonUtils.setJson(parameMap);
//            String encryptContent = RSAEncrypt.encryptData(requestParam).toString();
//            String content = "content=" + URLEncoder.encode(encryptContent, "utf-8");
//            mPostData = code + "&" + content;
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//    }

    @OnClick({R.id.btn_left})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }
}
