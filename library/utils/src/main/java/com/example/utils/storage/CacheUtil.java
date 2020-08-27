package com.example.utils.storage;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 缓存工具类
 */
public class CacheUtil {

    /**
     * 删除缓存目录
     */
    public static void delDir(Context context) {
        if (context == null) return;
        try {
            File file = getDirectory(context, true);
            if (null != file) {
                FileUtil.deleteFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     */
    public static void delCacheFile(Context context, String fileName) {
        if (context == null || TextUtils.isEmpty(fileName)) return;
        try {
            File file = getCacheFilePath(context, fileName);
            if (null != file) {
                FileUtil.deleteFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存缓存文件
     */
    public static void saveCacheFile(Context context, String fileName, Object object) {
        if (null == object || null == context || TextUtils.isEmpty(fileName)) return;
        try {
            File file = getCacheFilePath(context, fileName);
            if (null != file && !file.exists()) {
                file.createNewFile();
            }
            saveFile(object, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取缓存文件
     */
    public static Object getCacheFile(Context context, String fileName) {
        if (null == context) return null;
        try {
            File file = getCacheFilePath(context, fileName);
            return getFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取路径
     */
    private static File getCacheFilePath(Context context, @NonNull String fileName) {
        if (TextUtils.isEmpty(fileName)) return null;
        // 存入数据
        File dirFile = getDirectory(context, true);
        if (dirFile != null && !dirFile.exists()) {
            dirFile.mkdir();
        }
        if (null == dirFile)
            return null;
        else {
            return new File(dirFile.getAbsolutePath() + "/" + fileName);
        }
    }

    /**
     * 保存文件
     */
    private static void saveFile(Object obj, File saveFile) {
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        if (null != saveFile) {
            try {
                fileOutputStream = new FileOutputStream(saveFile.toString());
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
                objectOutputStream.writeObject(obj);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != objectOutputStream)
                        objectOutputStream.close();
                    if (null != bufferedOutputStream)
                        bufferedOutputStream.close();
                    if (null != fileOutputStream)
                        fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取文件
     */
    private static Object getFile(File saveFiles) {
        if (null == saveFiles) return null;
        Object object = null;
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        ObjectInputStream objectInputStream = null;

        //获取文件
        try {
            if (saveFiles.exists() && saveFiles.isFile()) {
                fileInputStream = new FileInputStream(saveFiles.toString());
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                objectInputStream = new ObjectInputStream(bufferedInputStream);
                object = objectInputStream.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }

    private static File getDirectory(Context context, boolean preferExternal) {
        String externalStorageState;
        File appCacheDir = null;
        if (null != context) {
            try {
                externalStorageState = Environment.getExternalStorageState();
            } catch (NullPointerException e) {
                externalStorageState = "";
            }
            if ((preferExternal) && ("mounted".equals(externalStorageState))
                    && (hasExternalStoragePermission(context))) {
                appCacheDir = getExternalCacheDir(context, "cache");
            }
            if (appCacheDir == null) {
                appCacheDir = context.getCacheDir();
            }
            if (appCacheDir == null) {
                String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
                appCacheDir = new File(cacheDirPath);
            }
        }
        return appCacheDir;
    }

    private static File getExternalCacheDir(Context context, String fileName) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), fileName);
        if (!(appCacheDir.exists())) {
            if (!(appCacheDir.mkdirs())) {
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return (perm == 0);
    }
}
