package com.example.network;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.LruCache;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.network.bean.NetBean;
import com.example.network.callback.IInterceptorCallback;
import com.example.network.callback.IObserverCallback;
import com.example.network.callback.IResponseCallback;
import com.example.network.converter.GsonConverterTypeAdapter;
import com.example.network.interceptor.HttpUrlInterceptor;
import com.example.network.interceptor.LogInterceptor;
import com.example.network.observer.DefaultObserver;
import com.example.network.observer.IBaseObserver;
import com.example.network.tag.ReqTag;
import com.example.network.utils.RequestUtil;
import com.example.utils.ActivitiesManager;
import com.example.utils.BuildConfig;
import com.example.utils.storage.CacheUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author fqxyi
 * @date 2018/2/27
 * 网络请求管理类
 */
public class RequestManager {

    /**
     * 单例
     **/

    private RequestManager() {

    }

    private static class RequestManagerHolder {
        private static final RequestManager INSTANCE = new RequestManager();
    }

    public static RequestManager get() {
        return RequestManagerHolder.INSTANCE;
    }

    /**
     * 成员变量
     **/
    //应用上下文
    private Context appContext;
    //保证整个APP只存在一个retrofit变量
    private Retrofit retrofit;
    //service阈值
    private int maxServiceLimit = 20;
    //缓存ApiService，减少重复创建
    private LruCache<String, Object> serviceCache;
    //HttpUrl拦截器，用于切换API
    private HttpUrlInterceptor httpUrlInterceptor;
    //observer阈值
    private int maxObserverLimit = 20;
    //关联Activity与观察者
    private LruCache<String, HashMap<String, Object>> observerCache;
    //memory cache 阈值
    private int maxMemoryCacheLimit = 10;
    //内存缓存
    private LruCache<String, Object> memoryCache;
    //observer回调接口
    private IObserverCallback observerCallback;

    public void init(Application application, String baseUrl) {
        init(application, baseUrl, null, null);
    }

    public void init(Application application, String baseUrl, IObserverCallback observerCallback) {
        init(application, baseUrl, observerCallback, null);
    }

    public void init(Application application, String baseUrl, IInterceptorCallback interceptorCallback) {
        init(application, baseUrl, null, interceptorCallback);
    }

    /**
     * 初始化
     *
     * @param application         使用Application的上下文
     * @param baseUrl             App中最常用的主机地址
     * @param observerCallback    自定义Observer
     * @param interceptorCallback 自定义Interceptor
     */
    public void init(Application application, String baseUrl, IObserverCallback observerCallback,
                     IInterceptorCallback interceptorCallback) {
        appContext = application;
        this.observerCallback = observerCallback;
        //
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(initOkHttpClient(appContext, interceptorCallback))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonConverterTypeAdapter.registerTypeAdapter()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        retrofit = builder.build();
    }

    /**
     * 初始化OkHttpClient
     * OkHttp timeout 分析： https://zhuanlan.zhihu.com/p/31640388
     */
    private OkHttpClient initOkHttpClient(Context context, IInterceptorCallback interceptorCallback) {
        /**
         * OkHttp 默认最大并发数 64，单域名最大并发 5，但这个是灵活的，是允许使用者去按照自己的复杂场景做相应的配置。
         * 比如某个应用才一个域名，但请求非常频繁，就可以调整 Dispatcher 的配置，比如设置单域名最大并发 10，最大并发 10。
         */
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(10);
        //
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                //简单实现缓存，缓存大小为10MB，仅当服务器支持缓存时才有用，除非自定义Interceptor，如：HttpCacheInterceptor
                .cache(new Cache(context.getCacheDir(), 1024 * 1024 * 10))
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .dispatcher(dispatcher)
                //设置进行连接失败重试
                .retryOnConnectionFailure(false);
        //添加拦截器
        addInterceptor(builder, interceptorCallback);
        return builder.build();
    }

