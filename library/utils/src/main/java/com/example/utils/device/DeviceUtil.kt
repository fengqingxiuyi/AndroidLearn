package com.example.utils.device

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager

/**
 * @author fqxyi
 * @date 2020/8/20
 * 设备相关工具类：屏幕大小、状态栏等
 */
object DeviceUtil {

    private var screenWidth = 0
    private var screenHeight = 0

    /**
     * 获取设备屏幕宽度
     */
    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        if (screenWidth > 0) {
            return screenWidth
        }
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        wm.defaultDisplay.getSize(point)
        screenWidth = point.x
        screenHeight = point.y
        return screenWidth
    }

    /**
     * 获取设备屏幕高度
     */
    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        if (screenHeight > 0) {
            return screenHeight
        }
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        wm.defaultDisplay.getSize(point)
        screenWidth = point.x
        screenHeight = point.y
        return screenHeight
    }

    /**
     * 获取虚拟功能键高度
     */
    fun getVirtualBarHeight(context: Context): Int {
        var vh = 0
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val dm = DisplayMetrics()
        try {
            val c = Class.forName("android.view.Display")
            val method = c.getMethod("getRealMetrics", DisplayMetrics::class.java)
            method.invoke(display, dm)
            vh = dm.heightPixels - windowManager.defaultDisplay.height
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return vh
    }

    /**
     * 获取虚拟功能键高度
     */
    fun getVirtualBarHeight(activity: Activity): Int {
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        return activity.window.findViewById<View>(Window.ID_ANDROID_CONTENT).top - frame.top
    }

    /**
     * 判断指定包名的应用是否安装
     */
    fun isPkgInstalled(context: Context, pkgName: String): Boolean {
        var packageInfo: PackageInfo?
        try {
            packageInfo = context.packageManager.getPackageInfo(pkgName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            packageInfo = null
            e.printStackTrace()
        }
        return packageInfo != null
    }

    /**
     * 获取设备基础信息，默认换行符为\n\n
     */
    fun getBuildInfo(): String = getBuildInfo("\n\n")

    /**
     * 获取设备基础信息
     *
     * @param lineBreak 换行符
     */
    fun getBuildInfo(lineBreak: String): String {
        return "手机品牌: " + Build.BRAND + lineBreak +
                "手机型号: " + Build.MODEL + lineBreak +
                "SDK版本: " + Build.VERSION.SDK_INT + lineBreak +
                "手机系统版本: " + Build.VERSION.INCREMENTAL + lineBreak +
                "Android系统版本: " + Build.VERSION.RELEASE
    }
}