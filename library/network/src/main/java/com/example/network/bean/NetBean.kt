package com.example.network.bean

/**
 * @author fqxyi
 * @date 2018/2/27
 * 回调给调用者的正确数据
 */
open class NetBean (
    // true 数据来自缓存 false 数据来自请求
    var cache: Boolean = false
)