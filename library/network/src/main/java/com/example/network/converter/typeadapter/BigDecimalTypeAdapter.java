package com.example.network.converter.typeadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalTypeAdapter extends TypeAdapter<BigDecimal> {
    /**
     * 处理 request
     */
    @Override
    public void write(JsonWriter out, BigDecimal value) throws IOException {
        if (value == null) {
            out.value(BigDecimal.ZERO);
            return;
        }
        out.value(value);
    }
    /**
     * 处理 response
     * 调用return语句之前，一定要确保nextXXX()函数只执行一次，并执行成功，否则会出现异常
     */
    @Override
    public BigDecimal read(JsonReader in) throws IOException {
        try {
            if (in.peek() == JsonToken.STRING) {
                return new BigDecimal(in.nextString());
            } else if (in.peek() == JsonToken.NUMBER) {
                return BigDecimal.valueOf(in.nextDouble());
            } else {
                in.skipValue();
                return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
