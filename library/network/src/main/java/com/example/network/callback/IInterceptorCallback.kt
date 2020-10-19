package com.example.network.callback

import okhttp3.Interceptor

/**
 * @author fqxyi
 * @date 2018/2/27
 * 拦截器回调接口
 */
interface IInterceptorCallback {

    fun getInterceptorList(): List<Interceptor>?

    fun getNetworkInterceptorList(): List<Interceptor>?
}