package com.example.common.network.interceptor

import android.content.Context
import android.text.TextUtils
import com.example.common.UserAgent.getUserAgent
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * @author fqxyi
 * @date 2018/2/27
 * 请求拦截器，在请求执行前做一些操作，比如说设置Header
 */
class HeaderInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = setHeaders(chain.request())
        return chain.proceed(request)
    }

    private fun setHeaders(request: Request): Request {
        val builder = request.newBuilder().url(request.url())
        addHeader(builder, "Charset", "UTF-8")
        addHeader(builder, "Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
        //增加关闭连接，不让它保持连接 解决连接流意外关闭问题
        addHeader(builder, "Connection", "close")
        //设备信息+应用版本号
        addHeader(builder, "User-Agent", getUserAgent(context))
        return builder.build()
    }

    private fun addHeader(
        builder: Request.Builder?,
        key: String,
        values: String
    ) {
        if (null != builder && !TextUtils.isEmpty(key) && !TextUtils.isEmpty(
                values
            )
        ) {
            builder.addHeader(key, values)
        }
    }

}