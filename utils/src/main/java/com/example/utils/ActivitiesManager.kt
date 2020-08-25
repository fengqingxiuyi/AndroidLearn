package com.example.utils

import android.app.Activity
import java.util.*

class ActivitiesManager private constructor() {

    companion object {
        var activityStack: Stack<Activity>? = null

        @Volatile
        private var instance: ActivitiesManager? = null

        @JvmStatic
        fun getInstance(): ActivitiesManager {
            if (instance == null) {
                synchronized(ActivitiesManager::class.java) {
                    if (instance == null) {
                        instance =
                            ActivitiesManager()
                    }
                }
            }
            return instance!!
        }
    }

    /**
     * 添加Activity到堆
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }

    fun remove(activity: Activity) {
        activityStack?.remove(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity? {
        return activityStack?.lastElement()
    }

    /**
     * 获取最后一个活着的Activity
     */
    fun getLastAliveActivity(): Activity? {
        activityStack?.let {
            for (i in it.indices.reversed()) {
                if (it[i] != null && !it[i].isFinishing) {
                    return it[i]
                }
            }
        }
        return null
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        activityStack?.let {
            for (activity in it) {
                if (activity.javaClass == cls) {
                    finishActivity(activity)
                }
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity) {
        activityStack?.remove(activity)
        activity.finish()
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        activityStack?.let {
            var i = 0
            val size = it.size
            while (i < size) {
                it[i]?.finish()
                i++
            }
            it.clear()
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivityByName(activity: Activity) {
        activityStack?.let {
            var i = 0
            val size = it.size
            while (i < size) {
                if (null != it[i] && it[i] !== activity) {
                    it[i].finish()
                }
                i++
            }
        }
    }

    /*** 是否包含 某一个 界面  */
    fun isExistByClass(cls: Class<*>): Boolean {
        activityStack?.let {
            for (activity in it) {
                if (activity.javaClass == cls) {
                    return true
                }
            }
        }
        return false
    }
}