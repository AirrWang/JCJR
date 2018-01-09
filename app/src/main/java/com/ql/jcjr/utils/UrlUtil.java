package com.ql.jcjr.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ql.jcjr.activity.WebViewActivity;
import com.ql.jcjr.model.H5Request;

/**
 * ClassName: urlUtil
 * Description:
 * Author: Administrator
 * Date: Created on 202016/10/10.
 */
public class UrlUtil {

    public static String getHtmlUrlString(String url) {
        return "<HTML><Div align=\"center\" margin=\"0px\"><IMG src=\"" + url + "\" " +
                "margin=\"0px\"/></Div>";
    }

    public static void showHtmlPage(Context context, String title, String url) {
        H5Request h5Request = new H5Request();
        h5Request.setTitle(title);
        h5Request.setUrl(url);
        h5Request.setIsLeftable(true);
        h5Request.setIsShowActionBar(false);
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("h5Request", h5Request);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