    /**
     * 添加拦截器
     */
    private void addInterceptor(OkHttpClient.Builder builder, IInterceptorCallback interceptorCallback) {
        //调用者定义的拦截器
        if (interceptorCallback != null) {
            List<Interceptor> interceptorList = interceptorCallback.getInterceptorList();
            if (interceptorList != null && interceptorList.size() > 0) {
                for (Interceptor interceptor : interceptorList) {
                    if (interceptor != null) {
                        builder.addInterceptor(interceptor);
                    }
                }
            }
            List<Interceptor> networkInterceptorList = interceptorCallback.getNetworkInterceptorList();
            if (networkInterceptorList != null && networkInterceptorList.size() > 0) {
                for (Interceptor interceptor : networkInterceptorList) {
                    if (interceptor != null) {
                        builder.addNetworkInterceptor(interceptor);
                    }
                }
            }
        }
        //HttpUrl拦截器，用于切换API
        builder.addInterceptor(httpUrlInterceptor = new HttpUrlInterceptor());
        if (BuildConfig.DEBUG) {
            //日志拦截器
            builder.addInterceptor(new LogInterceptor());
        }
    }

    /**
     * 创建API Service类
     */
    public <S> S create(Class<S> cls) {
        if (cls == null) {
            throw new RuntimeException("API Service Is Null");
        }
        if (serviceCache == null) {
            serviceCache = new LruCache<>(maxServiceLimit);
        }
        Object object = serviceCache.get(cls.getSimpleName());
        S s;
        if (object != null) {
            try {
                s = (S) serviceCache.get(cls.getSimpleName());
            } catch (Exception e) {
                s = retrofit.create(cls);
                serviceCache.put(cls.getSimpleName(), s);
            }
        } else {
            s = retrofit.create(cls);
            serviceCache.put(cls.getSimpleName(), s);
        }
        return s;
    }

