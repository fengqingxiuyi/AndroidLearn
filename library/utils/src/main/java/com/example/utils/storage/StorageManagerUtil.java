package com.example.utils.storage;

import android.content.Context;
import android.text.TextUtils;
import android.util.LruCache;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用存储管理，不建议使用SharedPreferencesUtil存储数据
 */
public class StorageManagerUtil {

    private static final String KEY_DEFAULT = "KEY_DEFAULT";

    private static Map<String, LruCache<String, Object>> lruCacheMap;

    private static int sMaxSize = 20;

    public static void init(Context context, int maxSize) {
        SharedPreferencesUtil.init(context);
        lruCacheMap = new HashMap<>();
        sMaxSize = maxSize;
    }

    public static void init(Context context) {
        SharedPreferencesUtil.init(context);
        lruCacheMap = new HashMap<>();
    }

    /**
     * 清除缓存
     */
    public static void clearCache() {
        for (LruCache<String, Object> objectLruCache : lruCacheMap.values()) {
            objectLruCache.evictAll();
        }
    }

    /**
     * 清除指定模块的缓存
     */
    public static void clearCache(String module) {
        if (lruCacheMap.containsKey(module) && null != lruCacheMap.get(module)) {
            lruCacheMap.get(module).evictAll();
        }
    }

    /**
     * 清除指定模块下指定key的缓存
     */
    public static void clearCache(String module, String key) {
        if (lruCacheMap.containsKey(module) && null != lruCacheMap.get(module)) {
            lruCacheMap.get(module).remove(key);
        }
    }

    /**
     * 清除指定模块的本地数据
     */
    public static void clearData(String module) {
        SharedPreferencesUtil.getInstance(module).clear();
    }

    /**
     * 清除指定模块下指定key的本地数据
     */
    public static void clearData(String module, String key) {
        SharedPreferencesUtil.getInstance(module).remove(key);
    }

    /**
     * 存储到本地
     */
    public static void putToDisk(String key, String value) {
        putToDisk(KEY_DEFAULT, key, value);
    }

    public static void putToDisk(String module, String key, String value) {
        putToCache(module, key, value);
        SharedPreferencesUtil.getInstance(module).put(key, value);
    }

    public static void putToDisk(String key, int value) {
        putToDisk(KEY_DEFAULT, key, value);
    }

    public static void putToDisk(String module, String key, int value) {
        putToCache(module, key, value);
        SharedPreferencesUtil.getInstance(module).put(key, value);
    }

    public static void putToDisk(String key, long value) {
        putToDisk(KEY_DEFAULT, key, value);
    }

    public static void putToDisk(String module, String key, long value) {
        putToCache(module, key, value);
        SharedPreferencesUtil.getInstance(module).put(key, value);
    }

    public static void putToDisk(String key, boolean value) {
        putToDisk(KEY_DEFAULT, key, value);
    }

    public static void putToDisk(String module, String key, boolean value) {
        putToCache(module, key, value);
        SharedPreferencesUtil.getInstance(module).put(key, value);
    }

    public static void putToDisk(String key, float value) {
        putToDisk(KEY_DEFAULT, key, value);
    }

    public static void putToDisk(String module, String key, float value) {
        putToCache(module, key, value);
        SharedPreferencesUtil.getInstance(module).put(key, value);
    }

    /**
     * 获取数据
     */
    public static <T> T get(String key, T defaultValue) {
        return get(KEY_DEFAULT, key, defaultValue);
    }

    public static <T> T get(String module, String key, T defaultValue) {
        if (TextUtils.isEmpty(module)) {
            return defaultValue;
        }
        if (TextUtils.isEmpty(key)) {
            return defaultValue;
        }
        Object v = getFromCache(module, key);
        if (null == v) {
            return defaultValue;
        }
        return (T) v;
    }

    public static String getString(String key) {
        return get(key, "");
    }

    public static String get(String key, String defaultValue) {
        return get(KEY_DEFAULT, key, defaultValue);
    }

