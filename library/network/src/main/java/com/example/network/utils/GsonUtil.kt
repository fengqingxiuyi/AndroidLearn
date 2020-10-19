package com.example.network.utils

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * gson解析工具类
 *
 * @author yuanwai
 */
object GsonUtil {
    var gson: Gson? = null
        get() {
            if (field == null) {
                synchronized(Gson::class.java) {
                    if (field == null) {
                        field = Gson()
                    }
                }
            }
            return field
        }
        private set

    /**
     * 将json转换为bean
     */
    fun <T> getBean(json: String?, cls: Class<T>?): T? {
        return if (TextUtils.isEmpty(json)) null else try {
            gson!!.fromJson(json, cls)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 将json转换为bean或list
     */
    fun getObjectOrList(json: String?, clazz: Class<*>?): Any? {
        return try {
            val jsonParser = JsonParser()
            val jsonElement = jsonParser.parse(json)
            if (jsonElement.isJsonArray) {
                val jsonArray = jsonElement.asJsonArray
                val list: MutableList<Any?> =
                    ArrayList()
                for (element in jsonArray) {
                    if (element.isJsonArray) {
                        list.add(getObjectOrList(element.toString(), clazz))
                    } else {
                        list.add(gson!!.fromJson(element, clazz))
                    }
                }
                list
            } else {
                gson!!.fromJson(json, clazz)
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 将json转换为string
     */
    @JvmStatic
    fun GsonToString(`object`: Any?): String {
        return if (`object` == null) {
            ""
        } else try {
            gson!!.toJson(`object`)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 将json转换为Map<String></String>, Object>
     */
    fun getMap(jsonString: String?): Map<String, Any>? {
        return try {
            gson!!
                .fromJson<Map<String, Any>>(
                    jsonString,
                    object :
                        TypeToken<Map<String?, Any?>?>() {}.type
                )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}