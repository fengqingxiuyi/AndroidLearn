package com.example.utils.device

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings

/**
 * 集成一些常用的系统相关的Intent
 */
object IntentUtil {

    /**
     * 跳转到系统的设置界面
     */
    @JvmStatic
    fun getSettingIntent(): Intent {
        return Intent(Settings.ACTION_SETTINGS)
    }

    /**
     * 跳转到系统的WLAN界面
     */
    @JvmStatic
    fun getWifiSettingIntent(): Intent {
        return Intent(Settings.ACTION_WIFI_SETTINGS)
    }

    /**
     * 跳转到系统的手机状态界面
     */
    @JvmStatic
    fun getDeviceInfoSettingIntent(): Intent {
        return Intent(Settings.ACTION_DEVICE_INFO_SETTINGS)
    }

    /**
     * 跳转到系统的开发者选项页面
     */
    @JvmStatic
    fun getAppDevelopSettingIntent(): Intent {
        return Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
    }

    /**
     * 跳转到系统的应用程序列表界面
     */
    @JvmStatic
    fun getAppSettingIntent(): Intent {
        return Intent(Settings.ACTION_APPLICATION_SETTINGS)
    }

    /**
     * 跳转到系统的应用信息页面
     */
    @JvmStatic
    fun getAppDetailSettingIntent(context: Context): Intent {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", context.packageName, null)
        return intent
    }

    /**
     * 调出系统的拨号界面
     */
    @JvmStatic
    fun getShowTelIntent(number: String?): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_DIAL
        intent.data = Uri.fromParts("tel", number, null)
        return intent
    }

    /**
     * 调出系统的短信发送界面
     */
    @JvmStatic
    fun getShowSMSIntent(number: String?): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.fromParts("smsto", number, null)
        return intent
    }

    /**
     * 调用系统的搜索功能
     */
    @JvmStatic
    fun getCallSearchIntent(content: String?): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_WEB_SEARCH
        intent.putExtra(SearchManager.QUERY, content)
        return intent
    }

    /**
     * 调用系统的浏览网页功能
     */
    @JvmStatic
    fun getCallWebIntent(url: String?): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
        return intent
    }

    /**
     * 调用系统的相机功能
     */
    @JvmStatic
    fun getShowCameraIntent(): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_CAMERA_BUTTON
        return intent
    }

    /**
     * 调用系统的相册功能
     */
    @JvmStatic
    fun getShowGalleryIntent(type: String?): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = type
        return intent
    }

    /**
     * 调用系统的录音机功能
     */
    @JvmStatic
    fun getShowRecorderIntent(): Intent {
        val intent = Intent()
        intent.action = MediaStore.Audio.Media.RECORD_SOUND_ACTION
        return intent
    }

    /**
     * 调用系统的分享功能
     */
    @JvmStatic
    fun getCallShareIntent(title: String?, content: String?): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TITLE, title)
        intent.putExtra(Intent.EXTRA_TEXT, content)
        return intent
    }
}