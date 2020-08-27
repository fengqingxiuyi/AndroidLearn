package com.example.network.interceptor;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author fqxyi
 * @date 2018/2/27
 * 日志拦截器
 */
public class LogInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        Log.d("LogInterceptor", String.format(
                "Sending request %s on %s%n%s",
                request.url(),
                chain.connection(),
                request.headers()));
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        Log.d("LogInterceptor", String.format(
                "Received response for %s in %.1fms%n%s",
                response.request().url(),
                (double) (t2 - t1) / 1000000.0D,
                response.headers()));
        return response;
    }

}
