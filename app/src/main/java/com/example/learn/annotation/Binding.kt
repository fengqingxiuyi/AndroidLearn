package com.example.learn.annotation

import android.app.Activity
import com.example.utils.LogUtil

/**
 * @author fqxyi
 * @date 2020/8/14
 */
object Binding {

    /**
     * 运行时注解
     */
    fun bindRuntime(activity: Activity) {
        try {
            activity.javaClass.declaredFields.forEach { field ->
                val bindView = field.getAnnotation(BindRuntime::class.java)
                bindView?.apply {
                    field.isAccessible = true
                    field.set(activity, activity.findViewById(value))
                }
            }
        } catch (e : Exception) {
            LogUtil.e("Binding Runtime", e.message ?: "未知错误")
        }
    }

}