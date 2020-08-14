package com.example.jetpacklearn.annotation

import android.app.Activity
import android.util.Log

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
            Log.e("Binding Runtime", e.message ?: "未知错误")
        }
    }

    /**
     * 编译时注解
     */
    fun bindCompile(target: Any) {
        try {
            val classs = target.javaClass
            val claName = classs.name + "_bindView"
            val clazz = Class.forName(claName)

            val bindMethod = clazz.getMethod("bindView", target::class.java)
            val ob = clazz.newInstance()
            bindMethod.invoke(ob, target)
        } catch (e : Exception) {
            Log.e("Binding Compile", e.message ?: "未知错误")
        }
    }

}