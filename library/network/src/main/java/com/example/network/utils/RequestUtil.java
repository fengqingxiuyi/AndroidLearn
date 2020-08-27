package com.example.network.utils;

import androidx.annotation.NonNull;

import com.example.network.callback.IResponseCallback;

/**
 * 请求工具类
 */
public class RequestUtil {

    public static String getResponseCallbackName(@NonNull IResponseCallback responseCallback) {
        String name = "unknown";
        try {
            name = responseCallback.toString();
            name = name.substring(name.lastIndexOf(".")+1);
            int aIndex = name.indexOf("@");
            if (aIndex != -1) {
                name = name.substring(0, name.indexOf("@"));
            }
            int sIndex = name.lastIndexOf("$");
            if (sIndex != -1) {
                name = name.substring(0, sIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public static String getClassName(@NonNull Object object) {
        String name = "unknown";
        try {
            name = object.getClass().getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public static String getClassSimpleName(@NonNull Object object) {
        String name = null;
        try {
            name = object.getClass().getSimpleName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

}
