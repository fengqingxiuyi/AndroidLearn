package com.example.network.utils

import com.example.network.callback.IResponseCallback

/**
 * 请求工具类
 */
object RequestUtil {

    fun getResponseCallbackName(responseCallback: IResponseCallback<*>): String {
        var name = "unknown"
        try {
            name = responseCallback.toString()
            name = name.substring(name.lastIndexOf(".") + 1)
            val aIndex = name.indexOf("@")
            if (aIndex != -1) {
                name = name.substring(0, name.indexOf("@"))
            }
            val sIndex = name.lastIndexOf("$")
            if (sIndex != -1) {
                name = name.substring(0, sIndex)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return name
    }

    fun getClassName(`object`: Any): String {
        var name = "unknown"
        try {
            name = `object`.javaClass.name
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return name
    }

    fun getClassSimpleName(`object`: Any): String? {
        var name: String? = null
        try {
            name = `object`.javaClass.simpleName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return name
    }
}