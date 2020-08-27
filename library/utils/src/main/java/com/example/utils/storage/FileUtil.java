package com.example.utils.storage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static final long B = 1;
    public static final long KB = B * 1024;
    public static final long MB = KB * 1024;
    public static final long GB = MB * 1024;
    private static final int BUFFER = 8192;

    public static String formatFileSize(long size) {
        StringBuilder sb = new StringBuilder();
        String u = null;
        double tmpSize = 0;
        if (size < KB) {
            sb.append(size).append("B");
            return sb.toString();
        } else if (size < MB) {
            tmpSize = getSize(size, KB);
            u = "KB";
        } else if (size < GB) {
            tmpSize = getSize(size, MB);
            u = "MB";
        } else {
            tmpSize = getSize(size, GB);
            u = "GB";
        }
        return sb.append(twodot(tmpSize)).append(u).toString();
    }

    public static String twodot(double d) {
        return String.format("%.2f", d);
    }

    public static double getSize(long size, long u) {
        return (double) size / (double) u;
    }

    /**
     * 判断sd卡
     */
    public static boolean isSdCardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static void CreateDir(String path) {
        if (!isSdCardMounted())
            return;
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文件
     */
    public static String readTextFile(File file) throws IOException {
        String text = null;
        try (InputStream is = new FileInputStream(file)) {
            text = readTextInputStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * 以UTF-8编码格式将内容写入SDCard
     */
    public static void writeFile(File file, String content) {
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
    public static String readFile(File file) {
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

    public static String readTextInputStream(InputStream is) throws IOException {
        StringBuilder strbuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            while ((line = reader.readLine()) != null) {
                strbuffer.append(line).append("\r\n");
            }
        }
        return strbuffer.toString();
    }

    /**
     * 写文件
     */
    public static void writeTextFile(File file, String str) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
            out.write(str.getBytes());
        }
    }

    public static List<String> getEmojiFile(Context context) {
        try {
            List<String> list = new ArrayList<String>();
            InputStream in = context.getResources().getAssets().open("emoji");//
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }
            br.close();
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getFileSize(File f) {
        long size = 0;
        File[] files = f.listFiles();
        if (files == null) {
            return size;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                size = size + getFileSize(file);
            } else {
                size = size + file.length();
            }
        }
        return size;
    }

    public static void deleteFile(File file) {
        if (null != file && file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File value : files) {
                        deleteFile(value);
                    }
                }
            }
            file.delete();
        }
    }

    public static File getDirectory(Context context, boolean preferExternal, String filename) {
        String externalStorageState;
        File appCacheDir = null;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) {
            externalStorageState = "";
        }
        if ((preferExternal) && ("mounted".equals(externalStorageState)) && (hasExternalStoragePermission(context))) {
            appCacheDir = getExternalCacheDir(context, filename);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/" + filename + "/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        return context.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return appCacheDir;
    }

    public static void clearAllCache(Context context) {
        if (context == null)
            return;
        try {
            cleanInternalCache(context);
            cleanExternalCache(context);
            cleanDatabases(context);
            cleanSharedPreference(context);
            cleanFiles(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * *
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * *
     */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) *
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * *
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File item : files) {
                    item.delete();
                }
            }
        }
    }

    /**
     * * 清除/data/data/com.xxx.xxx/files下的内容 * *
     */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }
}
