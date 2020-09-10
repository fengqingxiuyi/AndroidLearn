package com.example.utils.app

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import com.example.utils.BuildConfig

/**
 * 监听APP生命周期变化
 */
class AppLifecycleMonitor : ActivityLifecycleCallbacks {

    //前后台状态回调监听器
    private var foregroundListener: ForegroundListener? = null

    //计数器
    private var count = 0
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        synchronized(this) {
            if (count == 0) { //后台切换到前台
                foregroundListener?.getForegroundStatus(true)
            }
            count++
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {
        if (BuildConfig.DEBUG) {
            //应用程序最大可用内存
            val maxMemory = Runtime.getRuntime().maxMemory().toInt() / 1024 / 1024
            //应用程序已获得内存
            val totalMemory = Runtime.getRuntime().totalMemory().toInt() / 1024 / 1024.toLong()
            //应用程序已获得内存中未使用内存
            val freeMemory = Runtime.getRuntime().freeMemory().toInt() / 1024 / 1024.toLong()
            Log.i(
                "AppLifecycleMonitor",
                activity.toString() + "---> maxMemory=" + maxMemory + "M, totalMemory=" + totalMemory + "M, freeMemory=" + freeMemory + "M"
            )
            //内存使用检测
            val rateOfUsed = 1.0f * Runtime.getRuntime().totalMemory() / Runtime.getRuntime().maxMemory()
            Log.i(activity.toString(), "totalMemory / maxMemory ：$rateOfUsed")
        }
        synchronized(this) {
            count--
            if (count == 0) { //前台切换到后台
                foregroundListener?.getForegroundStatus(false)
            }
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    fun setForegroundListener(foregroundListener: ForegroundListener?) {
        this.foregroundListener = foregroundListener
    }

    interface ForegroundListener {
        fun getForegroundStatus(foreground: Boolean)
    }
}