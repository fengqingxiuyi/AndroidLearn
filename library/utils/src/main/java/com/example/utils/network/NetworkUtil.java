package com.example.utils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 网络状态工具类
 */
public class NetworkUtil {

    /**
     * 判断网络连接是否可用
     */
    public static boolean isNetworkAvailable(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        }
        return networkInfo.isAvailable();
    }

    /**
     * 判断网络是否连接
     */
    public static boolean isNetworkConnected(@NonNull Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        // 获取NetworkInfo对象
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        if (networkInfo == null || networkInfo.length == 0) {
            return false;
        }
        for (NetworkInfo info : networkInfo) {
            // 判断当前网络状态是否为连接状态
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否已连接Wifi
     */
    public static boolean isWifiConnected(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断是否已连接移动网络
     */
    public static boolean isMobileConnected(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 获取当前网络连接状态
     */
    public static int getConnectedType(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isAvailable()) {
            return ni.getType();
        }
        return -1;
    }

    /**
     * 获取当前网络类型名
     */
    public static String getConnectedTypeName(@NonNull Context context) {
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

    /**
     * 获取连接Wifi时本机的IP地址
     */
    public static String getWifiIpAddress(Context context) {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wm.isWifiEnabled()) {
            WifiInfo wi = wm.getConnectionInfo();
            int ia = wi.getIpAddress();
            return (ia & 0xFF) + "." +
                    (ia >> 8 & 0xFF) + "." +
                    (ia >> 16 & 0xFF) + "." +
                    (ia >> 24 & 0xFF);
        }
        return null;
    }

    /**
     * 获取连接移动网络时本机的IP地址
     */
    public static String getMobileIpAddress() {
        try {
            Enumeration<NetworkInterface> enNif = NetworkInterface.getNetworkInterfaces();
            // 遍历所用的网络接口
            while(enNif.hasMoreElements()) {
                // 得到每一个网络接口绑定的所有ip
                NetworkInterface nif = enNif.nextElement();
                Enumeration<InetAddress> enIa = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while(enIa.hasMoreElements()) {
                    InetAddress ia = enIa.nextElement();
                    if (!ia.isLoopbackAddress()) {
                        return ia.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取MAC地址，不准确
     */
    public static String getMacAddress(Context context) {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();
        String macStr = wi.getMacAddress();
        if (TextUtils.isEmpty(macStr)) {
            macStr = "null";
        }
        return macStr;
    }

}
