package com.ql.jcjr.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ql.jcjr.activity.WebViewActivity;
import com.ql.jcjr.utils.StringUtils;

import java.io.Serializable;
import java.util.List;


public class H5Request implements Serializable {

    public static final String REQUEST_HTTP_PREFIX = "http:";
    public static final String REQUEST_FILE_PREFIX = "file:";


    private static final long serialVersionUID = 5299453460320603455L;
    private String title;
    private String url;
    private Boolean isBackable;
    private Boolean isShowActionBar;
    private String extras;

    private void showPage(Context context, String url) {

//        if (url.contains(Constants.URL_SCHEME_J1WIRELESS)) {
//            url = url.replace(Constants.URL_SCHEME_J1WIRELESS, Constants.URL_SCHEME_J1WIRELESS_HEALTH);
//        }

        /*Intent intent = new Intent(context, UrlSchemeActivity.class);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);*/
    }

    /**
     * 打开H5页面
     *
     * @param context
     * @param url       跳转Url
     * @param needLogin 是否需要登陆
     */
    public static void startH5Page(Context context, String url, Boolean needLogin) {
        startH5Page(context, "", needLogin);
    }

    /**
     * 打开H5页面
     *
     * @param context
     * @param title     标题
     * @param url       跳转Url
     * @param needLogin 是否需要登陆
     */
    public static void startH5Page(Context context, String title, String url, Boolean needLogin) {
//        if (needLogin && !HYUserSessionCacheBean.shareInstance().isLogin()) {
//            StartActivityUtils.startLoginActivity(context);
//        } else {
            startH5Page(context, title, url);
//        }
    }

    /**
     * 打开H5页面
     *
     * @param context
     * @param url     跳转Url
     */
    public static void startH5Page(Context context, String url) {
        startH5Page(context, null, url);
    }

    /**
     * 打开H5页面
     *
     * @param context
     * @param title   H5页面的标题
     * @param url     跳转Url
     */
    public static void startH5Page(Context context, String title, String url) {
        if (StringUtils.isBlank(url)) {
            return;
        }
            H5Request h5Request = new H5Request();
            if (StringUtils.isNotBlank(title)) {
                h5Request.setTitle(title);
            }
            h5Request.setUrl(url);
            h5Request.setIsLeftable(true);
            h5Request.setIsShowActionBar(false);
            Intent intent = new Intent(context, WebViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("h5Request", h5Request);
            intent.putExtras(bundle);
            context.startActivity(intent);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *  在setUrl地址上面组装url地址，累加字段,并且将新的url赋值到this.url
     *
     * @param appends 要累加的字段
     */
    public void appendUrl(List<String> appends) {
        if (appends == null || appends.size() == 0) {
            return;
        }
        StringBuffer urlBuf = new StringBuffer();
        urlBuf.append(this.url);
        for (String append : appends) {
            if (urlBuf.toString().indexOf("?") > 0) {
                urlBuf.append("&");
            } else {
                urlBuf.append("?");
            }

            urlBuf.append(append);
        }
        setUrl(urlBuf.toString());

    }

    public Boolean getIsLeftable() {
        return isBackable;
    }

    public void setIsLeftable(Boolean isBackable) {
        this.isBackable = isBackable;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public Boolean getIsShowActionBar() {
        return isShowActionBar;
    }

    public void setIsShowActionBar(Boolean isShowActionBar) {
        this.isShowActionBar = isShowActionBar;
    }

}