    public static String get(String module, String key, String defaultValue) {
        if (TextUtils.isEmpty(module)) {
            return defaultValue;
        }
        if (TextUtils.isEmpty(key)) {
            return defaultValue;
        }
        Object v = getFromCache(module, key);
        if (null == v) {
            if (null == defaultValue) {
                defaultValue = "";
            }
            String s = SharedPreferencesUtil.getInstance(module).get(key, defaultValue);
            putToCache(module, key, s);
            return s;
        }
        if (v instanceof String) {
            return (String) v;
        }
        return defaultValue;
    }

    public static int getInt(String key) {
        return get(key, 0);
    }

    public static int get(String key, int defaultValue) {
        return get(KEY_DEFAULT, key, defaultValue);
    }

    public static int get(String module, String key, int defaultValue) {
        if (TextUtils.isEmpty(module)) {
            return defaultValue;
        }
        if (TextUtils.isEmpty(key)) {
            return defaultValue;
        }
        Object v = getFromCache(module, key);
        if (null == v) {
            int i = SharedPreferencesUtil.getInstance(module).get(key, defaultValue);
            putToCache(module, key, i);
            return i;
        }
        if (v instanceof Integer) {
            return (int) v;
        }
        return defaultValue;
    }

    public static long getLong(String key) {
        return get(key, 0L);
    }

    public static long get(String key, long defaultValue) {
        return get(KEY_DEFAULT, key, defaultValue);
    }

    public static long get(String module, String key, long defaultValue) {
        if (TextUtils.isEmpty(module)) {
            return defaultValue;
        }
        if (TextUtils.isEmpty(key)) {
            return defaultValue;
        }
        Object v = getFromCache(module, key);
        if (null == v) {
            long l = SharedPreferencesUtil.getInstance(module).get(key, defaultValue);
            putToCache(module, key, l);
            return l;
        }
        if (v instanceof Long) {
            return (long) v;
        }
        return defaultValue;
    }

    public static boolean getBoolean(String key) {
        return get(key, false);
    }

    public static boolean get(String key, boolean defaultValue) {
        return get(KEY_DEFAULT, key, defaultValue);
    }

    public static boolean get(String module, String key, boolean defaultValue) {
        if (TextUtils.isEmpty(module)) {
            return defaultValue;
        }
        if (TextUtils.isEmpty(key)) {
            return defaultValue;
        }
        Object v = getFromCache(module, key);
        if (null == v) {
            boolean b = SharedPreferencesUtil.getInstance(module).get(key, defaultValue);
            putToCache(module, key, b);
            return b;
        }
        if (v instanceof Boolean) {
            return (boolean) v;
        }
        return defaultValue;
    }

    public static float getFloat(String key) {
        return get(key, 0f);
    }

    public static float get(String key, float defaultValue) {
        return get(KEY_DEFAULT, key, defaultValue);
    }

    public static float get(String module, String key, float defaultValue) {
        if (TextUtils.isEmpty(module)) {
            return defaultValue;
        }
        if (TextUtils.isEmpty(key)) {
            return defaultValue;
        }
        Object v = getFromCache(module, key);
        if (null == v) {
            Float f = SharedPreferencesUtil.getInstance(module).get(key, defaultValue);
            putToCache(module, key, f);
            return f;
        }
        if (v instanceof Float) {
            return (float) v;
        }
        return defaultValue;
    }

    /**
     * 存储到缓存
     */
    private static void putToCache(String module, String key, Object value) {
        try {
            if (!lruCacheMap.containsKey(module) && null == lruCacheMap.get(module)) {
                lruCacheMap.put(module, new LruCache<String, Object>(sMaxSize));
            }
            if (null != value && null != lruCacheMap.get(module)) {
                lruCacheMap.get(module).put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object getFromCache(String module, String key) {
        if (null == lruCacheMap || !lruCacheMap.containsKey(module) || null == lruCacheMap.get(module)) {
            return null;
        }
        return lruCacheMap.get(module).get(key);
    }
}
