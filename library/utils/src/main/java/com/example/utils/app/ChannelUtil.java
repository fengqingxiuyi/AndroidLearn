package com.example.utils.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.utils.BuildConfig;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 渠道名（应用市场名）操作类，多用于企业应用多渠道打包后的包中
 */
public class ChannelUtil {

    //存储文件名
    private static final String SP_APP = "app";
    //存储Key
    private static final String CHANNEL_KEY = "channel";
    private static final String CHANNEL_VERSION_KEY = "channel_version";
    // 存储渠道名于内存中
    private static String channelName;

    /**
     * 返回渠道名（应用市场名），如果获取失败返回defaultChannel
     */
    public static String getChannel(Context context) {
        // 内存中获取
        if (!TextUtils.isEmpty(channelName)) {
            return channelName;
        }
        // sp中获取
        channelName = getChannelBySharedPreferences(context);
        if (!TextUtils.isEmpty(channelName)) {
            return channelName;
        }
        // 从apk中获取
        channelName = getChannelFromApk(context, CHANNEL_KEY);
        if (!TextUtils.isEmpty(channelName)) {
            //保存sp中备用
            setChannelBySharedPreferences(context, channelName);
            return channelName;
        }
        // 全部获取失败
        if (BuildConfig.DEBUG) {
            return "test";
        } else {
            return "tongyong";
        }
    }

    /**
     * 从apk中获取版本信息
     */
    private static String getChannelFromApk(Context context, String channelKey) {
        // 从apk包中获取
        ApplicationInfo appInfo = context.getApplicationInfo();
        String sourceDir = appInfo.sourceDir;
        // 默认放在META-INF/里， 所以需要再拼接一下
        String key = "META-INF/" + channelKey;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith(key)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] split = ret.split("_");
        String channel = "";
        if (split.length >= 2) {
            channel = ret.substring(split[0].length() + 1);
        }
        return channel;
    }

    /**
     * 本地保存channel & 对应版本号
     */
    private static void setChannelBySharedPreferences(Context context, String channel) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CHANNEL_KEY, channel);
        editor.putInt(CHANNEL_VERSION_KEY, AppUtil.getAppVersionCode(context));
        editor.apply();
    }

    /**
     * 从sp中获取channel
     *
     * @return 为空表示获取异常、sp中的值已经失效、sp中没有此值
     */
    private static String getChannelBySharedPreferences(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        int currentVersionCode = AppUtil.getAppVersionCode(context);
        if (currentVersionCode == -1) {
            // 获取错误
            return "";
        }
        int savedVersionCode = sharedPreferences.getInt(CHANNEL_VERSION_KEY, -1);
        if (savedVersionCode == -1) {
            // 本地没有存储的channel对应的版本号
            // 第一次使用 或者 原先存储版本号异常
            return "";
        }
        if (currentVersionCode != savedVersionCode) {
            return "";
        }
        return PreferenceManager.getDefaultSharedPreferences(context).getString(CHANNEL_KEY, "");
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SP_APP, Activity.MODE_PRIVATE);
    }

}
