package com.example.network.bean

/**
 * @author fqxyi
 * @date 2018/2/27
 * 回调给调用者的错误数据
 */
data class ErrorBean (
    /**
     * 网络库封装的错误码
     */
    var code: Int = 0,

    /**
     * 服务端返回的错误码
     */
    var serverCode: String? = null,

    /**
     * 网络库 和 服务端 返回的错误消息
     */
    var msg: String? = null
)