package com.example.utils.encrypt;

import com.example.utils.LogUtil;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 实现加密解密
 */
public class AesUtils {

    public static final String DEFAULT_CODING = "utf-8";
    //算法/模式/补码方式
    public static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     */
    public static String encrypt(String content, String key) throws Exception {
        byte[] input = content.getBytes(DEFAULT_CODING);

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(key.getBytes(DEFAULT_CODING));
        SecretKeySpec skc = new SecretKeySpec(digest, "AES");
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, skc);

        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
        return Base64Util.encode(cipherText);
    }

    /**
     * 将二进制转换成16进制
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 解密
     */
    public static String decrypt(String encrypted, String seed) throws Exception {
        byte[] keyb = seed.getBytes(DEFAULT_CODING);
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(keyb);
        SecretKeySpec skey = new SecretKeySpec(thedigest, "AES");
        Cipher dcipher = Cipher.getInstance("AES");
        dcipher.init(Cipher.DECRYPT_MODE, skey);

        byte[] clearbyte = dcipher.doFinal(toByte(encrypted));
        return new String(clearbyte);
    }

    /**
     * 字符串转字节数组
     */
    private static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }


    /**
     * 将16进制转换为二进制
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    // 加密
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            LogUtil.e("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            LogUtil.e("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes(DEFAULT_CODING);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(DEFAULT_CODING));

        return Base64Util.encode(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }
}
