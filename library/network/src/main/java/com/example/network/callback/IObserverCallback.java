package com.example.network.callback;

import androidx.annotation.NonNull;

import com.example.network.observer.IBaseObserver;
import com.example.network.tag.ReqTag;

/**
 * @author fqxyi
 * @date 2018/2/27
 * observer回调接口
 */
public interface IObserverCallback {

    <T> IBaseObserver<T> getObserver(
            @NonNull ReqTag reqTag, @NonNull IResponseCallback<T> responseCallback);
}
