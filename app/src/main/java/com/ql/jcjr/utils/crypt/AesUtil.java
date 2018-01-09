package com.ql.jcjr.utils.crypt;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * ClassName: AesUtil
 * Description:
 * Author: Administrator
 * Date: Created on 202016/10/17.
 */
public class AesUtil {

    public static final String KEY_ALGORITHM = "AES";
    //ECB模式（电子密码本模式：Electronic codebook）
    //ECB是最简单的块密码加密模式，加密前根据加密块大小（如AES为128位）分成若干块，之后将每块使用相同的密钥单独加密，解密同理。
    //ECB模式由于每块数据的加密是独立的因此加密和解密都可以并行计算，ECB模式最大的缺点是相同的明文块会被加密成相同的密文块，
    // 这种方法在某些环境下不能提供严格的数据保密性。
    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    //CBC模式（密码分组链接：Cipher-block chaining）
    //CBC模式对于每个待加密的密码块在加密前会先与前一个密码块的密文异或然后再用加密器加密。第一个明文块与一个叫初始化向量的数据块异或。
    //CBC模式相比ECB有更高的保密性，但由于对每个数据块的加密依赖与前一个数据块的加密所以加密无法并行。与ECB一样在加密前需要对数据进行填充，
    // 不是很适合对流数据进行加密。
    public static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";

    public static final String VIPARA   = "0102030405060708"; //iv参数设置 （ios、android、wp7、java 统一）

    //摇旺加密密钥
    public static final String SECRETKEY = "mf91yaowanglicai";

    /** 摇旺加密方法 **/
    public static byte[] encrypt(String content, String secretkey) {
        SecretKeySpec key = new SecretKeySpec(secretkey.getBytes(), KEY_ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] byteContent = content.getBytes("UTF-8");
            byte[] result = cipher.doFinal(byteContent);

            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

    //刘总加密方式
    public static String encryptString1(String srcStr ,String password) {
        String resultStr ="";
        byte[] result = encrypt1(srcStr, password);
        resultStr = bytesToHexString(result);
        return resultStr;
    }

    public static byte[] encrypt1(String content, String password)  {
        try {
            SecretKeySpec skeySpec = getKey1(password);
            //创建密码器
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
            //IvParameterSpec iv = new IvParameterSpec(VIPARA.getBytes());

            //初始化向量，默认16个0，由于是分组加密，所以下一组的iv，就用前一组的加密的密文来充当，我们可以指定IV来进行加解密，加大破解难度。
            byte[] ivByte = {(byte)0XCA,(byte)0X8A,(byte)0X88,(byte)0X78,(byte)0XF1,(byte)0X45,(byte)0XC9,(byte)0XB9,(byte)0XB3,(byte)0XC3,(byte)0X1A,(byte)0X1F,(byte)0X15,(byte)0XC3,(byte)0X4A,(byte)0X6D};
            IvParameterSpec iv = new IvParameterSpec(ivByte);

            // 以加密的方式用密钥初始化此 Cipher
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            //对明文字节数组进行加密
            byte[] encrypted = cipher.doFinal(content.getBytes());
            return encrypted ;

        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }

    private static SecretKeySpec getKey1(String strKey) {
        //byte[] arrBTmp = strKey.getBytes();
        // byte[] arrBTmp = {(byte)0xB0,(byte)0x0D,(byte)0xDF,(byte)0x9D,(byte)0x93,(byte)0xE1,(byte)0x99,(byte)0xEF,(byte)0xEA,(byte)0xE9,(byte)0x67,(byte)0x80,(byte)0x5E,(byte)0x0A,(byte)0x52,(byte)0x28};
        byte[] arrBTmp = {(byte)0xAA,(byte)0x70,(byte)0xCD,0x77,0x12,0x5F,(byte)0xC3,0x04,(byte)0xFE,(byte)0xBF,0x5E,0x3E,(byte)0xA4,(byte)0xE9,(byte)0xAF,(byte)0xBD};//{0xAA,0x70,0xCD,0x77,0x12,0x5F,0xC3,0x04,0xFE,0xBF,0x5E,0x3E,0xA4,0xE9,0xAF,0xBD};
        byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）

        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        //根据给定的enCodeFormat字节数组构造一个用AES算法加密的密钥。
        SecretKeySpec skeySpec = new SecretKeySpec(arrB, KEY_ALGORITHM);

        return skeySpec;
    }

    /**
     * 字节数组转化为16进制字符串
     * @param src
     * @return
     *
     */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 进行MD5加密
     *
     * @param info
     *            要加密的信息
     * @return String 加密后的字符串
     */
    public static String encryptToMD5(String info) {
        byte[] digesta = null;
        try {
            // 得到一个md5的消息摘要
            MessageDigest alga = MessageDigest.getInstance("MD5");
            // 添加要进行计算摘要的信息
            // alga.update(info.getBytes());
            alga.update(info.getBytes("utf-8"));
            // 得到该摘要
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 将摘要转为字符串
        String rs = byte2hex(digesta);
        return rs;
    }

    /**
     * 将二进制转化为16进制字符串
     *
     * @param b
     *            二进制字节数组
     * @return String
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    public static byte[] decrypt1(byte[] content, String password) {
        try {
            SecretKeySpec skeySpec = getKey1(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
            //IvParameterSpec iv = new IvParameterSpec(VIPARA.getBytes());
            //在CBC（不光是DES算法）模式下，iv通过随机数（或伪随机）机制产生是一种比较常见的方法。iv的作用主要是用于产生密文的第一个block，
            // 以使最终生成的密文产生差异（明文相同的情况下），使密码攻击变得更为困难，除此之外iv并无其它用途。因此iv通过随机方式产生是一种十分简便、有效的途径
            byte[] ivByte = {(byte)0XCA,(byte)0X8A,(byte)0X88,(byte)0X78,(byte)0XF1,(byte)0X45,(byte)0XC9,(byte)0XB9,(byte)0XB3,(byte)0XC3,(byte)0X1A,(byte)0X1F,(byte)0X15,(byte)0XC3,(byte)0X4A,(byte)0X6D};
            IvParameterSpec iv = new IvParameterSpec(ivByte);

            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(content);
            return original;
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }

    public static String decryptString1(String destStr ,String password) {
        String resultStr = "";
        byte[] result = hexStringToBytes(destStr);
        result = decrypt1(result, password);
        resultStr = new String(result);

        resultStr = resultStr.replace("&", "&amp;");
        //resultStr = resultStr.replace(">", "&gt;");
        //resultStr = resultStr.replace("<", "&lt;");
        resultStr = resultStr.replace("'", "&apos;");
        resultStr = resultStr.replace("ufeff", "");
        return resultStr;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     *
     * 16进制字符串转化成字节数组
     * @param hexString
     * @return
     *
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
}


