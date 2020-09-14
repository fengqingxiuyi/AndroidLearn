package com.example.learn.jetpack.viewmodel.apiservice;

import com.example.learn.jetpack.viewmodel.bean.MainBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * ApiService
 */
public interface MainApiService {

    @GET("/firstmybatis/queryName")
    Observable<MainBean> getData(@Query("id") int id);

}
