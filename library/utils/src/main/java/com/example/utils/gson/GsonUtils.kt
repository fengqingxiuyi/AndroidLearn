package com.example.utils.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

object GsonUtils {
    private val INSTANCE: Gson by lazy { GsonBuilder().serializeNulls().create() }
    private val INSTANCE_DISABLE_HTML_ESCAPING: Gson by lazy {
        GsonBuilder().serializeNulls().disableHtmlEscaping().create()
    }

    fun instance(disableHtmlEscaping: Boolean = false): Gson {
        return if (disableHtmlEscaping) INSTANCE_DISABLE_HTML_ESCAPING else INSTANCE
    }
}

inline fun <reified T> T.toJson(disableHtmlEscaping: Boolean = false): String =
    GsonUtils.instance(disableHtmlEscaping).toJson(this)

inline fun <reified T> String.fromJson(disableHtmlEscaping: Boolean = false): T =
    GsonUtils.instance(disableHtmlEscaping).fromJson(this, T::class.java)

inline fun <reified T> String.fromJsonType(disableHtmlEscaping: Boolean = false): T =
  GsonUtils.instance(disableHtmlEscaping).fromJson(this, object : TypeToken<T>() {}.type)

inline fun <reified T> String.fromJsonList(disableHtmlEscaping: Boolean = false): List<T>? =
    this.takeIf { it.isNotBlank() }
        ?.let {
            GsonUtils.instance(disableHtmlEscaping)
                .fromJson(this, ParameterizedTypeImpl(T::class.java))
        }
