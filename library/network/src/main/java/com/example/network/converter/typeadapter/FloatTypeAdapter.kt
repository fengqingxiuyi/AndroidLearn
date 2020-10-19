package com.example.network.converter.typeadapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException

class FloatTypeAdapter : TypeAdapter<Float>() {
    /**
     * 处理 request
     */
    @Throws(IOException::class)
    override fun write(
        out: JsonWriter,
        value: Float?
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
    override fun read(`in`: JsonReader): Float {
        return try {
            if (`in`.peek() == JsonToken.STRING) {
                java.lang.Float.valueOf(`in`.nextString())
            } else if (`in`.peek() == JsonToken.NUMBER) {
                val value = `in`.nextDouble()
                if (value >= Float.MIN_VALUE && value <= Float.MAX_VALUE) {
                    value.toFloat()
                } else {
                    0f
                }
            } else {
                `in`.skipValue()
                0f
            }
        } catch (e: Exception) {
            0f
        }
    }
}