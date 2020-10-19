package com.example.network.converter

import com.example.network.converter.typeadapter.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.math.BigDecimal
import java.math.BigInteger

object GsonConverterTypeAdapter {
    fun registerTypeAdapter(): Gson {
        val booleanTypeAdapter = BooleanTypeAdapter()
        val byteTypeAdapter = ByteTypeAdapter()
        val characterTypeAdapter = CharacterTypeAdapter()
        val doubleTypeAdapter = DoubleTypeAdapter()
        val floatTypeAdapter = FloatTypeAdapter()
        val integerTypeAdapter = IntegerTypeAdapter()
        val longTypeAdapter = LongTypeAdapter()
        val shortTypeAdapter = ShortTypeAdapter()
        return GsonBuilder()
            .registerTypeAdapter(BigDecimal::class.java, BigDecimalTypeAdapter())
            .registerTypeAdapter(BigInteger::class.java, BigIntegerTypeAdapter())
            .registerTypeAdapter(Boolean::class.javaPrimitiveType, booleanTypeAdapter)
            .registerTypeAdapter(Boolean::class.java, booleanTypeAdapter)
            .registerTypeAdapter(Byte::class.javaPrimitiveType, byteTypeAdapter)
            .registerTypeAdapter(Byte::class.java, byteTypeAdapter)
            .registerTypeAdapter(Char::class.javaPrimitiveType, characterTypeAdapter)
            .registerTypeAdapter(Char::class.java, characterTypeAdapter)
            .registerTypeAdapter(Double::class.javaPrimitiveType, doubleTypeAdapter)
            .registerTypeAdapter(Double::class.java, doubleTypeAdapter)
            .registerTypeAdapter(Float::class.javaPrimitiveType, floatTypeAdapter)
            .registerTypeAdapter(Float::class.java, floatTypeAdapter)
            .registerTypeAdapter(Int::class.javaPrimitiveType, integerTypeAdapter)
            .registerTypeAdapter(Int::class.java, integerTypeAdapter)
            .registerTypeAdapter(Long::class.javaPrimitiveType, longTypeAdapter)
            .registerTypeAdapter(Long::class.java, longTypeAdapter)
            .registerTypeAdapter(Short::class.javaPrimitiveType, shortTypeAdapter)
            .registerTypeAdapter(Short::class.java, shortTypeAdapter)
            .registerTypeAdapter(String::class.java, StringTypeAdapter())
            .create()
    }
}