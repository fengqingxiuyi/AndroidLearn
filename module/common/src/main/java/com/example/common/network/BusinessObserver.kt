package com.example.common.network

import com.example.common.network.base.NetBaseBean
import com.example.network.bean.ErrorBean
import com.example.network.callback.IResponseCallback
import com.example.network.exception.ExceptionManager
import com.example.network.observer.DefaultObserver
import com.example.network.tag.ReqTag
import com.example.network.utils.GsonUtil.GsonToString
import com.example.log.LogUtil.e
import okhttp3.Request
import retrofit2.HttpException

/**
 * @author fqxyi
 * @date 2018/2/27
 * 业务基础观察者
 */
class BusinessObserver<T>(reqTag: ReqTag, responseCallback: IResponseCallback<T>) :
    DefaultObserver<T>(reqTag, responseCallback) {

    /**
     * 数据可能来自cache
     * @param t
     */
    override fun onNext(t: T) {
        if (t == null) {
            responseCallback.onError(
                reqTag,
                ErrorBean(ExceptionManager.ERROR_SERVER, "response为空")
            )
        } else if (t is NetBaseBean) {
            val iNetBaseBean = t as NetBaseBean
            if (iNetBaseBean.isSuccess) {
                responseCallback.onSuccess(reqTag, t)
            } else if (iNetBaseBean.isToLogin) {
                if (!iNetBaseBean.cache) {
                    //重新登录逻辑
                }
            } else {
                if (!iNetBaseBean.cache) {
                    //上传接口请求错误
                }
                responseCallback.onError(
                    reqTag, ErrorBean(
                        ExceptionManager.ERROR_SERVER,
                        iNetBaseBean.returnCode, iNetBaseBean.returnMsg
                    )
                )
            }
        } else {
            responseCallback.onError(
                reqTag,
                ErrorBean(
                    ExceptionManager.ERROR_SERVER,
                    "response未继承NetBaseBean"
                )
            )
        }
    }

    override fun onError(e: Throwable) {
        if (e is HttpException) {
            val code = e.code()
            if (code in 300..399) { //忽略重定向响应码
                return
            }
            var request: Request? = null
            try {
                val response = e.response()
                if (response != null) {
                    request = response.raw().request()
                }
            } catch (thr: Throwable) {
                e(e)
            }
            if (request != null) {
                val netErrorUrl = request.url().toString()
                val params = GsonToString(request.body())
                //上传错误的网络请求地址和请求参数
            }
            //上传网络请求失败的错误
        }
        super.onError(e)
    }
}