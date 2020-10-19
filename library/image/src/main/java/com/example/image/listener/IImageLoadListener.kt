package com.example.image.listener

/**
 * @author fqxyi
 * @date 2018/2/22
 * 用于检测图片加载成功或失败的监听器
 */
interface IImageLoadListener {
    fun onSuccess(id: String?, width: Int, height: Int)
    fun onFailure(id: String?, throwable: Throwable?)
}