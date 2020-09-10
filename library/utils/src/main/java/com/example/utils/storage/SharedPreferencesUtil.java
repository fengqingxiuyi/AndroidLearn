package com.example.utils.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * SharedPreferences操作类，不建议使用SharedPreferencesUtil存储数据，建议使用StorageManagerUtil管理数据和缓存
 *
 * 注意点：传入的上下文不能是activity，应该是context，否则会导致内存泄漏
 *
 * 使用apply虽然可以很快的返回值，但是在Activity执行onPause方法的时候会执行写入操作，导致页面卡顿
 */
public class SharedPreferencesUtil {

    private SharedPreferences sharedPreferences;

    private static Context sContext;

    private static Map<String, SharedPreferencesUtil> sharedPreferencesUtilMap;

    public static void init(Context context) {
        sContext = context;
        sharedPreferencesUtilMap = new HashMap<>();
    }

    public static synchronized SharedPreferencesUtil getInstance(String filename) {
        if (sharedPreferencesUtilMap.containsKey(filename)) {
            return sharedPreferencesUtilMap.get(filename);
        }
        SharedPreferencesUtil instance = new SharedPreferencesUtil(sContext, filename);
        sharedPreferencesUtilMap.put(filename, instance);
        return instance;
    }

    private SharedPreferencesUtil(Context appContext, String filename) {
        sharedPreferences = appContext.getSharedPreferences(filename, Activity.MODE_PRIVATE);
    }

    /**
     * 清除数据
     */
    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 清除指定的key的数据
     */
    public void remove(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 异步操作，一般调用这个即可
     */
    public void put(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String get(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void put(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean get(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void put(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long get(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public void put(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int get(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void put(String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public float get(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0);
    }

}
