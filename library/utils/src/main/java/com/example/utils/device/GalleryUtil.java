package com.example.utils.device;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * 保存图片到相册的两种方式
 */
public class GalleryUtil {

    /**
     * 方式一：扫描文件通知相册更新
     * @param context
     * @param file
     */
    public static void saveImageByScanFile(Context context, File file) {
        // 下面这句话会导致QQ和微信识别失败
        // MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), "name", "description");

        // 通知相册更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    /**
     * 方式二：完整操作
     * @param context
     * @param filename
     * @param file
     */
    public static void saveImageByContentValuesComplex(Context context, String filename, File file) {
        // 插入file数据到相册
        ContentValues values = new ContentValues(9);
        values.put(MediaStore.Images.Media.TITLE, "Camera");
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.ORIENTATION, 0);
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.SIZE, file.length());
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // 通知相册更新
        context.sendBroadcast(new Intent("com.android.camera.NEW_PICTURE", uri));
    }

    /**
     * 方式二：简单操作
     * @param context
     * @param filename
     * @param file
     */
    public static void saveImageByContentValuesEasy(Context context, String filename, File file) {
        // 插入file数据到相册
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        // 通知相册更新
        context.sendBroadcast(new Intent("com.android.camera.NEW_PICTURE", uri));
    }


}
