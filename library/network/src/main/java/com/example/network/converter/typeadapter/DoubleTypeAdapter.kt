package com.example.network.converter.typeadapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException

class DoubleTypeAdapter : TypeAdapter<Double>() {
    /**
     * 处理 request
     */
    @Throws(IOException::class)
    override fun write(
        out: JsonWriter,
        value: Double?
    ) {
        if (value == null) {
            out.value(0.0)
            return
        }
        out.value(value)
    }

    /**
     * 处理 response
     * 调用return语句之前，一定要确保nextXXX()函数只执行一次，并执行成功，否则会出现异常
     */
    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Double {
        return try {
            when {
                `in`.peek() == JsonToken.STRING -> {
                    java.lang.Double.valueOf(`in`.nextString())
                }
                `in`.peek() == JsonToken.NUMBER -> {
                    `in`.nextDouble()
                }
                else -> {
                    `in`.skipValue()
                    0.0
                }
            }
        } catch (e: Exception) {
            0.0
        }
    }
}