package com.example.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author fqxyi
 * @date 2018/2/27
 * 日志拦截器
 */
class LogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val t1 = System.nanoTime()
        Log.d(
            "LogInterceptor", String.format(
                "Sending request %s on %s%n%s",
                request.url(),
                chain.connection(),
                request.headers()
            )
        )
        val response = chain.proceed(request)
        val t2 = System.nanoTime()
        Log.d(
            "LogInterceptor", String.format(
                "Received response for %s in %.1fms%n%s",
                response.request().url(),
                (t2 - t1).toDouble() / 1000000.0,
                response.headers()
            )
        )
        return response
    }
}