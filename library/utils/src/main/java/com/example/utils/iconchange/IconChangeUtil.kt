package com.example.utils.iconchange

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

/**
 * @author fqxyi
 * @date 2020/8/25
 * 应用ICON切换工具类
 */
object IconChangeUtil {

    /**
     * 判断 component 是否可用
     */
    fun componentEnabled(context: Context, activityPath: String): Boolean {
        return try {
            val pm = context.packageManager
            val cn = ComponentName(context, context.packageName + activityPath)
            pm.getComponentEnabledSetting(cn) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 判断 component 是否被禁用
     */
    fun componentDisabled(context: Context, activityPath: String): Boolean {
        return try {
            val pm = context.packageManager
            val cn = ComponentName(context, context.packageName + activityPath)
            pm.getComponentEnabledSetting(cn) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 启用组件
     */
    fun enableComponent(context: Context, activityPath: String, ACTIVITY_PATH_ARR: Array<String>) {
        if (componentEnabled(context, activityPath)) {
            return
        }
        try {
            val packageManager = context.packageManager
            // 先禁用除需要启用组件之外的组件
            for (item in ACTIVITY_PATH_ARR) {
                if (item == activityPath) {
                    continue
                }
                packageManager.setComponentEnabledSetting(
                    ComponentName(context, context.packageName + item),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
                )
            }
            // 再启用需要启用的组件
            packageManager.setComponentEnabledSetting(
                ComponentName(context, context.packageName + activityPath),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0
            ) // 0表示立刻杀死应用，并刷新ICON
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 打开应用
     * @param uri intent 数据
     */
    fun openApp(context: Context, uri: Uri?, ACTIVITY_PATH_ARR: Array<String>) {
        try {
            for (item in ACTIVITY_PATH_ARR) {
                if (componentDisabled(context, item)) {
                    continue
                }
                val intent = Intent()
                val cn = ComponentName(
                    context, context.packageName + item
                )
                intent.component = cn
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.data = uri
                context.startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}