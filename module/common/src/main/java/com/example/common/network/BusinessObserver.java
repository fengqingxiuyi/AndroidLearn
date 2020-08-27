package com.example.common.network;

import androidx.annotation.NonNull;

import com.example.common.network.base.NetBaseBean;
import com.example.network.bean.ErrorBean;
import com.example.network.callback.IResponseCallback;
import com.example.network.exception.ExceptionManager;
import com.example.network.observer.DefaultObserver;
import com.example.network.tag.ReqTag;
import com.example.network.utils.GsonUtil;
import com.example.utils.LogUtil;

import okhttp3.Request;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * @author fqxyi
 * @date 2018/2/27
 * 业务基础观察者
 */
public class BusinessObserver<T> extends DefaultObserver<T> {

    public BusinessObserver(@NonNull ReqTag reqTag, @NonNull IResponseCallback<T> responseCallback) {
        super(reqTag, responseCallback);
    }

    /**
     * 数据可能来自cache
     * @param t
     */
    @Override
    public void onNext(T t) {
        if (t == null) {
            responseCallback.onError(reqTag, new ErrorBean(ExceptionManager.ERROR_SERVER, "response为空"));
        } else if (t instanceof NetBaseBean) {
            NetBaseBean iNetBaseBean = (NetBaseBean) t;
            if (iNetBaseBean.isSuccess()) {
                responseCallback.onSuccess(reqTag, t);
            } else if (iNetBaseBean.isToLogin()) {
                if (!iNetBaseBean.cache) {
                    //重新登录逻辑
                }
            } else {
                if (!iNetBaseBean.cache) {
                    //上传接口请求错误
                }
                responseCallback.onError(reqTag, new ErrorBean(ExceptionManager.ERROR_SERVER,
                        iNetBaseBean.getReturnCode(), iNetBaseBean.getReturnMsg()));
            }
        } else {
            responseCallback.onError(reqTag, new ErrorBean(ExceptionManager.ERROR_SERVER, "response未继承NetBaseBean"));
        }
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            int code = httpException.code();
            if (code >= 300 && code <= 399) { //忽略重定向响应码
                return;
            }
            Request request = null;
            try {
                Response response = httpException.response();
                if (response != null) {
                    request = response.raw().request();
                }
            } catch (Throwable thr) {
                LogUtil.e(e);
            }
            if (request != null) {
                String netErrorUrl = request.url().toString();
                String params = GsonUtil.GsonToString(request.body());
                //上传错误的网络请求地址和请求参数
            }
            //上传网络请求失败的错误
        }
        super.onError(e);
    }

}
