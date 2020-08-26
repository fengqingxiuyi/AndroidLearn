package com.example.utils.encrypt;

import android.text.TextUtils;

import com.example.utils.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 通用Base64加解密
 */
public class Base64Util {

    private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    /**
     * Base64加密
     */
    public static String encode(String data) {
        if (TextUtils.isEmpty(data)) {
            return "";
        }
        return encode(data.getBytes());
    }

    /**
     * Base64加密
     */
    public static String encode(byte[] data) {
        if (data == null) {
            return "";
        }
        int len = data.length;
        if (len == 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder(len * 3 / 2);

        int end = len - 3;
        int i = 0;
        int n = 0;

        while (i <= end) {
            int d = ((((int) data[i]) & 0x0ff) << 16)
                    | ((((int) data[i + 1]) & 0x0ff) << 8)
                    | (((int) data[i + 2]) & 0x0ff);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append(legalChars[d & 63]);

            i += 3;

            if (n++ >= 14) {
                n = 0;
                buf.append(" ");
            }
        }

        if (i == len - 2) {
            int d = ((((int) data[i]) & 0x0ff) << 16)
                    | ((((int) data[i + 1]) & 255) << 8);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append("=");
        } else if (i == len - 1) {
            int d = (((int) data[i]) & 0x0ff) << 16;

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append("==");
        }

        return buf.toString();
    }

    /**
     * Base64解密
     */
    public static String decodeGetStr(String s) {
        return new String(decode(s));
    }

    /**
     * Base64解密
     */
    public static byte[] decode(String s) {
        byte[] decodedBytes = new byte[0];
        if (TextUtils.isEmpty(s)) {
            return decodedBytes;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            decode(s, bos);
            decodedBytes = bos.toByteArray();
        } catch (IOException e) {
            LogUtil.e("Error while decoding BASE64: " + e.toString());
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                LogUtil.e(e);
            }
        }
        return decodedBytes;
    }

    private static void decode(String s, OutputStream os) throws IOException {
        int i = 0;

        int len = s.length();

        while (true) {
            while (i < len && s.charAt(i) <= ' ')
                i++;

            if (i == len)
                break;

            int tri = (decode(s.charAt(i)) << 18)
                    + (decode(s.charAt(i + 1)) << 12)
                    + (decode(s.charAt(i + 2)) << 6)
                    + (decode(s.charAt(i + 3)));

            os.write((tri >> 16) & 255);
            if (s.charAt(i + 2) == '=')
                break;
            os.write((tri >> 8) & 255);
            if (s.charAt(i + 3) == '=')
                break;
            os.write(tri & 255);

            i += 4;
        }
    }

    private static int decode(char c) {
        if (c >= 'A' && c <= 'Z')
            return ((int) c) - 65;
        else if (c >= 'a' && c <= 'z')
            return ((int) c) - 97 + 26;
        else if (c >= '0' && c <= '9')
            return ((int) c) - 48 + 26 + 26;
        else
            switch (c) {
                case '+':
                    return 62;
                case '/':
                    return 63;
                case '=':
                    return 0;
                default:
                    throw new RuntimeException("unexpected code: " + c);
            }
    }
}
