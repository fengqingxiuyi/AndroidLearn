package com.example.network.callback;

import java.util.List;

import okhttp3.Interceptor;

/**
 * @author fqxyi
 * @date 2018/2/27
 * 拦截器回调接口
 */
public interface IInterceptorCallback {

    List<Interceptor> getInterceptorList();

    List<Interceptor> getNetworkInterceptorList();

}
