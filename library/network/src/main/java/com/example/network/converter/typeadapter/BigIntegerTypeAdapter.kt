package com.example.network.converter.typeadapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.math.BigInteger

class BigIntegerTypeAdapter : TypeAdapter<BigInteger>() {
    /**
     * 处理 request
     */
    @Throws(IOException::class)
    override fun write(
        out: JsonWriter,
        value: BigInteger?
    ) {
        if (value == null) {
            out.value(BigInteger.ZERO)
            return
        }
        out.value(value)
    }

    /**
     * 处理 response
     * 调用return语句之前，一定要确保nextXXX()函数只执行一次，并执行成功，否则会出现异常
     */
    @Throws(IOException::class)
    override fun read(`in`: JsonReader): BigInteger {
        return try {
            when {
                `in`.peek() == JsonToken.STRING -> {
                    BigInteger(`in`.nextString())
                }
                `in`.peek() == JsonToken.NUMBER -> {
                    //当服务端返回值为浮点型，但是定义类型为整型时，使用nextLong()会报错，故用nextDouble()接收，并强转
                    BigInteger.valueOf(`in`.nextDouble().toLong())
                }
                else -> {
                    `in`.skipValue()
                    BigInteger.ZERO
                }
            }
        } catch (e: Exception) {
            BigInteger.ZERO
        }
    }
}