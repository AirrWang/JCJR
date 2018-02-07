package com.ql.jcjr.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.ql.jcjr.R;
import com.ql.jcjr.base.BaseActivity;
import com.ql.jcjr.constant.RequestURL;
import com.ql.jcjr.entity.UserData;
import com.ql.jcjr.model.H5Request;
import com.ql.jcjr.utils.CommonUtils;
import com.ql.jcjr.utils.JsonUtils;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.ShareHelper;
import com.ql.jcjr.utils.StringUtils;
import com.ql.jcjr.view.ActionBar;
import com.ql.jcjr.view.MyWebView;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/20.
 * <p/>
 * 显示HTML(H5)
 */
public class WebViewActivity extends BaseActivity {
    public static final String SEPARATE = "/";
    @ViewInject(R.id.wb_web)
    public MyWebView webView;
    @ViewInject(R.id.ab_title)
    private ActionBar actionBar;
    @ViewInject(R.id.tv_request_url)
    private TextView tvRequestUrl;
    @ViewInject(R.id.progressbar)
    private ProgressBar progressbar;
    private Context mContext;
    private H5Request h5Request;
    @ViewInject(R.id.webviewlayout)
    private RelativeLayout webviewLayout;
    @ViewInject(R.id.tv_load_error)
    private TextView tvLoadError;

    private final int RESULT_CODE_LOGIN = 10;

    private final int INTERFACE_SHARE_INFO = 1;
    private final int INTERFACE_SHARE_CONTENT = 2;
    private final int INTERFACE_SHARE_ACTION = 3;
    private final int INTERFACE_REQUEST_UID = 4;
    private final int INTERFACE_REQUEST_LOGIN = 5;
    private final int INTERFACE_UPDATE_TITLE = 6;
    private final int INTERFACE_START_ACTIVITY = 7;
    private Handler webInterfaceHandler;
    private ShareHelper mShare;
    private Boolean isShare;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.title_bg_color), 0);
