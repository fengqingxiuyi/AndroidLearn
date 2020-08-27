package com.example.network.exception;

import com.example.network.bean.ErrorBean;
import com.example.utils.BuildConfig;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import retrofit2.HttpException;

/**
 * @author fqxyi
 * @date 2018/2/27
 * 异常管理类
 */
public class ExceptionManager {

    //服务端导致的错误
    public static final int ERROR_SERVER = -1;
    //未知错误
    private static final int ERROR_UNKNOWN = 1000;
    //解析错误
    private static final int ERROR_PARSE = 1001;
    //网络连接失败
    private static final int ERROR_HTTP = 1002;
    //未知主机错误
    private static final int ERROR_UNKNOWN_HOST = 1003;
    //网络连接超时
    private static final int ERROR_TIMEOUT = 1004;
    //证书验证失败
    private static final int ERROR_SSL = 1005;
    //IO异常
    private static final int ERROR_IO = 1006;

    public static ErrorBean getErrorBean(Throwable e) {
        ErrorBean errorBean = new ErrorBean();
        if (e == null) {
            errorBean.code = ERROR_UNKNOWN;
            errorBean.msg = "未知错误";
            return errorBean;
        }
        try {
            if (e instanceof com.google.gson.JsonSyntaxException) {
                errorBean.code = ERROR_PARSE;
                errorBean.msg = "解析格式错误";
            }
            if (e instanceof com.google.gson.JsonIOException) {
                errorBean.code = ERROR_PARSE;
                errorBean.msg = "解析IO异常";
            }
        } catch (NoClassDefFoundError error) {
            //do nothing
        }
        if (e instanceof org.json.JSONException
                || e instanceof android.net.ParseException) {
            errorBean.code = ERROR_PARSE;
            errorBean.msg = "解析错误";
        } else if (e instanceof HttpException) {
            HttpException exception = (HttpException) e;
            /**
             * HTTP 响应状态代码指示特定 HTTP 请求是否已成功完成。
             * 响应分为五类：信息响应(100–199)，成功响应(200–299)，重定向(300–399)，客户端错误(400–499)和服务器错误 (500–599)。
             * 详细说明文档：https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status
             */
            int code = exception.code();
            errorBean.code = code;
            errorBean.msg = "网络错误" + code;
        } else if (e instanceof SocketException) {
            errorBean.code = ERROR_HTTP;
            errorBean.msg = "网络连接失败";
        } else if (e instanceof UnknownHostException) {
            errorBean.code = ERROR_UNKNOWN_HOST;
            errorBean.msg = "未知主机错误";
        } else if (e instanceof ConnectTimeoutException
                || e instanceof SocketTimeoutException) {
            errorBean.code = ERROR_TIMEOUT;
            errorBean.msg = "网络连接超时";
        } else if (e instanceof SSLException) {
            errorBean.code = ERROR_SSL;
            errorBean.msg = "证书验证失败";
        } else if (e instanceof IOException) {
            errorBean.code = ERROR_IO;
            errorBean.msg = "IO异常";
        } else {
            errorBean.code = ERROR_UNKNOWN;
            errorBean.msg = "未知错误";
        }
        if (BuildConfig.DEBUG) {
            errorBean.msg = errorBean.msg + " => " + e.getMessage();
        }
        return errorBean;
    }

}
