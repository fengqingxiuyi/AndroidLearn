package com.example.network.converter.typeadapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.math.BigDecimal

class BigDecimalTypeAdapter : TypeAdapter<BigDecimal>() {
    /**
     * 处理 request
     */
    @Throws(IOException::class)
    override fun write(
        out: JsonWriter,
        value: BigDecimal?
    ) {
        if (value == null) {
            out.value(BigDecimal.ZERO)
            return
        }
        out.value(value)
    }

    /**
     * 处理 response
     * 调用return语句之前，一定要确保nextXXX()函数只执行一次，并执行成功，否则会出现异常
     */
    @Throws(IOException::class)
    override fun read(`in`: JsonReader): BigDecimal {
        return try {
            when {
                `in`.peek() == JsonToken.STRING -> {
                    BigDecimal(`in`.nextString())
                }
                `in`.peek() == JsonToken.NUMBER -> {
                    BigDecimal.valueOf(`in`.nextDouble())
                }
                else -> {
                    `in`.skipValue()
                    BigDecimal.ZERO
                }
            }
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
    }
}