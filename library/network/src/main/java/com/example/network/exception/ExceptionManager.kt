package com.example.network.exception

import android.net.ParseException
import com.example.network.bean.ErrorBean
import com.example.utils.BuildConfig
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * @author fqxyi
 * @date 2018/2/27
 * 异常管理类
 */
object ExceptionManager {
    //服务端导致的错误
    const val ERROR_SERVER = -1

    //未知错误
    private const val ERROR_UNKNOWN = 1000

    //解析错误
    private const val ERROR_PARSE = 1001

    //网络连接失败
    private const val ERROR_HTTP = 1002

    //未知主机错误
    private const val ERROR_UNKNOWN_HOST = 1003

    //网络连接超时
    private const val ERROR_TIMEOUT = 1004

    //证书验证失败
    private const val ERROR_SSL = 1005

    //IO异常
    private const val ERROR_IO = 1006

    fun getErrorBean(e: Throwable?): ErrorBean {
        val errorBean = ErrorBean()
        if (e == null) {
            errorBean.code = ERROR_UNKNOWN
            errorBean.msg = "未知错误"
            return errorBean
        }
        try {
            if (e is JsonSyntaxException) {
                errorBean.code = ERROR_PARSE
                errorBean.msg = "解析格式错误"
            }
            if (e is JsonIOException) {
                errorBean.code = ERROR_PARSE
                errorBean.msg = "解析IO异常"
            }
        } catch (error: NoClassDefFoundError) {
            //do nothing
        }
        if (e is JSONException
            || e is ParseException
        ) {
            errorBean.code = ERROR_PARSE
            errorBean.msg = "解析错误"
        } else if (e is HttpException) {

            /**
             * HTTP 响应状态代码指示特定 HTTP 请求是否已成功完成。
             * 响应分为五类：信息响应(100–199)，成功响应(200–299)，重定向(300–399)，客户端错误(400–499)和服务器错误 (500–599)。
             * 详细说明文档：https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status
             */
            val code = e.code()
            errorBean.code = code
            errorBean.msg = "网络错误$code"
        } else if (e is SocketException) {
            errorBean.code = ERROR_HTTP
            errorBean.msg = "网络连接失败"
        } else if (e is UnknownHostException) {
            errorBean.code = ERROR_UNKNOWN_HOST
            errorBean.msg = "未知主机错误"
        } else if (e is ConnectTimeoutException
            || e is SocketTimeoutException
        ) {
            errorBean.code = ERROR_TIMEOUT
            errorBean.msg = "网络连接超时"
        } else if (e is SSLException) {
            errorBean.code = ERROR_SSL
            errorBean.msg = "证书验证失败"
        } else if (e is IOException) {
            errorBean.code = ERROR_IO
            errorBean.msg = "IO异常"
        } else {
            errorBean.code = ERROR_UNKNOWN
            errorBean.msg = "未知错误"
        }
        if (BuildConfig.DEBUG) {
            errorBean.msg = errorBean.msg + " => " + e.message
        }
        return errorBean
    }
}