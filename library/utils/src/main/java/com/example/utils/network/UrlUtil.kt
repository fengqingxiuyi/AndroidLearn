package com.example.utils.network

import android.net.Uri
import android.text.TextUtils
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*

/**
 * URL操作类，例如：编解码操作
 */
object UrlUtil {
    /**
     * 以UTF-8的编码形式 编码 http url
     */
    fun encodeUrl(url: String): String {
        val uri = Uri.parse(url) ?: return ""
        // 拆分uri path，逐个编码
        val pathSegments = uri.pathSegments
        val encodedPathSegments = ArrayList<String>()
        for (uriPathSegment in pathSegments) {
            try {
                encodedPathSegments.add(URLEncoder.encode(uriPathSegment, "UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                return ""
            }
        }
        // 拆分uri parameters，逐个编码
        val encodedParameters = HashMap<String, String>()
        val parameterNames = uri.queryParameterNames
        for (parameterName in parameterNames) {
            val parameterValue = uri.getQueryParameter(parameterName) ?: ""
            try {
                encodedParameters[parameterName] = URLEncoder.encode(parameterValue, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                return ""
            }
        }
        // 组合编码后的uri
        val builder = StringBuilder()
        builder.append(url.substring(0, url.indexOf(uri.encodedPath!!)))
        for (uriEncodedPathSegment in encodedPathSegments) {
            builder.append("/")
            builder.append(uriEncodedPathSegment)
        }
        builder.append("?")
        for (entry in encodedParameters.entries) {
            builder.append(entry.key)
            builder.append("=")
            builder.append(entry.value)
            builder.append("&")
        }
        if (builder.lastIndexOf("&") == builder.length - 1) {
            builder.delete(builder.lastIndexOf("&"), builder.length)
        }
        if (builder.lastIndexOf("?") == builder.length - 1) {
            builder.delete(builder.lastIndexOf("?"), builder.length)
        }
        // 返回url
        return builder.toString()
    }

    /**
     * 以UTF-8的编码形式 解码 http url
     */
    fun decodeUrl(url: String): String {
        val uri = Uri.parse(url) ?: return ""
        // 拆分uri path，逐个解码
        val pathSegments = uri.pathSegments
        val decodedPathSegments: MutableList<String> =
            ArrayList()
        for (uriPathSegment in pathSegments) {
            try {
                decodedPathSegments.add(URLDecoder.decode(uriPathSegment, "UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                return ""
            }
        }
        // 拆分uri parameters，逐个解码
        val decodedParameters = HashMap<String, String>()
        val parameterNames = uri.queryParameterNames
        for (parameterName in parameterNames) {
            var parameterValue = uri.getQueryParameter(parameterName)
            if (TextUtils.isEmpty(parameterValue)) {
                parameterValue = ""
            }
            try {
                decodedParameters[parameterName] = URLDecoder.decode(parameterValue, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                return ""
            }
        }
        // 组合解码后的uri
        val builder = StringBuilder()
        builder.append(url.substring(0, url.indexOf(uri.encodedPath!!)))
        for (uriEncodedPathSegment in decodedPathSegments) {
            builder.append("/")
            builder.append(uriEncodedPathSegment)
        }
        builder.append("?")
        for (entry in decodedParameters.entries) {
            builder.append(entry.key)
            builder.append("=")
            builder.append(entry.value)
            builder.append("&")
        }
        if (builder.lastIndexOf("&") == builder.length - 1) {
            builder.delete(builder.lastIndexOf("&"), builder.length)
        }
        if (builder.lastIndexOf("?") == builder.length - 1) {
            builder.delete(builder.lastIndexOf("?"), builder.length)
        }
        // 返回url
        return builder.toString()
    }
}