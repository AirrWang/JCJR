package com.ql.jcjr.utils.crypt;

import android.util.Base64;

import com.ql.jcjr.utils.AppConfigCommon;
import com.ql.jcjr.utils.LogUtil;
import com.ql.jcjr.utils.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Created by Liuchao on 2016/9/21.
 */
public class RSAEncrypt {

    //加密填充方式
    private static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";
    //非對稱加密算法
    private static final String RSA = "RSA";

    private static final int MAX_DECRYPT_BLOKC = 128;
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * 加密数据　先RSA加密 再Base64
     *
     * @param data
     * @return
     */

    public static String encryptData(String data) {
        byte[] rsaResult;
        try {
            rsaResult = encryptByPublicKey(data.getBytes(), getPublicKey());
        } catch (Exception e) {
            LogUtil.i("decryptData Exceptione e = " + e);
            e.printStackTrace();
            return "";
        }
        return Base64.encodeToString(rsaResult, Base64.DEFAULT);
    }

    /**
     * 解密数据 先Base64解密 再RSA解密
     *
     * @param data
     * @return
     */
    public static String decryptData(String data) {

        if(StringUtils.isBlank(data)) {
            return "";
        }
        try {
            byte[] bases64Result = Base64.decode(data.getBytes(), Base64.DEFAULT);
            byte[] rsaResult = decryptByPublicKey(bases64Result, getPublicKey());
            return new String(rsaResult);
        } catch (Exception e) {
            LogUtil.i("decryptData Exceptione e = " + e);
            e.printStackTrace();
            return "";
        }
    }

    private static PublicKey getPublicKey() {
        try {
            PublicKey publicKey = loadPublicKey(AppConfigCommon.ENCRYPT_KEY);
            return publicKey;
        } catch (Exception e) {
            LogUtil.i("getPublicKey ex = " + e);
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 用公钥加密 每次加密的字节数，不能超过密钥的长度值减去11
     *
     * @param data      需加密数据的byte数据
     * @param publicKey 公钥
     * @return 加密后的byte型数据
     */
    public static byte[] rsaEncrypt(byte[] data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 传入编码数据并返回编码结果
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(publicKeyStr, 0);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    public static PublicKey loadPublicKey(InputStream in) throws Exception {
        try {
            return loadPublicKey(readKey(in));
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 读取密钥信息
     *
     * @param in
     * @return
     * @throws IOException
     */
    private static String readKey(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String readLine = null;
        StringBuilder sb = new StringBuilder();
        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) == '-') {
                continue;
            } else {
                sb.append(readLine);
                sb.append('\r');
            }
        }

        return sb.toString();
    }

    /**
     * 公钥解密
     *
     * @param publicKey   公钥
     * @param encryedData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public static byte[] rsaDecryp(byte[] encryedData, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);// 此处如果写成"RSA"解析的数据前多出来些乱码
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] output = cipher.doFinal(encryedData);
        return output;
    }

    /**
     * 分段解密
     * @param encryptedData
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, PublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOKC) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOKC);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOKC;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 分段加密
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, PublicKey publicKey)
            throws Exception {
        // 对数据加密
        Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

}
