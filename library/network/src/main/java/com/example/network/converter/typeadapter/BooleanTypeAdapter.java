package com.example.network.converter.typeadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class BooleanTypeAdapter extends TypeAdapter<Boolean> {
    /**
     * 处理 request
     */
    @Override
    public void write(JsonWriter out, Boolean value) throws IOException {
        if (value == null) {
            out.value(false);
            return;
        }
        out.value(value);
    }
    /**
     * 处理 response
     * 调用return语句之前，一定要确保nextXXX()函数只执行一次，并执行成功，否则会出现异常
     */
    @Override
    public Boolean read(JsonReader in) throws IOException {
        try {
            if (in.peek() == JsonToken.STRING) {
                return Double.parseDouble(in.nextString()) > 0;
            } else if (in.peek() == JsonToken.NUMBER) {
                return in.nextDouble() > 0;
            } else if (in.peek() == JsonToken.BOOLEAN) {
                return in.nextBoolean();
            } else {
                in.skipValue();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}

