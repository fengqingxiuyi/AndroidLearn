package com.example.utils.activity

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.view.WindowManager
import java.util.*

/**
 * 获取指定包名下的Activity
 */
object ActivityUtil {
    /**
     * 获取App中所有Activity的信息集合
     */
    @JvmStatic
    fun getActivityInfos(context: Context): List<ActivityInfo>? {
        try {
            val packageInfo =
                context.packageManager.getPackageInfo(
                    context.packageName, PackageManager.GET_ACTIVITIES
                )
            return listOf(*packageInfo.activities)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取App中所有Activity的名字集合
     */
    fun getActivityNames(context: Context): List<String> {
        val activityNames = ArrayList<String>()
        try {
            val packageInfo =
                context.packageManager.getPackageInfo(
                    context.packageName, PackageManager.GET_ACTIVITIES
                )
            val activityInfos = listOf(*packageInfo.activities)
            if (activityInfos.isEmpty()) {
                return activityNames
            }
            // 将activityInfos中的name添加到集合activityNames中
            for (activityInfo in activityInfos) {
                activityNames.add(activityInfo.name.substring(activityInfo.name.lastIndexOf(".") + 1))
            }
            return activityNames
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return activityNames
    }

    /**
     * 设置Activity全屏
     */
    fun setFullscreenMode(activity: Activity?) {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}