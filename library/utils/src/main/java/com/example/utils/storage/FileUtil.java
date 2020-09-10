package com.example.utils.storage;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class FileUtil {

    private static final String TAG = "FileUtil";

    public static final long B = 1;
    public static final long KB = B * 1024;
    public static final long MB = KB * 1024;
    public static final long GB = MB * 1024;

    /**
     * 格式化文件大小
     */
    public static String formatFileSize(long size) {
        StringBuilder sb = new StringBuilder();
        String u = null;
        double tmpSize = 0;
        if (size < KB) {
            sb.append(size).append("B");
            return sb.toString();
        } else if (size < MB) {
            tmpSize = (double) size / (double) KB;
            u = "KB";
        } else if (size < GB) {
            tmpSize = (double) size / (double) MB;
            u = "MB";
        } else {
            tmpSize = (double) size / (double) GB;
            u = "GB";
        }
        return sb.append(String.format("%.2f", tmpSize)).append(u).toString();
    }

    /**
     * 获取文件大小，单位为byte（若为目录，则包括所有子目录和文件）
     */
    public static long getFileSize(@NonNull File file) {
        long size = 0;
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        size += getFileSize(subFile);
                    }
                }
            } else {
                size += file.length();
            }
        }
        return size;
    }

    /**
     * 检查是否存在SD卡
     */
    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断某目录下文件是否存在
     */
    public static boolean isFileExists(@NonNull File dir, String fileName) {
        return new File(dir, fileName).exists();
    }

    /**
     * 创建目录
     */
    public static File createFileDir(@NonNull Context context, String dirName) {
        String filePath;
        // 如SD卡已存在，则存储；反之存在data目录下
        if (hasSDCard()) {
            // SD卡路径
            filePath = Environment.getExternalStorageDirectory() + File.separator + dirName;
        } else {
            filePath = context.getCacheDir().getPath() + File.separator + dirName;
        }
        return createFileDir(filePath);
    }

    /**
     * 创建目录
     */
    public static File createFileDir(@NonNull String filePath) {
        if (!hasSDCard()) {
            return null;
        }
        File destDir = new File(filePath);
        if (!destDir.exists()) {
            boolean isCreate = destDir.mkdirs();
            Log.i(TAG, filePath + " has created. " + isCreate);
        }
        return destDir;
    }

    /**
     * 删除文件（若为目录，则递归删除子目录和文件）
     */
    public static void deleteFile(@NonNull File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null) {
                // 删除子目录和文件
                for (File subFile : subFiles) {
                    deleteFile(subFile);
                }
            }
        } else {
            file.delete();
        }
    }

    /**
     * 删除文件（若为目录，则递归删除子目录和文件）
     */
    public static void deleteFile(@NonNull String filePath) {
        deleteFile(new File(filePath));
    }

    /**
     * 以UTF-8编码格式将内容写入SDCard
     */
    public static void writeFile(@NonNull File file, String content) {
        try {
            if (!file.exists()) {
                if (null != file.getParent()) {
                    File dir = new File(file.getParent());
                    dir.mkdirs();
                }
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(content);
            osw.flush();
            osw.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 以UTF-8编码格式读取SDCard中文件的内容
     */
    public static String readFile(@NonNull File file) {
        String res = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder("");
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            res = sb.toString();
            br.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 拷贝本地文件到新的文件路径中
     *
     * @param oldPath 旧的文件路径
     * @param newPath 新的文件路径
     */
    public static boolean copyFile(@NonNull String oldPath, @NonNull String newPath) {
        try {
            return copyFile(new FileInputStream(oldPath), new FileOutputStream(newPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将assets目录下的文件拷贝到新路径中
     *
     * @param oldPath assets目录下文件的相对路径
     */
    public static boolean copyFileFromAssets(@NonNull Context context, @NonNull String oldPath, @NonNull String newPath) {
        try {
            return copyFile(context.getResources().getAssets().open(oldPath), new FileOutputStream(newPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean copyFile(InputStream is, OutputStream os) {
        if (is == null || os == null) {
            return false;
        }
        try {
            byte[] buffer = new byte[1024];
            int byteRead;
            while ((byteRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, byteRead);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 文件类型判断工具类
     * 参考自：/libcore/luni/src/main/java/libcore/net/MimeUtils.java
     */
    public static String getFileType(String suffix) {
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (!TextUtils.isEmpty(mimeType)) {
            return mimeType.split("/")[0];
        }
        return null;
    }

    /**
     * 默认的文件保存路径
     */
    public static File getDefaultFilePath(Context context) {
        File file;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            file = Environment.getExternalStoragePublicDirectory(context.getPackageName());
        } else {
            file = context.getFileStreamPath(context.getPackageName());
        }
        return file;
    }

    /*************************************** 缓存 ***************************************/

    /**
     * 保存缓存文件
     */
    public static void saveCacheFile(@NonNull Context context, @NonNull String fileName, @NonNull Object object) {
        if (TextUtils.isEmpty(fileName)) return;
        try {
            File file = getCacheFilePath(context, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            saveFile(object, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存文件
     */
    private static void saveFile(@NonNull Object obj, @NonNull File saveFile) {
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
     * 获取缓存文件
     */
    public static Object getCacheFile(@NonNull Context context, @NonNull String fileName) {
        try {
            File file = getCacheFilePath(context, fileName);
            return getFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取文件
     */
    private static Object getFile(@NonNull File saveFiles) {
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

    /**
     * 删除文件
     */
    public static void delCacheFile(@NonNull Context context, @NonNull String fileName) {
        if (TextUtils.isEmpty(fileName)) return;
        try {
            File file = getCacheFilePath(context, fileName);
            FileUtil.deleteFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取路径
     */
    private static File getCacheFilePath(@NonNull Context context, @NonNull String fileName) {
        // 存入数据
        File dirFile = getCacheDirectory(context, true);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        return new File(dirFile.getAbsolutePath() + "/" + fileName);
    }

    /**
     * 删除缓存目录
     */
    public static void delCacheDir(@NonNull Context context) {
        try {
            File file = getCacheDirectory(context, true);
            FileUtil.deleteFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取缓存目录
     */
    private static File getCacheDirectory(@NonNull Context context, boolean preferExternal) {
        String externalStorageState;
        File appCacheDir = null;
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
        return appCacheDir;
    }

    /**
     * 获取外部存储器的缓存目录
     */
    private static File getExternalCacheDir(@NonNull Context context, @NonNull String fileName) {
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

    /**
     * 判断是否有存储权限
     */
    private static boolean hasExternalStoragePermission(@NonNull Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return (perm == 0);
    }

    /**
     * 获取应用缓存总大小
     */
    public static String getTotalCacheSize(@NonNull Context context) {
        long cacheSize = getFileSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFileSize(context.getExternalCacheDir());
        }
        return formatFileSize(cacheSize);
    }

    /**
     * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * *
     */
    public static void cleanInternalCache(@NonNull Context context) {
        deleteFile(context.getCacheDir());
    }

    /**
     * * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * *
     */
    public static void cleanDatabases(@NonNull Context context) {
        deleteFile(new File("/data/data/" + context.getPackageName() + "/databases"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) *
     */
    public static void cleanSharedPreference(@NonNull Context context) {
        deleteFile(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * * 清除/data/data/com.xxx.xxx/files下的内容 * *
     */
    public static void cleanFiles(@NonNull Context context) {
        deleteFile(context.getFilesDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     */
    public static void cleanExternalCache(@NonNull Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFile(context.getExternalCacheDir());
        }
    }

    /**
     * 清除所有缓存
     */
    public static void clearAllCache(@NonNull Context context) {
        try {
            cleanInternalCache(context);
            cleanExternalCache(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除所有数据，不包括缓存
     */
    public static void clearAllData(@NonNull Context context) {
        try {
            cleanDatabases(context);
            cleanSharedPreference(context);
            cleanFiles(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除所有缓存和数据
     */
    public static void clearAllCacheAndData(@NonNull Context context) {
        clearAllCache(context);
        clearAllData(context);
    }

}
