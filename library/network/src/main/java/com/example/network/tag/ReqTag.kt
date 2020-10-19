package com.example.network.tag

/**
 * @author fqxyi
 * @date 2018/2/27
 * 请求标识 & 携带数据 & 缓存key
 */
class ReqTag @JvmOverloads constructor(
    var tag: Int = 0, //请求标识，在[IResponseCallback]回调中返回，并且会作为observer缓存的关键
    var `object`: Any? = null //经过整个请求流程的携带数据
) {

    fun setTag(tag: Int): ReqTag {
        this.tag = tag
        return this
    }

    fun setObject(`object`: Any?): ReqTag {
        this.`object` = `object`
        return this
    }

    /**
     * 如果cacheKey不为空则优先执行缓存逻辑
     */
    var cacheKey: String? = null

    /**
     * 允许外部使用默认的Observer
     */
    var isUseDefaultObserver = false

}