package com.example.network.interceptor;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.utils.network.NetworkUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 服务器缓存拦截器，添加自定义的缓存策略
 */
public class HttpCacheInterceptor implements Interceptor {

    private Context context;

    public HttpCacheInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request;
        if (NetworkUtil.isNetworkAvailable(context)) {
            request = chain.request();
        } else {
            // 失效时间为1个月
            int maxStale = 60 * 60 * 24 * 28;
            /*
            Pragma是Http/1.1之前版本遗留的字段，用于做版本兼容，
            但不同的平台对此有不同的实现，所以在使用缓存策略时需要将其屏蔽，避免对缓存策略造成影响。
             */
            request = chain.request().newBuilder()
                    .removeHeader("Pragma")
                    .addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return chain.proceed(request);
    }

}
