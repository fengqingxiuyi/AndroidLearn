package com.example.network.interceptor;

import android.text.TextUtils;
import android.util.LruCache;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author fqxyi
 * @date 2018/2/27
 * HttpUrl拦截器，用于切换API
 */
public class HttpUrlInterceptor implements Interceptor {

    //新的API地址
    private String api;
    //保证请求path、参数和参数值正确传递到新的HttpUrl上
    private LruCache<String, String> mCache = new LruCache<>(20);

    /**
     * 以下代码删除了try-catch，是为了解决当请求超时之后，会再发起一次请求的问题
     */
    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //api地址为空时直接返回
        if (TextUtils.isEmpty(api)) {
            return chain.proceed(request);
        }
        HttpUrl apiHttpUrl = HttpUrl.parse(api);
        if (apiHttpUrl == null) {
            return chain.proceed(request);
        }
        //获取旧的HttpUrl
        HttpUrl oldHttpUrl = request.url();
        //校验oldHttpUrl是否包含在apiHttpUrl中
        if (oldHttpUrl.toString().contains(apiHttpUrl.toString())) {
            return chain.proceed(request);
        }
        HttpUrl.Builder builder = oldHttpUrl.newBuilder();
        //获取缓存的path、参数和参数值
        if (TextUtils.isEmpty(mCache.get(getKey(apiHttpUrl, oldHttpUrl)))) {
            for (int i = 0; i < oldHttpUrl.pathSize(); i++) {
                //当删除了上一个 index, PathSegment 的 item 会自动前进一位, 所以 remove(0) 就好
                builder.removePathSegment(0);
            }
            List<String> newPathSegments = new ArrayList<>();
            newPathSegments.addAll(apiHttpUrl.encodedPathSegments());
            newPathSegments.addAll(oldHttpUrl.encodedPathSegments());
            for (String PathSegment : newPathSegments) {
                builder.addEncodedPathSegment(PathSegment);
            }
        } else {
            builder.encodedPath(mCache.get(getKey(apiHttpUrl, oldHttpUrl)));
        }
        //构造新的HttpUrl
        HttpUrl newHttpUrl = builder
                .scheme(apiHttpUrl.scheme())
                .host(apiHttpUrl.host())
                .port(apiHttpUrl.port())
                .build();
        //缓存path、参数和参数值
        if (TextUtils.isEmpty(mCache.get(getKey(apiHttpUrl, oldHttpUrl)))) {
            mCache.put(getKey(apiHttpUrl, oldHttpUrl), newHttpUrl.encodedPath());
        }
        //返回新的HttpUrl
        return chain.proceed(request.newBuilder()
                .url(newHttpUrl)
                .build());
    }

    private String getKey(HttpUrl domainUrl, HttpUrl url) {
        return domainUrl.encodedPath() + url.encodedPath();
    }

    /**
     * 运行时切换API
     *
     * @param api api形式：https://www.fqxyi.top/
     */
    public void switchApi(String api) {
        this.api = api;
    }

}
