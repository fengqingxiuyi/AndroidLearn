package com.example.learn.network.hello;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author fqxyi
 * @date 2018/2/27
 * Api接口声明类
 */
public interface NetworkHelloApiService {

    @GET("/mybatis/queryName")
    Observable<NetworkHelloBean> getData(@Query("id") int id);

}
