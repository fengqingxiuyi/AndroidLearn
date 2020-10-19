package com.example.network.converter.typeadapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException

class ShortTypeAdapter : TypeAdapter<Short>() {
    /**
     * 处理 request
     */
    @Throws(IOException::class)
    override fun write(
        out: JsonWriter,
        value: Short?
    ) {
        if (value == null) {
            out.value(0)
            return
        }
        out.value(value)
    }

    /**
     * 处理 response
     * 调用return语句之前，一定要确保nextXXX()函数只执行一次，并执行成功，否则会出现异常
     */
    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Short {
        return try {
            if (`in`.peek() == JsonToken.STRING) {
                `in`.nextString().toShort()
            } else if (`in`.peek() == JsonToken.NUMBER) {
                //当服务端返回值为浮点型，但是定义类型为整型时，使用nextInt()会报错，故用nextDouble()接收，并强转
                val value = `in`.nextDouble()
                if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
                    value.toShort()
                } else {
                    0
                }
            } else {
                `in`.skipValue()
                0
            }
        } catch (e: Exception) {
            0
        }
    }
}