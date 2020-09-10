package com.example.utils.device;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;

/**
 * 集成一些常用的系统相关的Intent
 */
public class IntentUtil {

    /**
     * 跳转到系统的设置界面
     */
    public static Intent getSettingIntent() {
        return new Intent(Settings.ACTION_SETTINGS);
    }

    /**
     * 跳转到系统的WLAN界面
     */
    public static Intent getWifiSettingIntent() {
        return new Intent(Settings.ACTION_WIFI_SETTINGS);
    }

    /**
     * 跳转到系统的手机状态界面
     */
    public static Intent getDeviceInfoSettingIntent() {
        return new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS);
    }

    /**
     * 跳转到系统的开发者选项页面
     */
    public static Intent getAppDevelopSettingIntent() {
        return new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
    }

    /**
     * 跳转到系统的应用程序列表界面
     */
    public static Intent getAppSettingIntent() {
        return new Intent(Settings.ACTION_APPLICATION_SETTINGS);
    }

    /**
     * 跳转到系统的应用信息页面
     */
    public static Intent getAppDetailSettingIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return intent;
    }

    /**
     * 调出系统的拨号界面
     */
    public static Intent getShowTelIntent(String number) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.fromParts("tel", number, null));
        return intent;
    }

    /**
     * 调出系统的短信发送界面
     */
    public static Intent getShowSMSIntent(String number) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.fromParts("smsto", number, null));
        return intent;
    }

    /**
     * 调用系统的搜索功能
     */
    public static Intent getCallSearchIntent(String content) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, content);
        return intent;
    }

    /**
     * 调用系统的浏览网页功能
     */
    public static Intent getCallWebIntent(String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        return intent;
    }

    /**
     * 调用系统的相机功能
     */
    public static Intent getShowCameraIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CAMERA_BUTTON);
        return intent;
    }

    /**
     * 调用系统的相册功能
     */
    public static Intent getShowGalleryIntent(String type) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        return intent;
    }

    /**
     * 调用系统的录音机功能
     */
    public static Intent getShowRecorderIntent() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        return intent;
    }

    /**
     * 调用系统的分享功能
     */
    public static Intent getCallShareIntent(String title, String content) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        return intent;
    }
}
