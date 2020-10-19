package com.example.network.observer

import com.example.network.bean.ErrorBean
import com.example.network.callback.IResponseCallback
import com.example.network.exception.ExceptionManager
import com.example.network.exception.ExceptionManager.getErrorBean
import com.example.network.tag.ReqTag
import io.reactivex.disposables.Disposable

/**
 * @author fqxyi
 * @date 2018/2/27
 * 默认观察者
 */
open class DefaultObserver<T>(//请求标识
    protected var reqTag: ReqTag, //自定义的响应回调
    protected var responseCallback: IResponseCallback<T>
) : IBaseObserver<T> {

    //实现断开Observer与Observable的连接
    private var disposable: Disposable? = null
    override fun onSubscribe(d: Disposable) {
        disposable = d
    }

    override fun onNext(t: T) {
        if (t == null) {
            responseCallback.onError(
                reqTag,
                ErrorBean(ExceptionManager.ERROR_SERVER, "response为空")
            )
        } else {
            responseCallback.onSuccess(reqTag, t)
        }
    }

    override fun onError(e: Throwable) {
        responseCallback.onError(
            reqTag,
            getErrorBean(e)
        )
        dispose()
    }

    override fun onComplete() {
        dispose()
    }

    override fun dispose() {
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
            disposable = null
        }
    }

}