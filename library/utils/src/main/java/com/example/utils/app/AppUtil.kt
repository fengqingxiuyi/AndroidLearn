package com.example.utils.app

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.text.TextUtils
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * @author fqxyi
 * @date 2020/8/24
 */
object AppUtil {

    /**
     * 获取当前版本
     */
    fun getAppVersionIncludeV(context: Context): String {
        return "V" + getAppVersion(context)
    }

    /**
     * 获取当前版本
     */
    fun getAppVersion(context: Context): String {
        return try {
            context.packageManager.getPackageInfo(
                context.packageName, 0
            ).versionName
        } catch (e: Exception) {
            "1.0.0"
        }
    }

    /**
     * 获取当前版本号
     */
    @JvmStatic
    fun getAppVersionCode(context: Context): Int {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionCode
        } catch (e: Exception) {
            1
        }
    }

    /**
     * 获取当前应用名称
     */
    fun getAppName(context: Context): String {
        return try {
            context.packageManager.getApplicationLabel(context.applicationInfo).toString()
        } catch (e: Exception) {
            ""
        }
    }

    fun isMainProcess(context: Context): Boolean {
        val processAppName = getProcessName()
        return isMainProcess(context, processAppName)
    }

    fun isMainProcess(context: Context, processAppName: String): Boolean {
        return TextUtils.isEmpty(processAppName) ||
                processAppName.equals(context.packageName, ignoreCase = true)
    }

    /**
     * 获取进程号对应的进程名
     */
    fun getProcessName(): String {
        val pid = Process.myPid()
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return ""
    }

    /**
     * 彻底退出应用程序
     */
    fun exitApp(context: Context) {
        try {
            val activityMgr = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityMgr.restartPackage(context.packageName)
            System.exit(0)
            Process.killProcess(Process.myPid())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}