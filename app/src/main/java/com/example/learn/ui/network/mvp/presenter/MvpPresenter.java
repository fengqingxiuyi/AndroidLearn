package com.example.learn.ui.network.mvp.presenter;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.common.ui.loading.LoadingUtil;
import com.example.learn.ui.network.mvp.api.MvpApiService;
import com.example.learn.ui.network.mvp.bean.MvpBean;
import com.example.learn.ui.network.mvp.bean.MvpSecondBean;
import com.example.learn.ui.network.mvp.contract.MvpContract;
import com.example.network.RequestManager;
import com.example.network.bean.ErrorBean;
import com.example.network.callback.IResponseCallback;
import com.example.network.tag.ReqTag;

/**
 * 业务逻辑层
 */
public class MvpPresenter implements MvpContract.Presenter, IResponseCallback {

    private Activity activity;
    private MvpContract.View view;

    public MvpPresenter(Activity activity, MvpContract.View view) {
        this.activity = activity;
        this.view = view;
    }

    @Override
    public void mainRequest() {
        LoadingUtil.showLoading(activity);
        RequestManager.get().async(
                new ReqTag(1),
                RequestManager.get().create(MvpApiService.class).getData(),
                this
        );
    }

    @Override
    public void secondRequest() {
        RequestManager.get().async(
                new ReqTag(2),
                RequestManager.get().create(MvpApiService.class).getSecondData(),
                this
        );
    }

    @Override
    public void onSuccess(@NonNull ReqTag reqTag, @NonNull Object response) {
        LoadingUtil.hideLoading(activity);
        if (reqTag.getTag() == 1) {
            view.mainResponse((MvpBean) response);
        } else {
            view.secondResponse((MvpSecondBean) response);
        }
    }

    @Override
    public void onError(@NonNull ReqTag reqTag, @NonNull ErrorBean errorBean) {
        LoadingUtil.hideLoading(activity);
        view.error(errorBean);
    }

}
