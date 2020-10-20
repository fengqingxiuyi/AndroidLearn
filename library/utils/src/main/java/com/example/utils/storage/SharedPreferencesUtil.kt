package com.example.utils.storage

import android.app.Activity
import android.content.Context
import java.util.*

/**
 * SharedPreferences操作类，不建议使用SharedPreferencesUtil存储数据，建议使用StorageManagerUtil管理数据和缓存
 *
 * 注意点：传入的上下文不能是activity，应该是context，否则会导致内存泄漏
 *
 * 使用apply虽然可以很快的返回值，但是在Activity执行onPause方法的时候会执行写入操作，导致页面卡顿
 */
class SharedPreferencesUtil private constructor(
    appContext: Context,
    filename: String
) {

    companion object {
        private lateinit var sContext: Context
        private var sharedPreferencesUtilMap = HashMap<String, SharedPreferencesUtil>()

        @JvmStatic
        fun init(context: Context) {
            sContext = context
        }

        @JvmStatic
        @Synchronized
        fun getInstance(filename: String): SharedPreferencesUtil {
            if (sharedPreferencesUtilMap.containsKey(filename)) {
                return sharedPreferencesUtilMap[filename]!!
            }
            val instance = SharedPreferencesUtil(sContext, filename)
            sharedPreferencesUtilMap[filename] = instance
            return instance
        }
    }

    private val sharedPreferences =
        appContext.getSharedPreferences(filename, Activity.MODE_PRIVATE)

    /**
     * 清除数据
     */
    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    /**
     * 清除指定的key的数据
     */
    fun remove(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    /**
     * 异步操作，一般调用这个即可
     */
    fun put(key: String?, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    operator fun get(key: String?, defaultValue: String?): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    fun getString(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    fun put(key: String?, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    operator fun get(key: String?, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun getBoolean(key: String?): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun put(key: String?, value: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    operator fun get(key: String?, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun getLong(key: String?): Long {
        return sharedPreferences.getLong(key, 0)
    }

    fun put(key: String?, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    operator fun get(key: String?, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun getInt(key: String?): Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun put(key: String?, value: Float) {
        val editor = sharedPreferences.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    operator fun get(key: String?, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    fun getFloat(key: String?): Float {
        return sharedPreferences.getFloat(key, 0f)
    }

}