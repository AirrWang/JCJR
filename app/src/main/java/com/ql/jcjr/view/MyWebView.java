package com.ql.jcjr.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lidroid.xutils.util.LogUtils;

/**
 * Created by liuwenjing on 15/9/19.
 */
public class MyWebView extends WebView {
    private IMyWebViewClient clientListener;
    private IMyWebChromeClient chromeClientListener;
    private IInterceptUrl mIInterceptUrlListener;
    private Context mContext;

    /**
     * Construct a new WebView with a Context object.
     *
     * @param context A Context object used to access application assets.
     */
    public MyWebView(Context context) {
        super(context);
        initWebViewSetting(context);
    }

    /**
     * Construct a new WebView with layout parameters.
     *
     * @param context A Context object used to access application assets.
     * @param attrs   An AttributeSet passed to our parent.
     */
    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebViewSetting(context);
    }

    /**
     * Construct a new WebView with layout parameters and a default style.
     *
     * @param context  A Context object used to access application assets.
     * @param attrs    An AttributeSet passed to our parent.
     * @param defStyle The default style resource ID.
     */
    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWebViewSetting(context);
    }

    private void initWebViewSetting(Context context) {
        mContext = context;
        this.setWebViewClient(new J1WebViewClient());
        this.setWebChromeClient(new J1WebChromeClient());
    }

    public void setIMyWebViewClientListener(IMyWebViewClient listener, IMyWebChromeClient chromeClientListener, IInterceptUrl interceptUrl) {
        this.clientListener = listener;
        this.chromeClientListener = chromeClientListener;
        this.mIInterceptUrlListener = interceptUrl;
    }

    public interface IMyWebViewClient {

        void webViewCanGoBack(boolean isCanGoBack);

        void onReceivedError(WebView view,
                int errorCode, String description, String failingUrl);
    }

    public interface IMyWebChromeClient {
        void onReceivedTitle(WebView view, String title);

        void onProgressChanged(WebView view, int newProgress);
    }

    private class J1WebViewClient extends WebViewClient {

        //更新访问历史
        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
            //当进入层次比较深的时候显示关闭按钮
            boolean canGoBack;
            if (view.canGoBack()) {
                /**
                 * <p>
                 *     是否显示关闭按钮
                 *     true 显示 false 不显示
                 *     <>目前都不显示 update by rape flower 20160331</>
                 * </p>
                 */
                canGoBack = false;
            } else {
                canGoBack = false;
            }
            if (clientListener != null) {
                clientListener.webViewCanGoBack(canGoBack);
            }
        }

        // 重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.w("url = " + url);
//            view.loadUrl(url);
//            return true;

//            if (url != null && url.contains("http://www.jicaibaobao.com/newweixinlist/index.html")) {
//                mIInterceptUrlListener.interceptUrl(view,url);
//            }else {
//                view.loadUrl(url);
//            }
//
//            return true;
            return mIInterceptUrlListener.interceptUrl(view, url);
        }

        //加载错误的时候会产生这个回调，在其中可做错误处理，比如我们可以加载一个错误提示页面
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // 覆盖原生错误界面
            String data = "  ";
            view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
            if (clientListener != null) {
                clientListener.onReceivedError(view, errorCode, description, failingUrl);
            }
        }
    }

    private class J1WebChromeClient extends android.webkit.WebChromeClient {

         //获取网页title标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (chromeClientListener != null) {
                chromeClientListener.onReceivedTitle(view, title);
            }
        }

        /**
         * 当前加载进度
         * @param view
         * @param newProgress 当前的加载进度，值从0到100
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (chromeClientListener != null) {
                chromeClientListener.onProgressChanged(view, newProgress);
            }
        }
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }


    public interface IInterceptUrl {

        boolean interceptUrl(WebView view, String url);
    }
}
