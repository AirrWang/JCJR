package com.ql.jcjr.utils;

import android.content.pm.PackageManager;
import android.os.Build;

import com.ql.jcjr.application.JcbApplication;

/**
 * ClassName: CommonUtils
 * Description:
 * Author: liuchao
 * Date: Created on 202016/10/12.
 */
public class CommonUtils {

    private static int appVersionCode = 0;
    private static String appVersionName = "";
    private static String deviceMode = "";
    private static String deviceBrand = "";
    private static String deviceInfo = "";

    public static String shareUrl = "http://www.jicaibaobao.com/newweixin/index.html";
    public static String shareIcon = "http://www.jicaibaobao.com/themes/soonmes/media/weixin1/images/fxlogo200.jpg";
    public static String shareTitle = "积财金融,专业理财平台";
    public static String shareContent = "完善的风控体系保障 ,多样化产品期限与收益率完美搭配";


    /**
     * 获取VersionCode
     */
    public static int getAppVersionCode() {
        if (appVersionCode == 0) {
            try {
                appVersionCode = JcbApplication.getInstance().appContext.getPackageManager().getPackageInfo(
                        JcbApplication.getInstance().appContext.getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                appVersionCode = 1;
                e.printStackTrace();
            }
        }
        return appVersionCode;
    }

    /**
     * 获取VersionName
     */
    public static String getAppVersionName() {
        if (StringUtils.isBlank(appVersionName)) {
            try {
                appVersionName = JcbApplication.getInstance().appContext.getPackageManager().getPackageInfo(
                        JcbApplication.getInstance().appContext.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                appVersionName = "";
                e.printStackTrace();
            }
        }
        if (StringUtils.isBlank(appVersionName)) {
            appVersionName = "unknown version name " + getDeviceInfo();
        }
        return appVersionName;
    }

    /**
     * 先获取手机型号,后获取手机品牌,如果获取不出来则显示unknown
     */
    public static String getDeviceInfo() {
        deviceInfo = getDeviceModel();
        if (StringUtils.isBlank(deviceInfo)) {
            deviceInfo = getDeviceBrand();
        }
        if (StringUtils.isBlank(deviceInfo)) {
            deviceInfo = "unknown android phone";
        }
        return deviceInfo;
    }

    /**
     * 获取手机型号
     */
    private static String getDeviceModel() {
        if (StringUtils.isBlank(deviceMode)) {
            deviceMode = Build.MODEL; //
        }
        return deviceMode;
    }

    /**
     * 获取手机品牌
     */
    private static String getDeviceBrand() {

        if (StringUtils.isBlank(deviceBrand)) {
            deviceBrand = Build.BRAND;
        }
        return deviceBrand;
    }
}
