package com.ql.jcjr.utils;

/**
 * Created by Liuchao on 2016/9/22.
 */
public class AppConfigCommon {

    //正式
    public static final String REQUEST_URL = "http://116.228.192.101:33104/services/rs/ql/mServer";

    //线下
    public static final String OFFLINE_TRANSACTION_URL = "http://116.228.192.101:33001/";

    public static final String ENCRYPT_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCArbzixjUkRdE14cSR8m9KFnTHTeAopYaJwcKCp0SB+R/OZ1kore6G73opjm5PZEMSFlmmt0aBc6wBg18zc706Bnu1LSZY2hTMC8MCKRbsw4QRRo6ToohjhEaytQN/Buq55zlGEOTnIHEXmphwh4yzf8W28wn156NN9wnaAzBrnwIDAQAB";

    // Base on 720p
    public final static int UI_WIDTH  = 720;
    public final static int UI_HEIGHT = 1280;

    // Base on density 2
    public final static int UI_DENSITY = 2;

    /**
     * 用来保存拍照图片的目录
     */
    public final static String SHARE_IMAGE_NAME = "launcher_icon.png";
    public final static String SHARE_QR_NAME = "qr.png";
    public final static String SHARE_RECEIVE_QR_NAME = "receive_qr.png";
    public final static String SHARE_PUBLIC_NAME = "public.png";

    //线下
    public static final String DEVICE_SIGN_REQUEST_CODE = "170000";//设备签到
    public static final String OFFLINE_TRANSACTION_REQUEST_CODE = "180001";//线下交易
    public static final String AES_Password = "dynamicode";// AES密钥明文

}
