package com.example.social.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * 简单的包装一下工具类，一个方法创建一个类，没必要
 */
public class Utils {

    //两次点击按钮之间的点击间隔不能少于500毫秒
    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;

    /**
     * 防止多次点击
     */
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) < MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    /**
     * finishActivity
     */
    public static void finish(Activity activity, boolean needFinishActivity) {
        if (!needFinishActivity) {
            return;
        }
        if (activity != null) {
            activity.finish();
        }
    }

    /**
     * 提示统一使用，如需接入自定义的toast，可在此处替换
     */
    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 发起网络请求，获取json数据
     *
     * @param urlStr 请求地址
     */
    public static String get(String urlStr) throws IOException {
        if (TextUtils.isEmpty(urlStr)) {
            return "";
        }
        HttpsURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream os = null;
        try {
            // 构造URL
            URL url = new URL(urlStr);
            // 打开连接
            conn = (HttpsURLConnection) url.openConnection();
            // 输入流
            is = conn.getInputStream();
            // 1K的数据缓冲
            byte[] b = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            os = new ByteArrayOutputStream();
            // 开始读取
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
                os.flush();
            }
            return os.toString("UTF-8");
        } finally {
            // 完毕，关闭所有链接
            if (null != conn) {
                conn.disconnect();
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
