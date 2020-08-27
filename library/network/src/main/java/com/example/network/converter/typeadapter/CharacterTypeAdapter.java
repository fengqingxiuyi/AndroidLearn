package com.example.network.converter.typeadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class CharacterTypeAdapter extends TypeAdapter<Character> {
    /**
     * 处理 request
     */
    @Override
    public void write(JsonWriter out, Character value) throws IOException {
        if (value == null) {
            out.value("");
            return;
        }
        out.value(value);
    }
    /**
     * 处理 response
     * 调用return语句之前，一定要确保nextXXX()函数只执行一次，并执行成功，否则会出现异常
     */
    @Override
    public Character read(JsonReader in) throws IOException {
        try {
            if (in.peek() == JsonToken.STRING) {
                return in.nextString().charAt(0);
            } else if (in.peek() == JsonToken.NUMBER) {
                //当服务端返回值为浮点型，但是定义类型为整型时，使用nextInt()会报错，故用nextDouble()接收，并强转
                return (char) in.nextDouble();
            } else {
                in.skipValue();
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }
}