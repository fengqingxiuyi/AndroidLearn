package com.example.learn.jetpack.viewmodel.repository;

import android.content.Context;

import com.example.common.base.BaseRepository;
import com.example.learn.jetpack.viewmodel.apiservice.MainApiService;
import com.example.learn.jetpack.viewmodel.bean.MainBean;
import com.example.network.RequestManager;
import com.example.network.bean.ErrorBean;
import com.example.network.tag.ReqTag;

import io.reactivex.Observable;

/**
 * Repository层
 * 处理从local storage、web service、database、...中得到的数据，并返回给viewModel
 */
public class MainRepository extends BaseRepository<MainBean> {

    //内存存储id，用于切换id，获取不同的请求结果，校验liveData的实现效果
    private int id = 0;

    public MainRepository(Context context) {
        super(context);
    }

    @Override
    public Observable<MainBean> getApiService() {
        if (id == 1) {
            id = 2;
        } else {
            id = 1;
        }
        return RequestManager.get().create(MainApiService.class).getData(id);
    }

    @Override
    public void responseSuccess(ReqTag reqTag, MainBean response) {
        responseLiveData.setValue(response);
    }

    @Override
    public void responseError(ReqTag reqTag, ErrorBean errorBean) {
        errorLiveData.setValue(errorBean);
    }
}
