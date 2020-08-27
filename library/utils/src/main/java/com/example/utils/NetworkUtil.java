package com.example.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

/**
 * 网络状态工具类
 */
@SuppressLint("MissingPermission")
public class NetworkUtil {

    /**
     * 获取当前网络连接状态
     */
    public static int getConnectedType(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null && ni.isAvailable()) {
                return ni.getType();
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
    }

    /**
     * 获取当前网络类型名
     */
    public static String getConnectedTypeName(Context context) {
        int type = getConnectedType(context);
        switch (type) {
            case -2:
                return "Exception";
            case -1:
                return "网络未连接";
            case 0:
                return "移动网络";
            case 1:
                return "Wifi";
            case 7:
                return "蓝牙";
            case 9:
                return "以太网";
            case 16:
                return "代理";
            case 17:
                return "VPN";
            default:
                return "其他网络";
        }
    }

    public static boolean isNetworkAvailable(@NonNull Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        // 获取NetworkInfo对象
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        if (networkInfo != null && networkInfo.length > 0) {
            for (int i = 0; i < networkInfo.length; i++) {
                // 判断当前网络状态是否为连接状态
                if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * make true current connect service is wifi
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

}
