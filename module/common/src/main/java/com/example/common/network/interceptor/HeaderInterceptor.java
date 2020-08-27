package com.example.common.network.interceptor;

import android.content.Context;
import android.text.TextUtils;

import com.example.common.UserAgent;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author fqxyi
 * @date 2018/2/27
 * 请求拦截器，在请求执行前做一些操作，比如说设置Header
 */
public class HeaderInterceptor implements Interceptor {
    
    private Context context;

    public HeaderInterceptor(Context context) {
        this.context = context;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = setHeaders(chain.request());
        return chain.proceed(request);
    }

    public Request setHeaders(Request request) {
        Request.Builder builder = request.newBuilder().url(request.url());
        addHeader(builder, "Charset", "UTF-8");
        addHeader(builder, "Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        //增加关闭连接，不让它保持连接 解决连接流意外关闭问题
        addHeader(builder, "Connection", "close");
        //设备信息+应用版本号
        addHeader(builder, "User-Agent", UserAgent.getUserAgent(context));
        return builder.build();
    }

    private void addHeader(Request.Builder builder, String key, String values){
        if(null != builder && !TextUtils.isEmpty(key) && !TextUtils.isEmpty(values)){
            builder.addHeader(key, values);
        }
    }

}
