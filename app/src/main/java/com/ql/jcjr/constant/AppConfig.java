package com.ql.jcjr.constant;

import android.os.Environment;

/**
 * Created by Liuchao on 2016/9/22.
 */
public class AppConfig {

    //官方网站
    public static final String OFFICIAL_WEBSITE_URL = "http://www.jicaibaobao.com";
    //常见问题
    public static final String COMMON_PROBLEM_URL = "http://www.jicaibaobao.com/addition/faq.html";
    //关于我们
    public static final String ABOUT_US_URL = "http://www.jicaibaobao.com/addition/appabout.html";
    //客服电话
    public static final String SERVICE_HOTLINE_NUM = "4008525558";

    /**
     * 用来保存拍照图片的目录
     * Environment.getExternalStorageDirectory().getAbsolutePath() + "/HSS/hss_img_save";
     */
    public static final String APP_PICTURE_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath() + "/JCBB/image";

    /**
     * 用来下载app的目录
     */
    public static final String APP_UPDATE_FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath() + "/JCBB/save";
    /**
     * apk的name
     */
    public static final String APP_UPDATE_NAME = "积财宝.apk";
}
