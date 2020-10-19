package com.example.network.converter.typeadapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException

class CharacterTypeAdapter : TypeAdapter<Char>() {
    /**
     * 处理 request
     */
    @Throws(IOException::class)
    override fun write(
        out: JsonWriter,
        value: Char?
    ) {
        if (value == null) {
            out.value("")
            return
        }
        out.value(value.toLong())
    }

    /**
     * 处理 response
     * 调用return语句之前，一定要确保nextXXX()函数只执行一次，并执行成功，否则会出现异常
     */
    @Throws(IOException::class)
    override fun read(`in`: JsonReader): Char {
        return try {
            when {
                `in`.peek() == JsonToken.STRING -> {
                    `in`.nextString()[0]
                }
                `in`.peek() == JsonToken.NUMBER -> {
                    //当服务端返回值为浮点型，但是定义类型为整型时，使用nextInt()会报错，故用nextDouble()接收，并强转
                    `in`.nextDouble().toChar()
                }
                else -> {
                    `in`.skipValue()
                    0.toChar()
                }
            }
        } catch (e: Exception) {
            0.toChar()
        }
    }
}