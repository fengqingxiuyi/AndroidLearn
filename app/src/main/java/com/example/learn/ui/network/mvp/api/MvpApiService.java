package com.example.learn.ui.network.mvp.api;

import com.example.learn.ui.network.mvp.bean.MvpBean;
import com.example.learn.ui.network.mvp.bean.MvpSecondBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface MvpApiService {

    @GET("/firstjavaweb/hello")
    Observable<MvpBean> getData();

    @GET("/firstjavaweb/hello")
    Observable<MvpSecondBean> getSecondData();

}
