package com.example.network.converter.typeadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ShortTypeAdapter extends TypeAdapter<Short> {
    /**
     * 处理 request
     */
    @Override
    public void write(JsonWriter out, Short value) throws IOException {
        if (value == null) {
            out.value(0);
            return;
        }
        out.value(value);
    }
    /**
     * 处理 response
     * 调用return语句之前，一定要确保nextXXX()函数只执行一次，并执行成功，否则会出现异常
     */
    @Override
    public Short read(JsonReader in) throws IOException {
        try {
            if (in.peek() == JsonToken.STRING) {
                return Short.valueOf(in.nextString());
            } else if (in.peek() == JsonToken.NUMBER) {
                //当服务端返回值为浮点型，但是定义类型为整型时，使用nextInt()会报错，故用nextDouble()接收，并强转
                double value = in.nextDouble();
                if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
                    return (short) value;
                } else {
                    return 0;
                }
            } else {
                in.skipValue();
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }
}
