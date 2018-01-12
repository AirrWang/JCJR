package com.ql.jcjr.constant;

/**
 * Description:
 * Author: connorlin
 * Date: Created on 202016/10/23.
 */
public class Global {

    public static final String RESULT_PARAM_MISS = "1001";

    public static final String MESSAGE_CODE_FIRST = "1";
    public static final String MESSAGE_CODE_SECOND = "2";
    public static final String MESSAGE_CODE_THIRD = "3";

    public static final int STATUS_CODE_FIRST = 1;
    public static final int STATUS_CODE_SECOND = 2;

    public static final String RESULT_SUCCESS = "00";
    public static final String RESULT_TOKEN_WRONG = "001";

    public static final String RESULT_CODE = "RSPCODE";
    public static final String RESULT_MESSAGE = "RSPMSG";

    public static final int RESULT_CODE_200 = 200;  // 请求成功
    public static final String RESULT_EXCEPTION = "连接异常！";
    public static final String RESULT_CONNECT_EXCEPTION = "网络连接异常！";
    public static final String RESULT_CONNECT_TIMEOUT = "连接超时！";
    public static final String SOCKET_CONNECT_TIMEOUT = "数据读取异常！";
    public static final String LOADING = "正在加载...";
    public static final int NET_TIMEOUT_MILLIS = 15000;    // 15秒
    public static final int SOCKET_TIMEOUT_MILLIS = 60000;    // 60秒


    public static final String FLAG_REGISTER = "register";
    public static final String FLAG_FORGET = "forget";
    public static final String FLAG_FORGET_TRANS = "forget_trans";

    public static final int WHATE_TIMER_TASK = 1;

    public static final String STATUS_AVAILABLE = "0";
    public static final String STATUS_USED = "1";
    public static final String STATUS_OVERDUE = "2";

    public static final String STATUS_AUTO_BID_SETTING = "1";
    public static final String STATUS_AUTO_BID_UNSETTING = "2";

    public static final String STATUS_PASS= "1";
    public static final String STATUS_UN_PASS = "0";

    public static final String HB_TYPE_DK = "dikou_hb";
    public static final String HB_TYPE_JX = "cash_hb";
    public static final String HB_TYPE_FX = "fanxian";
}
