package com.example.network.converter.typeadapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException

class BooleanTypeAdapter : TypeAdapter<Boolean>() {
    /**
     * 处理 request
     */
    @Throws(IOException::class)
    override fun write(
        out: JsonWriter,
        value: Boolean?
    ) {
        if (value == null) {
            out.value(false)
            return
        }
        out.value(value)
    }

    /**
     * 处理 response
     * 调用return语句之前，一定要确保nextXXX()函数只执行一次，并执行成功，否则会出现异常
     */
    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Boolean {
        return try {
            when {
                `in`.peek() == JsonToken.STRING -> {
                    `in`.nextString().toDouble() > 0
                }
                `in`.peek() == JsonToken.NUMBER -> {
                    `in`.nextDouble() > 0
                }
                `in`.peek() == JsonToken.BOOLEAN -> {
                    `in`.nextBoolean()
                }
                else -> {
                    `in`.skipValue()
                    false
                }
            }
        } catch (e: Exception) {
            false
        }
    }
}