    /**
     * 同步请求
     */
    public <T> T sync(@NonNull Call<T> call) {
        try {
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> void asyncForKotlin(@NonNull Observable<T> observable,
                                                   @NonNull IResponseCallback responseCallback) {
        asyncForKotlin(new ReqTag(), observable, responseCallback);
    }

    /**
     * IResponseCallback不携带泛型，需要自己在回调中判断，兼容Kotlin中的真泛型问题
     */
    public <T> void asyncForKotlin(@NonNull ReqTag reqTag,
                                                   @NonNull Observable<T> observable,
                                                   @NonNull IResponseCallback responseCallback) {
        async(reqTag, observable, responseCallback);
    }

    public <T> void async(@NonNull Observable<T> observable,
                                          @NonNull IResponseCallback<T> responseCallback) {
        async(new ReqTag(), observable, responseCallback);
    }

    /**
     * 异步请求 使用统一的数据结构
     *
     * @param reqTag           请求标识
     * @param observable       被观察者
     * @param responseCallback 响应监听器
     */
    public <T> void async(@NonNull ReqTag reqTag,
                                          @NonNull Observable<T> observable,
                                          @NonNull IResponseCallback<T> responseCallback) {
        getConcatObservable(reqTag, observable).subscribe(getObserver(reqTag, responseCallback));
    }

    /**
     * 如果reqTag.getCacheKey()不为空，则先取内存缓存中的内容，
     * 如果内存缓存中有内容，则发送onNext事件，再发送onComplete事件表示读缓存结束；如果内存缓存中无内容，则取磁盘缓存中的内容；
     * 如果磁盘缓存中有内容，则发送onNext事件，再发送onComplete事件表示读缓存结束，无内容则直接发送onComplete事件；
     * 最后获取网络内容
     */
    private <T> Observable<T> getConcatObservable(@NonNull final ReqTag reqTag,
                                                                  @NonNull Observable<T> netObservable) {
        Observable<T> concatObservable;
        if (!TextUtils.isEmpty(reqTag.getCacheKey())) { //如果缓存key不为空则缓存数据
            Observable<T> cacheObservable = getCacheObservable(reqTag);
            concatObservable = Observable.concatArrayDelayError(
                    cacheObservable,
                    getNetObservable(reqTag, netObservable, true));
        } else {
            concatObservable = getNetObservable(reqTag, netObservable, false);
        }
        return concatObservable;
    }

    private <T> Observable<T> getCacheObservable(@NonNull final ReqTag reqTag) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                T t = null;
                try {
                    Object obj = null;
                    if (memoryCache != null) {
                        obj = memoryCache.get(reqTag.getCacheKey());
                    }
                    if (obj == null) {
                        obj = CacheUtil.getCacheFile(appContext, reqTag.getCacheKey());
                    }
                    t = (T) obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (t instanceof NetBean) {
                    ((NetBean) t).cache = true;
                    emitter.onNext(t);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private <T> Observable<T> getNetObservable(@NonNull final ReqTag reqTag,
                                                               @NonNull Observable<T> netObservable,
                                                               final boolean cache) {
        return netObservable.subscribeOn(Schedulers.io()).map(new Function<T, T>() {
            @Override
            public T apply(T t) throws Exception {
                if (!cache) {
                    return t;
                }
                try {
                    if (memoryCache == null) {
                        memoryCache = new LruCache<>(maxMemoryCacheLimit);
                    }
                    memoryCache.put(reqTag.getCacheKey(), t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    CacheUtil.saveCacheFile(appContext, reqTag.getCacheKey(), t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return t;
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }

    private <T> IBaseObserver<T> getObserver(@NonNull ReqTag reqTag,
                                             @NonNull IResponseCallback<T> responseCallback) {
        IBaseObserver<T> observer = null;
        if (observerCallback != null && !reqTag.isUseDefaultObserver()) {
            observer = observerCallback.getObserver(reqTag, responseCallback);
        }
        if (observer == null) {
            observer = new DefaultObserver<>(reqTag, responseCallback);
        }
        //缓存responseCallback，在Activity销毁的时候同时销毁Observer
        if (observerCache == null) {
            observerCache = new LruCache<>(maxObserverLimit);
        }
        String keyName = getKeyName();
        if (TextUtils.isEmpty(keyName)) {
            return observer;
        }
        HashMap<String, Object> map = observerCache.get(keyName);
        if (map == null) {
            map = new HashMap<>();
        }
        String callbackName = RequestUtil.getResponseCallbackName(responseCallback);
        if (map.get(callbackName) == null) {
            map.put(callbackName, observer);
            observerCache.put(keyName, map);
        }
        return observer;
    }

    /**
     * 获取缓存key的名称
     */
    private String getKeyName() {
        String key = null;
        try {
            Activity activity = ActivitiesManager.getInstance().getLastAliveActivity();
            if (!(activity instanceof FragmentActivity)) {
                return null;
            }
            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
            List<Fragment> fragments = fragmentManager.getFragments();
            Fragment visibleFragment = null;
            for (Fragment fragment : fragments) {
                if (!fragment.isHidden() && fragment.getView() != null) {
                    visibleFragment = fragment;
                }
            }
            if (visibleFragment == null) {
                key = RequestUtil.getClassSimpleName(activity);
            } else {
                key = RequestUtil.getClassSimpleName(visibleFragment);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return key;
    }

    /**
     * 运行时切换API
     *
     * @param api api形式：https://www.fqxyi.top/
     */
    public void switchApi(String api) {
        clearMemoryCache();
        CacheUtil.delDir(appContext);
        if (httpUrlInterceptor != null) {
            httpUrlInterceptor.switchApi(api);
        }
    }

    /**
     * 销毁资源
     */
    public void destroy(Activity activity) {
        try {
            destroy(RequestUtil.getClassSimpleName(activity));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy(Fragment fragment) {
        try {
            destroy(RequestUtil.getClassSimpleName(fragment));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void destroy(String keyName) {
        try {
            if (TextUtils.isEmpty(keyName) || observerCache == null || observerCache.get(keyName) == null) return;
            HashMap<String, Object> map = observerCache.get(keyName);
            if (map == null) return;
            for (String key : map.keySet()) {
                IBaseObserver observer = (IBaseObserver) map.get(key);
                if (observer != null) {
                    observer.dispose();
                    observer = null;
                }
            }
            map.clear();
            observerCache.remove(keyName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearMemoryCache() {
        try {
            if (memoryCache != null) {
                memoryCache.evictAll();
                memoryCache = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
