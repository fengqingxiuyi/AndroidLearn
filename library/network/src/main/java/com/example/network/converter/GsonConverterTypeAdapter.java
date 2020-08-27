package com.example.network.converter;

import com.example.network.converter.typeadapter.BigDecimalTypeAdapter;
import com.example.network.converter.typeadapter.BigIntegerTypeAdapter;
import com.example.network.converter.typeadapter.BooleanTypeAdapter;
import com.example.network.converter.typeadapter.ByteTypeAdapter;
import com.example.network.converter.typeadapter.CharacterTypeAdapter;
import com.example.network.converter.typeadapter.DoubleTypeAdapter;
import com.example.network.converter.typeadapter.FloatTypeAdapter;
import com.example.network.converter.typeadapter.IntegerTypeAdapter;
import com.example.network.converter.typeadapter.LongTypeAdapter;
import com.example.network.converter.typeadapter.ShortTypeAdapter;
import com.example.network.converter.typeadapter.StringTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class GsonConverterTypeAdapter {

    public static Gson registerTypeAdapter() {
        BooleanTypeAdapter booleanTypeAdapter = new BooleanTypeAdapter();
        ByteTypeAdapter byteTypeAdapter = new ByteTypeAdapter();
        CharacterTypeAdapter characterTypeAdapter = new CharacterTypeAdapter();
        DoubleTypeAdapter doubleTypeAdapter = new DoubleTypeAdapter();
        FloatTypeAdapter floatTypeAdapter = new FloatTypeAdapter();
        IntegerTypeAdapter integerTypeAdapter = new IntegerTypeAdapter();
        LongTypeAdapter longTypeAdapter = new LongTypeAdapter();
        ShortTypeAdapter shortTypeAdapter = new ShortTypeAdapter();
        return new GsonBuilder()
                .registerTypeAdapter(BigDecimal.class, new BigDecimalTypeAdapter())
                .registerTypeAdapter(BigInteger.class, new BigIntegerTypeAdapter())
                .registerTypeAdapter(boolean.class, booleanTypeAdapter)
                .registerTypeAdapter(Boolean.class, booleanTypeAdapter)
                .registerTypeAdapter(byte.class, byteTypeAdapter)
                .registerTypeAdapter(Byte.class, byteTypeAdapter)
                .registerTypeAdapter(char.class, characterTypeAdapter)
                .registerTypeAdapter(Character.class, characterTypeAdapter)
                .registerTypeAdapter(double.class, doubleTypeAdapter)
                .registerTypeAdapter(Double.class, doubleTypeAdapter)
                .registerTypeAdapter(float.class, floatTypeAdapter)
                .registerTypeAdapter(Float.class, floatTypeAdapter)
                .registerTypeAdapter(int.class, integerTypeAdapter)
                .registerTypeAdapter(Integer.class, integerTypeAdapter)
                .registerTypeAdapter(long.class, longTypeAdapter)
                .registerTypeAdapter(Long.class, longTypeAdapter)
                .registerTypeAdapter(short.class, shortTypeAdapter)
                .registerTypeAdapter(Short.class, shortTypeAdapter)
                .registerTypeAdapter(String.class, new StringTypeAdapter())
                .create();
    }

}
