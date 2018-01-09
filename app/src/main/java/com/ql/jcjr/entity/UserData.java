package com.ql.jcjr.entity;

import com.ql.jcjr.utils.SharedPreferencesUtils;
import com.ql.jcjr.utils.StringUtils;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class UserData {

    public static UserData getInstance() {
        return Singleton.instance;
    }

    private static class Singleton {
        private static UserData instance = new UserData();
    }


    public String getUSERID() {
        return SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.KEY_USER_ID, "");
    }

    public void setUSERID(String userId) {
         SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.KEY_USER_ID, userId);
    }

    public void setPhoneNumber(String phoneNumber) {
        SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.KEY_PHONE_NUM, phoneNumber);
    }

    public String getPhoneNumber() {
        return SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.KEY_PHONE_NUM, "");
    }

    public void setRealName(String realName) {
        SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.KEY_REAL_NAME, realName);
    }

    public String getRealName() {
        return SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.KEY_REAL_NAME, "");
    }

    public void setIsSetPay(String status) {
        SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.KEY_IS_SET_PAY, status);
    }

    public String getIsSetPay() {
        return SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.KEY_IS_SET_PAY, "");
    }
    public void setIsOpenGesture(boolean isOpen) {
        SharedPreferencesUtils.getInstance().putBoolean(SharedPreferencesUtils.KEY_IS_OPEN_GESTURE, isOpen);
    }

    public boolean getIsOpenGesture() {
        return SharedPreferencesUtils.getInstance().getBoolean(SharedPreferencesUtils.KEY_IS_OPEN_GESTURE, false);
    }

    public void setGestureCipher(String gestureCipher) {
        SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.KEY_GESTURE_CIPHER, gestureCipher);
    }

    public String getGestureCipher() {
        return SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.KEY_GESTURE_CIPHER, "");
    }

    public void setUserIconUrl(String iconUrl) {
        SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.KEY_USER_ICON_URL, iconUrl);
    }

    public String getUserIconUrl() {
        return SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.KEY_USER_ICON_URL, "");
    }

    public boolean isLogin() {
        if(StringUtils.isBlank(getUSERID())) {
            return false;
        }else{
            return true;
        }
    }

    public void setAutoBidMoney(String money) {
        SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.KEY_AUTO_BID_MONEY, money);
    }

    public String getAutoBidMoney() {
        return SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.KEY_AUTO_BID_MONEY, "");
    }

    public void setAutoBidDayStart(String dayStart) {
        SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.KEY_AUTO_BID_DAY_START, dayStart);
    }

    public String getAutoBidDayStart() {
        return SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.KEY_AUTO_BID_DAY_START, "");
    }

    public void setAutoBidDayEnd(String dayEnd) {
        SharedPreferencesUtils.getInstance().putString(SharedPreferencesUtils.KEY_AUTO_BID_DAY_END, dayEnd);
    }

    public String getAutoBidDayEnd() {
        return SharedPreferencesUtils.getInstance().getString(SharedPreferencesUtils.KEY_AUTO_BID_DAY_END, "");
    }

    public void setHideMyMoney(boolean isHide) {
        SharedPreferencesUtils.getInstance().putBoolean(SharedPreferencesUtils.KEY_HIDE_MY_MONEY, isHide);
    }

    public boolean getHideMyMoney() {
        return SharedPreferencesUtils.getInstance().getBoolean(SharedPreferencesUtils.KEY_HIDE_MY_MONEY, false);
    }

    public int getMsgNum() {
        return SharedPreferencesUtils.getInstance().getInt(SharedPreferencesUtils.KEY_MSG_NUM_MSG, 0);
    }

    public void setMsgNum(int num) {
        SharedPreferencesUtils.getInstance().putInt(SharedPreferencesUtils.KEY_MSG_NUM_MSG, num);
    }

    public int getActNum() {
        return SharedPreferencesUtils.getInstance().getInt(SharedPreferencesUtils.KEY_MSG_NUM_ACT, 0);
    }

    public void setActNum(int num) {
        SharedPreferencesUtils.getInstance().putInt(SharedPreferencesUtils.KEY_MSG_NUM_ACT, num);
    }

    public int getNoticeNum() {
        return SharedPreferencesUtils.getInstance().getInt(SharedPreferencesUtils.KEY_MSG_NUM_NOTICE, 0);
    }

    public void setNoticeNum(int num) {
        SharedPreferencesUtils.getInstance().putInt(SharedPreferencesUtils.KEY_MSG_NUM_NOTICE, num);
    }
}
