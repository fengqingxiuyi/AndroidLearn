package com.example.network.converter.typeadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class StringTypeAdapter extends TypeAdapter<String> {
    /**
     * 处理 request
     */
    @Override
    public void write(JsonWriter out, String value) throws IOException {
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
    public String read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return "";
        } else {
            String value = in.nextString();
            if ("null".equals(value)) {
                return "";
            }
            return value;
        }
    }
}