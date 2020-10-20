package com.example.utils.app

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.example.utils.BuildConfig
import com.example.utils.app.AppUtil.getAppVersionCode
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * 渠道名（应用市场名）操作类，多用于企业应用多渠道打包后的包中
 */
object ChannelUtil {
    //存储文件名
    private const val SP_APP = "app"

    //存储Key
    private const val CHANNEL_KEY = "channel"
    private const val CHANNEL_VERSION_KEY = "channel_version"

    // 存储渠道名于内存中
    private var channelName: String? = null

    /**
     * 返回渠道名（应用市场名），如果获取失败返回defaultChannel
     */
    fun getChannel(context: Context): String? {
        // 内存中获取
        if (!TextUtils.isEmpty(channelName)) {
            return channelName
        }
        // sp中获取
        channelName = getChannelBySharedPreferences(context)
        if (!TextUtils.isEmpty(channelName)) {
            return channelName
        }
        // 从apk中获取
        channelName = getChannelFromApk(context, CHANNEL_KEY)
        if (!TextUtils.isEmpty(channelName)) {
            //保存sp中备用
            setChannelBySharedPreferences(context, channelName)
            return channelName
        }
        // 全部获取失败
        return if (BuildConfig.DEBUG) {
            "test"
        } else {
            "tongyong"
        }
    }

    /**
     * 从apk中获取版本信息
     */
    private fun getChannelFromApk(
        context: Context,
        channelKey: String
    ): String {
        // 从apk包中获取
        val appInfo = context.applicationInfo
        val sourceDir = appInfo.sourceDir
        // 默认放在META-INF/里， 所以需要再拼接一下
        val key = "META-INF/$channelKey"
        var ret = ""
        var zipfile: ZipFile? = null
        try {
            zipfile = ZipFile(sourceDir)
            val entries = zipfile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement() as ZipEntry
                val entryName = entry.name
                if (entryName.startsWith(key)) {
                    ret = entryName
                    break
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                zipfile?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val split = ret.split("_".toRegex()).toTypedArray()
        var channel = ""
        if (split.size >= 2) {
            channel = ret.substring(split[0].length + 1)
        }
        return channel
    }

    /**
     * 本地保存channel & 对应版本号
     */
    private fun setChannelBySharedPreferences(context: Context, channel: String?) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString(CHANNEL_KEY, channel)
        editor.putInt(CHANNEL_VERSION_KEY, getAppVersionCode(context))
        editor.apply()
    }

    /**
     * 从sp中获取channel
     *
     * @return 为空表示获取异常、sp中的值已经失效、sp中没有此值
     */
    private fun getChannelBySharedPreferences(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        val currentVersionCode = getAppVersionCode(context)
        if (currentVersionCode == -1) {
            // 获取错误
            return ""
        }
        val savedVersionCode =
            sharedPreferences.getInt(CHANNEL_VERSION_KEY, -1)
        if (savedVersionCode == -1) {
            // 本地没有存储的channel对应的版本号
            // 第一次使用 或者 原先存储版本号异常
            return ""
        }
        return if (currentVersionCode != savedVersionCode) {
            ""
        } else sharedPreferences.getString(CHANNEL_KEY, "").toString()
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SP_APP, Activity.MODE_PRIVATE)
    }
}