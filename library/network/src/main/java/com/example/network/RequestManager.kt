package com.example.network

import android.app.Activity
import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.util.LruCache
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.network.bean.NetBean
import com.example.network.callback.IInterceptorCallback
import com.example.network.callback.IObserverCallback
import com.example.network.callback.IResponseCallback
import com.example.network.converter.GsonConverterTypeAdapter.registerTypeAdapter
import com.example.network.interceptor.HttpUrlInterceptor
import com.example.network.interceptor.LogInterceptor
import com.example.network.observer.DefaultObserver
import com.example.network.observer.IBaseObserver
import com.example.network.tag.ReqTag
import com.example.network.utils.RequestUtil.getClassSimpleName
import com.example.network.utils.RequestUtil.getResponseCallbackName
import com.example.utils.BuildConfig
import com.example.utils.activity.ActivitiesManager.Companion.getInstance
import com.example.utils.storage.FileUtil
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author fqxyi
 * @date 2018/2/27
 * 网络请求管理类
 */
class RequestManager
/**
 * 单例
 */
private constructor() {

    private object RequestManagerHolder {
        val INSTANCE = RequestManager()
    }

    /**
     * 成员变量
     */
    //应用上下文
    private lateinit var appContext: Context

    //保证整个APP只存在一个retrofit变量
    private lateinit var retrofit: Retrofit

    //service阈值
    private val maxServiceLimit = 20

    //缓存ApiService，减少重复创建
    private val serviceCache by lazy {
        LruCache<String, Any>(maxServiceLimit)
    }

    //HttpUrl拦截器，用于切换API
    private var httpUrlInterceptor: HttpUrlInterceptor? = null

    //observer阈值
    private val maxObserverLimit = 20

    //关联Activity与观察者
    private val observerCache by lazy {
        LruCache<String, HashMap<String, Any>>(maxObserverLimit)
    }

    //memory cache 阈值
    private val maxMemoryCacheLimit = 10

    //内存缓存
    private val memoryCache by lazy {
        LruCache<String, Any>(maxMemoryCacheLimit)
    }

    //observer回调接口
    private var observerCallback: IObserverCallback? = null

    /**
     * 初始化
     *
     * @param application         使用Application的上下文
     * @param baseUrl             App中最常用的主机地址
     * @param observerCallback    自定义Observer
     * @param interceptorCallback 自定义Interceptor
     */
    @JvmOverloads
    fun init(
        application: Application,
        baseUrl: String,
        observerCallback: IObserverCallback? = null,
        interceptorCallback: IInterceptorCallback? = null
    ) {
        appContext = application
        this.observerCallback = observerCallback
        //
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(initOkHttpClient(application, interceptorCallback))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(registerTypeAdapter()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        retrofit = builder.build()
    }

    /**
     * 初始化OkHttpClient
     * OkHttp timeout 分析： https://zhuanlan.zhihu.com/p/31640388
     */
    private fun initOkHttpClient(
        context: Context,
        interceptorCallback: IInterceptorCallback?
    ): OkHttpClient {
        /**
         * OkHttp 默认最大并发数 64，单域名最大并发 5，但这个是灵活的，是允许使用者去按照自己的复杂场景做相应的配置。
         * 比如某个应用才一个域名，但请求非常频繁，就可以调整 Dispatcher 的配置，比如设置单域名最大并发 10，最大并发 10。
         */
        val dispatcher = Dispatcher()
        dispatcher.maxRequestsPerHost = 10
        //
        val builder =
            OkHttpClient.Builder() //简单实现缓存，缓存大小为10MB，仅当服务器支持缓存时才有用，除非自定义Interceptor，如：HttpCacheInterceptor
                .cache(Cache(context.cacheDir, 1024 * 1024 * 10))
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .dispatcher(dispatcher) //设置进行连接失败重试
                .retryOnConnectionFailure(false)
        //添加拦截器
        addInterceptor(builder, interceptorCallback)
        return builder.build()
    }

    /**
     * 添加拦截器
     */
    private fun addInterceptor(
        builder: OkHttpClient.Builder,
        interceptorCallback: IInterceptorCallback?
    ) {
        //调用者定义的拦截器
        if (interceptorCallback != null) {
            val interceptorList =
                interceptorCallback.getInterceptorList()
            if (!interceptorList.isNullOrEmpty()) {
                for (interceptor in interceptorList) {
                    builder.addInterceptor(interceptor)
                }
            }
            val networkInterceptorList =
                interceptorCallback.getNetworkInterceptorList()
            if (!networkInterceptorList.isNullOrEmpty()) {
                for (interceptor in networkInterceptorList) {
                    builder.addNetworkInterceptor(interceptor)
                }
            }
        }
        //HttpUrl拦截器，用于切换API
        builder.addInterceptor(HttpUrlInterceptor().also { httpUrlInterceptor = it })
        if (BuildConfig.DEBUG) {
            //日志拦截器
            builder.addInterceptor(LogInterceptor())
        }
    }

    /**
     * 创建API Service类
     */
    fun <S> create(cls: Class<S>?): S {
        if (cls == null) {
            throw RuntimeException("API Service Is Null")
        }
        val `object` = serviceCache[cls.simpleName]
        var s: S
        if (`object` != null) {
            try {
                s = serviceCache[cls.simpleName] as S
            } catch (e: Exception) {
                s = retrofit.create(cls)
                serviceCache.put(cls.simpleName, s)
            }
        } else {
            s = retrofit.create(cls)
            serviceCache.put(cls.simpleName, s)
        }
        return s
    }

    /**
     * 同步请求
     */
    fun <T> sync(call: Call<T>): T? {
        return try {
            call.execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun <T> async(
        observable: Observable<T>,
        responseCallback: IResponseCallback<T>
    ) {
        async(ReqTag(), observable, responseCallback)
    }

    /**
     * 异步请求 使用统一的数据结构
     *
     * @param reqTag           请求标识
     * @param observable       被观察者
     * @param responseCallback 响应监听器
     */
    fun <T> async(
        reqTag: ReqTag,
        observable: Observable<T>,
        responseCallback: IResponseCallback<T>
    ) {
        getConcatObservable(reqTag, observable).subscribe(getObserver(reqTag, responseCallback))
    }

    /**
     * 如果reqTag.getCacheKey()不为空，则先取内存缓存中的内容，
     * 如果内存缓存中有内容，则发送onNext事件，再发送onComplete事件表示读缓存结束；如果内存缓存中无内容，则取磁盘缓存中的内容；
     * 如果磁盘缓存中有内容，则发送onNext事件，再发送onComplete事件表示读缓存结束，无内容则直接发送onComplete事件；
     * 最后获取网络内容
     */
    private fun <T> getConcatObservable(
        reqTag: ReqTag,
        netObservable: Observable<T>
    ): Observable<T> {
        val concatObservable: Observable<T>
        concatObservable = if (!TextUtils.isEmpty(reqTag.cacheKey)) { //如果缓存key不为空则缓存数据
            val cacheObservable: Observable<T> = getCacheObservable(reqTag)
            Observable.concatArrayDelayError(
                cacheObservable,
                getNetObservable(reqTag, netObservable, true)
            )
        } else {
            getNetObservable(reqTag, netObservable, false)
        }
        return concatObservable
    }

    private fun <T> getCacheObservable(reqTag: ReqTag): Observable<T> {
        return Observable.create(ObservableOnSubscribe<T> { emitter ->
            var t: T? = null
            try {
                var obj = memoryCache[reqTag.cacheKey]
                if (obj == null) {
                    obj = FileUtil.getCacheFile(
                        appContext,
                        reqTag.cacheKey!!
                    )
                }
                t = obj as T?
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (t is NetBean) {
                (t as NetBean).cache = true
                emitter.onNext(t)
            }
            emitter.onComplete()
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun <T> getNetObservable(
        reqTag: ReqTag,
        netObservable: Observable<T>,
        cache: Boolean
    ): Observable<T> {
        return netObservable.subscribeOn(Schedulers.io())
            .map(Function<T, T> { t ->
                if (!cache) {
                    return@Function t
                }
                try {
                    memoryCache.put(reqTag.cacheKey, t)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    FileUtil.saveCacheFile(
                        appContext!!,
                        reqTag.cacheKey!!,
                        t!!
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                t
            }).observeOn(AndroidSchedulers.mainThread())
    }

    private fun <T> getObserver(
        reqTag: ReqTag,
        responseCallback: IResponseCallback<T>
    ): IBaseObserver<T> {
        var observer: IBaseObserver<T>? = null
        if (observerCallback != null && !reqTag.isUseDefaultObserver) {
            observer = observerCallback!!.getObserver(reqTag, responseCallback)
        }
        if (observer == null) {
            observer = DefaultObserver(reqTag, responseCallback)
        }
        //缓存responseCallback，在Activity销毁的时候同时销毁Observer
        val keyName = getKeyName()
        if (TextUtils.isEmpty(keyName)) {
            return observer
        }
        var map = observerCache[keyName]
        if (map == null) {
            map = HashMap()
        }
        val callbackName = getResponseCallbackName(responseCallback)
        if (map[callbackName] == null) {
            map[callbackName] = observer
            observerCache.put(keyName, map)
        }
        return observer
    }

    /**
     * 获取缓存key的名称
     */
    private fun getKeyName(): String? {
        var key: String? = null
        try {
            val activity =
                getInstance().getLastAliveActivity() as? FragmentActivity ?: return null
            val fragmentManager =
                activity.supportFragmentManager
            val fragments =
                fragmentManager.fragments
            var visibleFragment: Fragment? = null
            for (fragment in fragments) {
                if (!fragment.isHidden && fragment.view != null) {
                    visibleFragment = fragment
                }
            }
            key =
                visibleFragment?.let { getClassSimpleName(it) } ?: getClassSimpleName(activity)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return key
    }

    /**
     * 运行时切换API
     *
     * @param api api形式：https://www.fqxyi.top/
     */
    fun switchApi(api: String?) {
        clearMemoryCache()
        FileUtil.delCacheDir(appContext)
        httpUrlInterceptor?.switchApi(api)
    }

    /**
     * 销毁资源
     */
    fun destroy(activity: Activity) {
        try {
            destroy(getClassSimpleName(activity))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun destroy(fragment: Fragment) {
        try {
            destroy(getClassSimpleName(fragment))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun destroy(keyName: String?) {
        try {
            if (TextUtils.isEmpty(keyName) || observerCache[keyName] == null
            ) return
            val map = observerCache[keyName] ?: return
            for (key in map.keys) {
                var observer = map[key] as IBaseObserver<*>?
                if (observer != null) {
                    observer.dispose()
                    observer = null
                }
            }
            map.clear()
            observerCache.remove(keyName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun clearMemoryCache() {
        try {
            memoryCache.evictAll()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        @JvmStatic
        fun get(): RequestManager {
            return RequestManagerHolder.INSTANCE
        }
    }
}