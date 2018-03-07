package com.ql.jcjr.utils;

import android.graphics.Paint;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Liuchao on 2016/9/23.
 */
public class StringUtils {

    public static boolean isBlank(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String s) {
        return !StringUtils.isBlank(s);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static boolean isNull(String obj) {
        return obj == null;
    }

    public static boolean isNotNull(String obj) {
        return obj != null;
    }


    /**
     * 判断一个字符串是不是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumericString(String str) {
        return str.matches("[0-9]+");
    }

    /**
     * 判断一个字符串是不是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isPhoneNumber(String mobileNo) {
        String regex = "^[1][0-9]{10}$";
        return Pattern.matches(regex, mobileNo);
    }

    public static boolean isValidEMail(String eMail) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return Pattern.matches(regex, eMail);
    }

    public static boolean isValidPwd(String pwd) {
        String regex =
                "^(?=.*?[a-zA-Z`~!@#$%^&*()_\\-+={}\\[\\]\\\\|:;\\\"'<>,.?/])[a-zA-Z\\d`~!@#$%^&*" +
                        "()_\\-+={}\\[\\]\\\\|:;\\\"'<>,.?/]{6,20}$";
        return Pattern.matches(regex, pwd);
    }

    public static boolean isValidVerifyCode(String code) {
        String regex = "[0-9]{6}";
        return Pattern.matches(regex, code);
    }

    public static boolean isValidStringDate(String strDate) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date inputBirthday = fmt.parse(strDate);
            if (new Date().after(inputBirthday)) {
                return true;
            } else {
                return false;

            }
        } catch (ParseException e) {
            return false;
        }
    }

    public static String getBirthday(String cardID) {
        String returnDate = null;
        StringBuffer tempStr = null;
        if (cardID != null && cardID.trim().length() > 0) {
            tempStr = new StringBuffer(cardID.substring(6, 14));
            tempStr.insert(6, '-');
            tempStr.insert(4, '-');
        }
        if (tempStr != null && tempStr.toString().trim().length() > 0) {
            returnDate = tempStr.toString();
        }
        return returnDate;
    }

    public static String getGender(String cardID) {
        String returnGender = null;
        if (cardID != null && cardID.trim().length() > 0) {
            returnGender = cardID.substring(16, 17);
            if (Integer.parseInt(returnGender) % 2 == 0) {
                returnGender = "2";
            } else {
                returnGender = "1";
            }
        }
        return returnGender;
    }

    public static boolean isValidZipCode(String zipCode) {
        String regex = "[0-9]\\d{5}(?!\\d)";
        return Pattern.matches(regex, zipCode);
    }

    public static String getCurrentDate() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(c.getTime());

    }

    public String splitIt(String str, int length) {
        int loopCount;
        StringBuffer splitedStrBuf = new StringBuffer();
        loopCount =
                (str.length() % length == 0) ? (str.length() / length) : (str.length() / length +
                        1);
        System.out.println("Will Split into " + loopCount);
        for (int i = 1; i <= loopCount; i++) {
            if (i == loopCount) {
                splitedStrBuf.append(str.substring((i - 1) * length, str.length()));
            } else {
                splitedStrBuf.append(str.substring((i - 1) * length, (i * length)));
            }
        }

        return splitedStrBuf.toString();
    }

    /**
     * 手机号中间四位隐藏
     *
     * @param mobile 手机号
     * @return
     */
    public static String mobileFormat(String mobile) {
        if (isEmpty(mobile)) {
            return mobile;
        }
        String startString = mobile.substring(0, 3);
        String endString = mobile.substring(7);
        return startString + "****" + endString;
    }

    /**
     * 校验输入的是否是字母、数字、汉字
     *
     * @param s
     * @return
     */
    public static boolean isValidEditText(String s) {
        String regex = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";
        return Pattern.matches(regex, s);
    }

    /**
     * 判断是否是Emoji表情
     *
     * @param codePoint
     * @return
     */
    public static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return 表情返回true  否则返回false
     */
    public static boolean isEmojiCharacter(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 限制字符串长度
     *
     * @param source 字符串
     * @param limit  限制的长度
     * @param suffix 截取字符串后添加后缀，如"..."，如果不需要，设为null
     * @return
     */
    public static String limitStringLength(String source, int limit, String suffix) {
        int length = source.length();
        StringBuffer sb = new StringBuffer();
        if (length > limit) {
            sb.append(source.substring(0, limit))
                    .append(suffix);
        }
        return sb.toString();
    }

    /**
     * 含有特殊字符时，截取特殊字符下标之前的字符串(处理图片url时)
     *
     * @param originalStr
     * @param specialStr
     * @return
     */
    public static String isContainSpecialSign(String originalStr, String specialStr) {
        String newStr = originalStr;
        if (originalStr.indexOf(specialStr) > -1) {
            newStr = originalStr.substring(0, originalStr.indexOf(specialStr));
        }
        return newStr;
    }

    /**
     * 生成价格字符串
     */
    public static String formatPriceString(String price) {
        if (isBlank(price)) {
            return price;
        }
        return "￥" + price;
    }

    /**
     * 带有下划线的Textview
     */
    public static void formatUnderlinePrice(TextView view, String price) {
        view.setVisibility(View.GONE);
        if (view != null && StringUtils.isNotBlank(price)) {
            view.setText(price);
            view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            view.setVisibility(View.GONE);
        }
    }


    /**
     * 清除电话号码中的空格
     *
     * @return
     */
    private static String clearBlank(String number) {
        if (StringUtils.isNotBlank(number)) {
            return number.replaceAll(" ", "");
        }
        return number;
    }

    /**
     * 电话号码的长度是否符合
     *
     * @param number
     * @return
     */
    private static boolean numberLength(String number) {
        if (StringUtils.isNotBlank(number)) {
            int len = clearBlank(number).length();
            return (len == 11 || len == 14);
        }
        return false;
    }

    /**
     * 过滤掉电话号码中个的"+86"
     */
    public static String filterPlus86AndBlank(String number) {
        if (StringUtils.isNotBlank(number)) {
            if (number.contains("+86")) {
                return clearBlank(number.replace("+86", ""));
            } else {
                return clearBlank(number);
            }
        }

        return number;
    }

    // 获取手机IP地址
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }


    //*/转换成8DA43EE0格式16进制
    public static final String hexString = "0123456789ABCDEF";
    public static String decode(String bytes,boolean isUpperCase) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(
                bytes.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
                    .indexOf(bytes.charAt(i + 1))));
        if(isUpperCase){
            return new String(baos.toByteArray());
        }
        return new String(baos.toByteArray()).toLowerCase();
    }

    //*/将字节数组中每个字节拆解成2位16进制整数
    public static String encode(String str)
    {
        str = str.toUpperCase();

        // 根据默认编码获取字节数组

        byte[] bytes = str.getBytes();

        StringBuilder sb = new StringBuilder(bytes.length * 2);

        // 将字节数组中每个字节拆解成2位16进制整数

        for (int i = 0; i < bytes.length; i++)

        {

            sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));

            sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));

        }

        return sb.toString();

    }

    public static String getHideCardNum(String cardNum) {
        int length = cardNum.length();
        if(length >= 8) {
            String firstFour = cardNum.substring(0,4);
            String lastFour = cardNum.substring(length - 4,length);
            int hideLength = length - firstFour.length() - lastFour.length();

            String hideTag = "";

            for (int i = 0; i < hideLength; i++){
                hideTag += "*";
            }
            return firstFour + hideTag + lastFour;
        }

        return cardNum;

    }

    public static String formatMoney(Double money) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formatStr = decimalFormat.format(money);
        return formatStr;
    }

    public static String formatMoney(String money) {
        if (StringUtils.isBlank(money)){
            return money;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        double d = Double.valueOf(money);
        String formatStr = decimalFormat.format(d);
        return formatStr;
    }

    /**
     * 使用java正则表达式去掉多余的0
     * @param s
     * @return
     */
    public static String subAllZero(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    //获取手机序列号
    public static String getSerialNo() {
        String serialno = Build.SERIAL;
        //防止得出来的值和imei一样
        if (StringUtils.isNotBlank(serialno) && (!"9774d56d682e549c".equals(serialno))) {
            return serialno;
        }
        //若没有build属性,则通过反射去读取字段
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialno = (String) (get.invoke(c, "ro.serialno", ""));
        } catch (Exception e) {
            e.printStackTrace();
            serialno = null;
        }
        if ("9774d56d682e549c".equals(serialno)) {
            return null;
        }
        return serialno;
    }

    public static SpannableString getSpannableString(String str, int colorId, int start, int end) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(colorId), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static String getHidePhoneNum(String num) {
        int length = num.length();
            String first = num.substring(0,3);
            String last = num.substring(length - 4,length);

            String hideTag = "****";

        return first + hideTag + last;

    }

}

