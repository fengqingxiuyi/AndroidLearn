package com.example.network.callback

import com.example.network.bean.ErrorBean
import com.example.network.tag.ReqTag

/**
 * @author fqxyi
 * @date 2018/2/27
 * 网络请求回调接口
 */
interface IResponseCallback<T> {
    fun onSuccess(reqTag: ReqTag, response: T)
    fun onError(reqTag: ReqTag, errorBean: ErrorBean)
}