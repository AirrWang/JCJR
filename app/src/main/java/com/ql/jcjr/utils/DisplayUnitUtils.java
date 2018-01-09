package com.ql.jcjr.utils;

import android.content.Context;
import android.content.res.TypedArray;

import com.ql.jcjr.R;
import com.ql.jcjr.application.JcbApplication;

/**
 * 用来进行dp、sp转换成px 的工具类
 */
public class DisplayUnitUtils {

    private DisplayUnitUtils() {
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue //@param scale   （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(float pxValue,Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue //@param scale    （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(float dipValue,Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue //@param fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue,Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue //@param fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue,Context context) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕的scaledDensity
     *
     * @return
     */
    public static float getDisplayDensity(Context context) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return fontScale;
    }

    /**
     * 获取屏幕的宽度
     *
     * @return
     */
    public static int getDisplayWidth(Context context) {
        int width = Math.min(context.getResources().getDisplayMetrics().widthPixels
                , context.getResources().getDisplayMetrics().heightPixels);
        // LogUtils.d("widthPixels ="+context.getResources().getDisplayMetrics().widthPixels);
        // LogUtils.d("heightPixels ="+context.getResources().getDisplayMetrics().heightPixels);
        return width;
    }
    /**
     * 获取屏幕的宽度
     *
     * @return
     */
    public static int getDisplayWidth() {
        int width = getDisplayWidth(JcbApplication.getInstance().getApplicationContext());
        return width;
    }

    /**
     * 获取屏幕的高度
     *
     * @return
     */
    public static int getDisplayHeight(Context context) {
        int height = //context.getResources().getDisplayMetrics().heightPixels;
                Math.max(context.getResources().getDisplayMetrics().widthPixels
                        , context.getResources().getDisplayMetrics().heightPixels);
        return height;
    }


    /**
     * 根据传入的值，计算出
     *
     * @param uiHeight UI设计图给出的尺寸
     */
    public static int scaleHeightByDisplayWidth(int uiHeight,Context context) {
        final int iosWidth = 640;
        int width = getDisplayWidth(context);
        int result = width * uiHeight / iosWidth;
        if (result == 0) {
            return uiHeight;
        }
        return result;
    }

    /**
     * 根据传入的值，计算出
     *
     * @param uiHeight UI设计图给出的尺寸
     * @param iosStand IOS的标准宽度
     */

    public static int scaleHeightByDisplayWidth(int uiHeight, int iosStand,Context context) {
        final int iosWidthStand = iosStand;
        int width = getDisplayWidth(context);
        //LogUtils.d("width = "+width);
        if (iosWidthStand == 0) {
            return scaleHeightByDisplayWidth(uiHeight,context);
        }
        int result = width * uiHeight / iosWidthStand;
        return result;
    }

    /**
     * 获取系统默认的actionbar的高度
     */
    public static int getActionBarHeight(Context context) {
        int defHeight;
        defHeight = context.getResources().getDimensionPixelOffset(R.dimen.actionbar_height);
        int height = defHeight;
        try {
            TypedArray actionBarSizeTypedArray = context.obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
            height = actionBarSizeTypedArray.getDimensionPixelOffset(0, defHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (height <= 48) {
            height = defHeight;
        }
        return height;
    }

    /**
     * 获取宽度的比例值
     *
     * @param scale      需要计算的比例
     * @param subtractor 在计算比例之前需要减去的值
     */
    public static int getDisplayScaleWidth(float scale, int subtractor,Context context) {
        float result = (getDisplayWidth(context) - subtractor) * scale;
        return (int) result;
    }

    /**
     * 获取高度的比例值
     *
     * @param scale      需要计算的比例
     * @param subtractor 在计算比例之前需要减去的值
     */
    public static int getDisplayScaleHeight(float scale, int subtractor,Context context) {
        float result = (getDisplayHeight(context) - subtractor) * scale;
        return (int) result;
    }

    /**
     * 获取系统状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取屏幕的scaledDensity
     *
     * @return
     */
    public static float getDisplayScaleDensity() {
        float fontScale = JcbApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return fontScale;
    }

}
