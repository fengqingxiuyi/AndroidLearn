package com.example.network.interceptor

import android.text.TextUtils
import android.util.LruCache
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 * @author fqxyi
 * @date 2018/2/27
 * HttpUrl拦截器，用于切换API
 */
class HttpUrlInterceptor : Interceptor {

    //新的API地址
    private var api: String? = null

    //保证请求path、参数和参数值正确传递到新的HttpUrl上
    private val mCache = LruCache<String, String>(20)

    /**
     * 以下代码删除了try-catch，是为了解决当请求超时之后，会再发起一次请求的问题
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        //api地址为空时直接返回
        if (TextUtils.isEmpty(api)) {
            return chain.proceed(request)
        }
        val apiHttpUrl = HttpUrl.parse(api!!) ?: return chain.proceed(request)
        //获取旧的HttpUrl
        val oldHttpUrl = request.url()
        //校验oldHttpUrl是否包含在apiHttpUrl中
        if (oldHttpUrl.toString().contains(apiHttpUrl.toString())) {
            return chain.proceed(request)
        }
        val builder = oldHttpUrl.newBuilder()
        //获取缓存的path、参数和参数值
        if (TextUtils.isEmpty(mCache[getKey(apiHttpUrl, oldHttpUrl)])) {
            for (i in 0 until oldHttpUrl.pathSize()) {
                //当删除了上一个 index, PathSegment 的 item 会自动前进一位, 所以 remove(0) 就好
                builder.removePathSegment(0)
            }
            val newPathSegments: MutableList<String> = ArrayList()
            newPathSegments.addAll(apiHttpUrl.encodedPathSegments())
            newPathSegments.addAll(oldHttpUrl.encodedPathSegments())
            for (PathSegment in newPathSegments) {
                builder.addEncodedPathSegment(PathSegment)
            }
        } else {
            builder.encodedPath(mCache[getKey(apiHttpUrl, oldHttpUrl)])
        }
        //构造新的HttpUrl
        val newHttpUrl = builder
            .scheme(apiHttpUrl.scheme())
            .host(apiHttpUrl.host())
            .port(apiHttpUrl.port())
            .build()
        //缓存path、参数和参数值
        if (TextUtils.isEmpty(mCache[getKey(apiHttpUrl, oldHttpUrl)])) {
            mCache.put(getKey(apiHttpUrl, oldHttpUrl), newHttpUrl.encodedPath())
        }
        //返回新的HttpUrl
        return chain.proceed(
            request.newBuilder()
                .url(newHttpUrl)
                .build()
        )
    }

    private fun getKey(domainUrl: HttpUrl, url: HttpUrl): String {
        return domainUrl.encodedPath() + url.encodedPath()
    }

    /**
     * 运行时切换API
     *
     * @param api api形式：https://www.fqxyi.top/
     */
    fun switchApi(api: String?) {
        this.api = api
    }
}