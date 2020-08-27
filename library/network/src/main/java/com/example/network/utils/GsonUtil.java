package com.example.network.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * gson解析工具类
 *
 * @author yuanwai
 */
public class GsonUtil {

    private static Gson gson;

    public static Gson getGson(){
        if (gson == null) {
            synchronized (Gson.class) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }

    /**
     * 将json转换为bean
     */
    public static <T> T getBean(String json, Class<T> cls) {
        if (TextUtils.isEmpty(json)) return null;
        try {
            return getGson().fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将json转换为bean或list
     */
    public static Object getObjectOrList(String json, Class<?> clazz) {
        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(json);
            if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                List<Object> list = new ArrayList<Object>();
                for (JsonElement element : jsonArray) {
                    if (element.isJsonArray()) {
                        list.add(getObjectOrList(element.toString(), clazz));
                    } else {
                        list.add(getGson().fromJson(element, clazz));
                    }
                }
                return list;
            } else {
                return getGson().fromJson(json, clazz);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将json转换为string
     */
    public static String GsonToString(Object object) {
        if (object == null) {
            return "";
        }
        try {
            return getGson().toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将json转换为Map<String, Object>
     */
    public static Map<String, Object> getMap(String jsonString) {
        try {
            return getGson().fromJson(jsonString,
                    new TypeToken<Map<String, Object>>() {
                    }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
