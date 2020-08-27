package com.example.network.observer;

import androidx.annotation.NonNull;

import com.example.network.bean.ErrorBean;
import com.example.network.callback.IResponseCallback;
import com.example.network.exception.ExceptionManager;
import com.example.network.tag.ReqTag;

import io.reactivex.disposables.Disposable;

/**
 * @author fqxyi
 * @date 2018/2/27
 * 默认观察者
 */
public class DefaultObserver<T> implements IBaseObserver<T> {

    //请求标识
    protected ReqTag reqTag;
    //自定义的响应回调
    protected IResponseCallback<T> responseCallback;
    //实现断开Observer与Observable的连接
    private Disposable disposable;

    public DefaultObserver(@NonNull ReqTag reqTag, @NonNull IResponseCallback<T> responseCallback) {
        this.reqTag = reqTag;
        this.responseCallback = responseCallback;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(final T t) {
        if (t == null) {
            responseCallback.onError(reqTag, new ErrorBean(ExceptionManager.ERROR_SERVER, "response为空"));
        } else {
            responseCallback.onSuccess(reqTag, t);
        }
    }

    @Override
    public void onError(Throwable e) {
        responseCallback.onError(reqTag, ExceptionManager.getErrorBean(e));
        dispose();
    }

    @Override
    public void onComplete() {
        dispose();
    }

    @Override
    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }

}
