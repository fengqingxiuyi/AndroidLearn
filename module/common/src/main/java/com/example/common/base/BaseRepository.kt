package com.example.common.base

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.network.RequestManager
import com.example.network.bean.ErrorBean
import com.example.network.callback.IResponseCallback
import com.example.network.tag.ReqTag
import io.reactivex.Observable

abstract class BaseRepository<T>(protected var context: Context) {

    @JvmField
    protected val responseLiveData: MutableLiveData<T> = MutableLiveData()
    @JvmField
    protected val errorLiveData: MutableLiveData<ErrorBean> = MutableLiveData()

    /**
     * 请求数据
     */
    fun loadData() {
        RequestManager.get().async(
            getReqTag(),
            getApiService(),
            object : IResponseCallback<T> {
                override fun onSuccess(reqTag: ReqTag, response: T) {
                    responseSuccess(reqTag, response)
                }

                override fun onError(reqTag: ReqTag, errorBean: ErrorBean) {
                    responseError(reqTag, errorBean)
                }
            }
        )
    }

    /**
     * 获取数据
     */
    open fun getResponseData(): MutableLiveData<T> {
        return responseLiveData
    }

    /**
     * 获取数据
     */
    open fun getErrorData(): MutableLiveData<ErrorBean> {
        return errorLiveData
    }

    protected open fun getReqTag(): ReqTag {
        return ReqTag()
    }

    abstract fun getApiService(): Observable<T>

    abstract fun responseSuccess(reqTag: ReqTag?, response: T)

    abstract fun responseError(reqTag: ReqTag?, errorBean: ErrorBean?)

}