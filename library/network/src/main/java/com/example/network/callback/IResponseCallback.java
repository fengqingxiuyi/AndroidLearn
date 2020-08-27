package com.example.network.callback;

import androidx.annotation.NonNull;

import com.example.network.bean.ErrorBean;
import com.example.network.tag.ReqTag;

/**
 * @author fqxyi
 * @date 2018/2/27
 * 网络请求回调接口
 */
public interface IResponseCallback<T> {

    void onSuccess(@NonNull ReqTag reqTag, @NonNull T response);

    void onError(@NonNull ReqTag reqTag, @NonNull ErrorBean errorBean);

}
