# network

网络组件 基于Retrofit2+RxJava2+GSON的网络框架

[network](https://bintray.com/fqxyi/maven/network)已上传到jcenter，可通过以下方式依赖：

## Maven

```
<dependency>
  <groupId>com.fqxyi</groupId>
  <artifactId>network</artifactId>
  <version>1.0.3</version>
  <type>pom</type>
</dependency>
```

## Gradle

```
compile 'com.fqxyi:network:1.0.3'
```

## lvy

```
<dependency org='com.fqxyi' name='network' rev='1.0.3'>
  <artifact name='network' ext='pom' ></artifact>
</dependency>
```

# 可以忽略的内容

HelloActivity访问的是自己编写的JavaWeb程序，地址：<https://github.com/fengqingxiuyi/FirstJavaWeb>

# 使用方式

## 初始化

```java
RequestManager.get().init(application, baseUrl);
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