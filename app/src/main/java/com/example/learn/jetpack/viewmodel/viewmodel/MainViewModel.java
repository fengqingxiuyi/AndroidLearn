package com.example.learn.jetpack.viewmodel.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.common.base.BaseViewModel;
import com.example.learn.jetpack.viewmodel.bean.MainBean;
import com.example.learn.jetpack.viewmodel.repository.MainRepository;
import com.example.network.bean.ErrorBean;

import org.jetbrains.annotations.NotNull;

/**
 * ViewModel层
 * 负责提供View层需要的接口，执行数据转换的逻辑
 * 好处: 不会在由于configuration改变引起的onDestroy而销毁数据
 */
public class MainViewModel extends BaseViewModel<MainRepository> {

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * 如果有必要的话，可以使用{@link Transformations}执行数据转换操作
     */
    public LiveData<String> getResponseData() {
        final MutableLiveData<MainBean> liveData = mRepository.getResponseData();
        //执行数据转换操作并获得转换后的数据
        LiveData<String> newLiveData = Transformations.switchMap(liveData, new Function<MainBean, LiveData<String>>() {
            @Override
            public LiveData<String> apply(MainBean mainBean) {
                final MutableLiveData<String> newLiveData = new MutableLiveData<>();
                if (mainBean == null || mainBean.data == null) {
                    newLiveData.setValue(null);
                } else {
                    newLiveData.setValue(mainBean.data.name);
                }
                return newLiveData;
            }
        });
        //返回转换后的数据
        return newLiveData;
    }

    public LiveData<ErrorBean> getErrorData() {
        return mRepository.getErrorData();
    }

    /**
     * 发起请求
     */
    public void loadData() {
        mRepository.loadData();
    }

    @NotNull
    @Override
    protected MainRepository getRepository() {
        return new MainRepository(context);
    }
}
