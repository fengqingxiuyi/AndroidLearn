package com.example.utils.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.text.TextUtils
import java.net.NetworkInterface
import java.net.SocketException

/**
 * 网络状态工具类
 */
object NetworkUtil {
    /**
     * 判断网络连接是否可用
     */
    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo ?: return false
        return networkInfo.isAvailable
    }

    /**
     * 判断网络是否连接
     */
    @JvmStatic
    fun isNetworkConnected(context: Context): Boolean {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // 获取NetworkInfo对象
        val networkInfo = connectivityManager.allNetworkInfo
        if (networkInfo.isNullOrEmpty()) {
            return false
        }
        for (info in networkInfo) {
            // 判断当前网络状态是否为连接状态
            if (info.state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
        return false
    }

    /**
     * 判断是否已连接Wifi
     */
    @JvmStatic
    fun isWifiConnected(context: Context): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return ni != null && ni.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * 判断是否已连接移动网络
     */
    @JvmStatic
    fun isMobileConnected(context: Context): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return ni != null && ni.type == ConnectivityManager.TYPE_MOBILE
    }

    /**
     * 获取当前网络连接状态
     */
    fun getConnectedType(context: Context): Int {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return if (ni != null && ni.isAvailable) {
            ni.type
        } else -1
    }

    /**
     * 获取当前网络类型名
     */
    @JvmStatic
    fun getConnectedTypeName(context: Context): String {
        return when (getConnectedType(context)) {
            -2 -> "Exception"
            -1 -> "网络未连接"
            0 -> "移动网络"
            1 -> "Wifi"
            7 -> "蓝牙"
            9 -> "以太网"
            16 -> "代理"
            17 -> "VPN"
            else -> "其他网络"
        }
    }

    /**
     * 获取连接Wifi时本机的IP地址
     */
    @JvmStatic
    fun getWifiIpAddress(context: Context): String? {
        val wm = context.applicationContext
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wm.isWifiEnabled) {
            val wi = wm.connectionInfo
            val ia = wi.ipAddress
            return (ia and 0xFF).toString() + "." +
                    (ia shr 8 and 0xFF) + "." +
                    (ia shr 16 and 0xFF) + "." +
                    (ia shr 24 and 0xFF)
        }
        return null
    }

    /**
     * 获取连接移动网络时本机的IP地址
     */
    @JvmStatic
    fun getMobileIpAddress(): String? {
        try {
            val enNif =
                NetworkInterface.getNetworkInterfaces()
            // 遍历所用的网络接口
            while (enNif.hasMoreElements()) {
                // 得到每一个网络接口绑定的所有ip
                val nif = enNif.nextElement()
                val enIa = nif.inetAddresses
                // 遍历每一个接口绑定的所有ip
                while (enIa.hasMoreElements()) {
                    val ia = enIa.nextElement()
                    if (!ia.isLoopbackAddress) {
                        return ia.hostAddress
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取MAC地址，不准确
     */
    @SuppressLint("HardwareIds")
    @JvmStatic
    fun getMacAddress(context: Context): String? {
        val wm = context.applicationContext
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wi = wm.connectionInfo
        var macStr = wi.macAddress
        if (TextUtils.isEmpty(macStr)) {
            macStr = "null"
        }
        return macStr
    }
}