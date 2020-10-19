package com.example.network.callback

import com.example.network.observer.IBaseObserver
import com.example.network.tag.ReqTag

/**
 * @author fqxyi
 * @date 2018/2/27
 * observer回调接口
 */
interface IObserverCallback {
    fun <T> getObserver(
        reqTag: ReqTag, responseCallback: IResponseCallback<T>
    ): IBaseObserver<T>?
}