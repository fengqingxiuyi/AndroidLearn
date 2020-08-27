# network

网络组件 基于Retrofit2+RxJava2+GSON的网络框架

## 目录结构说明

```
network
  |--src/main/java/com/example/network //多级目录折叠
       |--bean //基类数据Bean
       |--callback //回调接口
       |--converter //数据转换器
       |--exception //异常管理
       |--global //全局工具
       |--interceptor //拦截器
       |--observer //观察者
       |--tag //请求标识 & 携带数据 & 缓存key
       |--utils //请求工具
       |--RequestManager //单例，请求管理类
```

# 可以忽略的内容

HelloActivity访问的是自己编写的JavaWeb程序，地址：<https://github.com/fengqingxiuyi/FirstJavaWeb>

# 使用方式

## 初始化

```kotlin
RequestManager.get().init(AppGlobal.application, "http://192.168.10.134:9900/",
    object : IObserverCallback {
        override fun <T : Any?> getObserver(
            reqTag: ReqTag,
            responseCallback: IResponseCallback<T>
        ): IBaseObserver<T> {
            return BusinessObserver(
                reqTag,
                responseCallback
            )
        }
    },
    object : IInterceptorCallback {
        override fun getInterceptorList(): MutableList<Interceptor> {
            val interceptorList: MutableList<Interceptor> = ArrayList()
            interceptorList.add(FormToJsonInterceptor())
            return interceptorList
        }

        override fun getNetworkInterceptorList(): MutableList<Interceptor> {
            val interceptorList: MutableList<Interceptor> = ArrayList()
            interceptorList.add(HttpCacheInterceptor(this@MyApplication))
            return interceptorList
        }
    })
```

## 创建ApiService

### ~~方式1 响应数据类型 不需要继承父类~~

```java
public interface HelloApiService {

    @GET("API地址")
    Observable<HttpResult<响应数据类型>> getData(请求参数);

}
```

### ~~方式2 响应数据类型 需要继承BaseEntity~~

```java
public interface HelloApiService {

    @GET("API地址")
    Observable<响应数据类型> getData(请求参数);

}
```

### 方式3 响应数据类型 不需要继承BaseEntity

```java
public interface HelloApiService {

    @GET("API地址")
    Observable<响应数据类型> getData(请求参数);

}
```

## 发起请求

```java
//请求标识，该参数可不加
ReqTag reqTag = new ReqTag();
reqTag.setTag(1);

RequestManager.get().async(
        reqTag,
        RequestManager.get().create(ApiService类).getData(请求参数),
        new IResponseCallback<响应数据类型>() {
            @Override
            public void onSuccess(ReqTag reqTag, 响应数据类型 response) {
                //请求成功
            }

            @Override
            public void onError(ReqTag reqTag, ErrorBean errorBean) {
                //请求错误
            }
        }
);
```