package com.example.utils

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

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