//        StatusBarUtil.setTransparent(this);
        ViewUtils.inject(this);

        mContext = this;
        actionBar.setLeftVisible(View.VISIBLE);


        Intent intent=getIntent();
        isShare = intent.getBooleanExtra("isShare",false);
        if (!isShare){
            actionBar.showShareIcon(new MyShareClickListener());
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            h5Request = (H5Request) bundle.getSerializable("h5Request");
            if (h5Request != null) {
                initWebView(h5Request.getUrl());
            }
            setActionBar(h5Request);
        }

        webInterfaceHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case INTERFACE_SHARE_INFO:

                        break;

                    case INTERFACE_SHARE_CONTENT:

                        break;

                    case INTERFACE_SHARE_ACTION:
                        mShare.share();
                        break;

                    case INTERFACE_REQUEST_UID:
                        String token =UserData.getInstance().getUSERID();
                        webView.loadUrl("javascript:onRequestUidResult('" + token +"')");
                        break;

                    case INTERFACE_REQUEST_LOGIN:
                        Intent intent = new Intent(mContext, LoginActivityCheck.class);
                        startActivityForResult(intent, RESULT_CODE_LOGIN);
                        break;

                    case INTERFACE_UPDATE_TITLE:
                        String title = (String)msg.obj;
                        actionBar.setTitle(title);
                        break;

                    case INTERFACE_START_ACTIVITY:

                        Bundle bundle = msg.getData();
                        String packageInfo = bundle.getString("url");
                        String params = bundle.getString("data");
                        LogUtil.i("packageInfo = " + packageInfo);
                        LogUtil.i("params = " + params);
                        try {
                            Intent startIntent = new Intent();
                            if (packageInfo.contains(SEPARATE)) {
                                String[] str = packageInfo.split(SEPARATE);
                                LogUtil.i(str[0]+" ---- " + str[1]);
                                startIntent.setComponent(new ComponentName(str[0], str[1]));
                            } else {
                                startIntent.setAction(packageInfo);
                            }

                            HashMap<String, Object> paramsMap = null;
                            if(null != params && params.length()>0){
                                JSONObject jsonObject = new JSONObject(params);
                                paramsMap = new HashMap<>();
                                JsonUtils.JsonToHashMap(jsonObject, paramsMap);
                            }

                            if (null != paramsMap) {
                                for (Object o : paramsMap.entrySet()) {
                                    Map.Entry entry = (Map.Entry) o;
                                    startIntent.putExtra((String) entry.getKey(), (String) entry.getValue());
                                }
                            }
                            mContext.startActivity(startIntent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                }

                super.handleMessage(msg);
            }
        };

        mShare = new ShareHelper(this);
        mShare.setShareWebInfo(CommonUtils.shareUrl,CommonUtils.shareIcon, CommonUtils.shareTitle, CommonUtils.shareContent);
    }

    class MyShareClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            mShare.share();
        }
    }

    @Override
    protected void onDestroy() {
        UMShareAPI.get(this).release();
        super.onDestroy();
    }

    private void initWebView(String url) {
        WebSettings webSettings = webView.getSettings();
        //使页面适应屏幕大小
        webSettings.setUseWideViewPort(true);
        //使页面适应屏幕大小
        webSettings.setLoadWithOverviewMode(true);
        //设置后可以放大缩小
        webSettings.setBuiltInZoomControls(true);
        //隐藏缩小放大控件
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.loadUrl(url);
        webView.setIMyWebViewClientListener(new MyWebView.IMyWebViewClient() {
            @Override
            public void webViewCanGoBack(boolean isCanGoBack) {
                //当进入层次比较深的时候显示关闭按钮
                if (isCanGoBack) {
                    actionBar.setLeftSecondText("关闭");
                } else {
                    actionBar.setLeftSecondVisible(View.GONE);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                    String failingUrl) {
                // 覆盖原生错误界面
                String data = "  ";
                view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
                showLoadError();
            }
        }, new MyWebView.IMyWebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (h5Request == null || StringUtils.isBlank(h5Request.getTitle())) {
                    actionBar.setTitle(title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 95) {
                    progressbar.setVisibility(View.GONE);
                } else {
                    progressbar.setVisibility(View.VISIBLE);
                }
            }
        }, new MyWebView.IInterceptUrl() {
            @Override
            public boolean interceptUrl(WebView view, String url) {

                if (url != null && url.equals(RequestURL.INTERCEPT_XSFL_URL)) {
//                    view.loadUrl("http://www.jicaibaobao.com/index.action?user&q=going/login1");
                    Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
                    intent.putExtra("main_index",1);
                    WebViewActivity.this.startActivity(intent);
                    finish();
                } else if(url != null && url.equals(RequestURL.INTERCEPT_RECHARGE_URL)){
                    Intent intent = new Intent(WebViewActivity.this, RechargeActivity.class);
                    WebViewActivity.this.startActivity(intent);
                    finish();
                }
                else if(url != null && url.indexOf(RequestURL.INTERCEPT_GOINGACCOUNT_URL)!=-1){
                    Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
                    intent.putExtra("main_index",2);
                    WebViewActivity.this.startActivity(intent);
                    finish();
                } else if(url != null && url.equals(RequestURL.INTERCEPT_MYREDPACKETS_URL)){
                    Intent intent = new Intent(WebViewActivity.this, MyRedPacketsActivity.class);
                    WebViewActivity.this.startActivity(intent);
                }
                else {
                    view.loadUrl(url);
                }

                return true;
            }
        });

        webView.addJavascriptInterface(new WebappInterface(), "JCJRInterface");
    }

    /**
     * 设置标题栏
     */
    public void setActionBar(H5Request h5Request) {
        if (h5Request != null) {
            actionBar.setTitle(h5Request.getTitle());
        }
    }

    private void showLoadError() {
        webView.setVisibility(View.GONE);
        tvLoadError.setVisibility(View.VISIBLE);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @OnClick({R.id.btn_left, R.id.tv_second_left, R.id.btn_right, R.id.tv_load_error})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_second_left: {
                finish();

                break;
            }
            case R.id.btn_left:
                webviewGoBack();
//                webviewGoBack();///TODO 取消返回上一个页面，直接关闭
                break;
            case R.id.btn_right:
                break;
            case R.id.tv_load_error:
                reLoadWebView();
                break;
            default:
                break;
        }
    }

    private void reLoadWebView() {
        webView.reload();
        webView.setVisibility(View.VISIBLE);
        tvLoadError.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            webviewGoBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void webviewGoBack() {
        //根页面的时候，隐藏关闭
        if (webView.copyBackForwardList().getCurrentIndex() == 1) {
            actionBar.setLeftSecondVisible(View.GONE);
        }
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
//            if (isH5ShowDialog()) {
//                showH5Dialog(h5DialogJson);
//            } else {
            finish();
//            }
        }
    }

    public class WebappInterface {

        /**
         * 网页 设置 分享信息 到客户端
         * @param fromUrl 分享的地址
         * @param fromIconUrl 分享的图片地址
         * @param fromTitle 分享的标题
         * @param fromContent 分享的内容
         */
        @JavascriptInterface
        public void setShareInfo(final String fromUrl, final String fromIconUrl, final String fromTitle, final String fromContent) {
            if(null != fromUrl){
                mShare.setShareWebInfo(fromUrl, fromIconUrl, fromTitle, fromContent);
                webInterfaceHandler.sendEmptyMessage(INTERFACE_SHARE_INFO);
            }
        }

        /**
         * 网页 设置 分享内容 到客户端
         * @param fromTitle 分享的标题
         * @param fromContent 分享的内容
         */
        @JavascriptInterface
        public void setShareContent(final String fromTitle, final String fromContent) {
            if(null != fromTitle){
                mShare.setShareContent(fromTitle, fromContent);
                webInterfaceHandler.sendEmptyMessage(INTERFACE_SHARE_CONTENT);
            }
        }

        /**
         * 网页 调用 客户端 分享功能
         */
        @JavascriptInterface
        public void appShare() {
            webInterfaceHandler.sendEmptyMessage(INTERFACE_SHARE_ACTION);
        }

        /**
         * 网页请求客户端下发uid
         */
        @JavascriptInterface
        public void requestUid() {
            webInterfaceHandler.sendEmptyMessage(INTERFACE_REQUEST_UID);
        }

        /**
         * 网页请求客户端登录
         */
        @JavascriptInterface
        public void requestLogin() {
            webInterfaceHandler.sendEmptyMessage(INTERFACE_REQUEST_LOGIN);
        }

        @JavascriptInterface
        public void changeTitle(final String title) {
            if(null != title){
                Message msg = new Message();
                msg.obj = title;
                msg.what = INTERFACE_UPDATE_TITLE;
                webInterfaceHandler.sendMessage(msg);
            }
        }

        /**
         * 打开应用界面
         */
        @JavascriptInterface
        public void startActivityWithParam(String packageInfo, String params) {
            //packageInfo格式 包名/activity路径，或者是action名字，如启动拨号之类的，，，params附带参数、json格式
            if(null != packageInfo){
                Message msg = new Message();
                msg.what = INTERFACE_START_ACTIVITY;
                Bundle bundle = msg.getData();
                bundle.putString("url", packageInfo);
                bundle.putString("data", params);

                webInterfaceHandler.sendMessage(msg);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//        LogUtil.i("onActivityResult = "+requestCode);
        if(requestCode == RESULT_CODE_LOGIN){
            //判断是否有登陆记录
            String uid = UserData.getInstance().getUSERID();
            if(null != uid && uid.length()>0){
                webView.loadUrl("javascript:onRequestUidResult('" + uid +"')");
            }

        }
    }

}
