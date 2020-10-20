package com.example.utils.storage

import android.content.Context
import android.util.LruCache
import com.example.utils.storage.SharedPreferencesUtil.Companion.getInstance
import java.util.*

/**
 * 应用存储管理，不建议使用SharedPreferencesUtil存储数据
 */
object StorageManagerUtil {

    private const val KEY_DEFAULT = "KEY_DEFAULT"
    private var lruCacheMap = HashMap<String, LruCache<String, Any?>>()
    private var sMaxSize = 20

    fun init(context: Context, maxSize: Int) {
        SharedPreferencesUtil.init(context)
        sMaxSize = maxSize
    }

    fun init(context: Context) {
        SharedPreferencesUtil.init(context)
    }

    /**
     * 清除缓存
     */
    fun clearCache() {
        for (objectLruCache in lruCacheMap.values) {
            objectLruCache.evictAll()
        }
    }

    /**
     * 清除指定模块的缓存
     */
    fun clearCache(module: String?) {
        lruCacheMap[module]?.evictAll()
    }

    /**
     * 清除指定模块下指定key的缓存
     */
    fun clearCache(module: String?, key: String?) {
        lruCacheMap[module]?.remove(key)
    }

    /**
     * 清除指定模块的本地数据
     */
    fun clearData(module: String) {
        getInstance(module).clear()
    }

    /**
     * 清除指定模块下指定key的本地数据
     */
    fun clearData(module: String, key: String) {
        getInstance(module).remove(key)
    }

    /**
     * 存储到本地
     */
    fun putToDisk(key: String, value: String?) {
        putToDisk(KEY_DEFAULT, key, value)
    }

    @JvmStatic
    fun putToDisk(module: String, key: String, value: String?) {
        putToCache(module, key, value)
        getInstance(module).put(key, value)
    }

    fun putToDisk(key: String, value: Int) {
        putToDisk(KEY_DEFAULT, key, value)
    }

    @JvmStatic
    fun putToDisk(module: String, key: String, value: Int) {
        putToCache(module, key, value)
        getInstance(module).put(key, value)
    }

    fun putToDisk(key: String, value: Long) {
        putToDisk(KEY_DEFAULT, key, value)
    }

    @JvmStatic
    fun putToDisk(module: String, key: String, value: Long) {
        putToCache(module, key, value)
        getInstance(module).put(key, value)
    }

    fun putToDisk(key: String, value: Boolean) {
        putToDisk(KEY_DEFAULT, key, value)
    }

    @JvmStatic
    fun putToDisk(module: String, key: String, value: Boolean) {
        putToCache(module, key, value)
        getInstance(module).put(key, value)
    }

    fun putToDisk(key: String, value: Float) {
        putToDisk(KEY_DEFAULT, key, value)
    }

    @JvmStatic
    fun putToDisk(module: String, key: String, value: Float) {
        putToCache(module, key, value)
        getInstance(module).put(key, value)
    }

    /**
     * 获取数据
     */
    @JvmStatic
    operator fun <T> get(key: String, defaultValue: T): T {
        return get(KEY_DEFAULT, key, defaultValue)
    }

    @JvmStatic
    operator fun <T> get(module: String, key: String, defaultValue: T): T {
        val v = getFromCache(module, key) ?: return defaultValue
        return v as T
    }

    fun getString(key: String): String {
        return get(key, "")
    }

    @JvmStatic
    operator fun get(key: String, defaultValue: String?): String {
        return get(KEY_DEFAULT, key, defaultValue)
    }

    @JvmStatic
    operator fun get(
        module: String,
        key: String,
        defaultValue: String?
    ): String {
        val v = getFromCache(module, key)
        if (null == v) {
            val s = getInstance(module)[key, defaultValue] ?: ""
            putToCache(module, key, s)
            return s
        }
        return if (v is String) {
            v
        } else defaultValue ?: ""
    }

    fun getInt(key: String): Int {
        return get(key, 0)
    }

    @JvmStatic
    operator fun get(key: String, defaultValue: Int): Int {
        return get(KEY_DEFAULT, key, defaultValue)
    }

    @JvmStatic
    operator fun get(module: String, key: String, defaultValue: Int): Int {
        val v = getFromCache(module, key)
        if (null == v) {
            val i = getInstance(module)[key, defaultValue]
            putToCache(module, key, i)
            return i
        }
        return if (v is Int) {
            v
        } else defaultValue
    }

    fun getLong(key: String): Long {
        return get(key, 0L)
    }

    @JvmStatic
    operator fun get(key: String, defaultValue: Long): Long {
        return get(KEY_DEFAULT, key, defaultValue)
    }

    @JvmStatic
    operator fun get(module: String, key: String, defaultValue: Long): Long {
        val v = getFromCache(module, key)
        if (null == v) {
            val l = getInstance(module)[key, defaultValue]
            putToCache(module, key, l)
            return l
        }
        return if (v is Long) {
            v
        } else defaultValue
    }

    fun getBoolean(key: String): Boolean {
        return get(key, false)
    }

    @JvmStatic
    operator fun get(key: String, defaultValue: Boolean): Boolean {
        return get(KEY_DEFAULT, key, defaultValue)
    }

    @JvmStatic
    operator fun get(
        module: String,
        key: String,
        defaultValue: Boolean
    ): Boolean {
        val v = getFromCache(module, key)
        if (null == v) {
            val b = getInstance(module)[key, defaultValue]
            putToCache(module, key, b)
            return b
        }
        return if (v is Boolean) {
            v
        } else defaultValue
    }

    fun getFloat(key: String): Float {
        return get(key, 0f)
    }

    @JvmStatic
    operator fun get(key: String, defaultValue: Float): Float {
        return get(KEY_DEFAULT, key, defaultValue)
    }

    @JvmStatic
    operator fun get(module: String, key: String, defaultValue: Float): Float {
        val v = getFromCache(module, key)
        if (null == v) {
            val f = getInstance(module)[key, defaultValue]
            putToCache(module, key, f)
            return f
        }
        return if (v is Float) {
            v
        } else defaultValue
    }

    /**
     * 存储到缓存
     */
    private fun putToCache(
        module: String,
        key: String,
        value: Any?
    ) {
        try {
            if (null == lruCacheMap[module]) {
                lruCacheMap[module] = LruCache(sMaxSize)
            }
            lruCacheMap[module]!!.put(key, value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getFromCache(module: String, key: String): Any? {
        return lruCacheMap[module]?.get(key)
    }
}