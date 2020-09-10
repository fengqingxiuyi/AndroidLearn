package com.example.utils.download;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.utils.device.GalleryUtil;
import com.example.utils.network.UrlUtil;
import com.example.utils.storage.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多文件下载类（支持单文件下载）
 */
public class DownloadUtil {

    private static final String TAG = DownloadUtil.class.getSimpleName();

    private Context context;

    // 线程池
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);
    // 存储文件夹
    private File parentDir;
    // 存储文件名
    private String fileName;
    // 最大重新总执行次数
    private static final int MAX_RETRY_TIMES = 2;

    // 原始下载链接
    private String sourceLink;
    // 原始下载链接集合
    private List<String> sourceLinks;
    // 失败的链接的索引的集合
    private List<Integer> failureIndexs = new ArrayList<>();

    // 下载状态监听，提供回调
    private DownloadStateListener listener;

    private int finishedCount = 0;

    public interface DownloadStateListener {
        void onStart();

        void onFailure(List<Integer> failureIndexs);

        void onAllSucceed(String fileDownloadDir);

        void onError(IOException e);
    }

    public DownloadUtil(Context context, @NonNull File parentDir,
                        @NonNull String sourceLink, DownloadStateListener listener) {
        // 初始化目录
        initFile(parentDir);
        this.context = context;
        this.parentDir = parentDir;
        this.sourceLink = sourceLink;
        this.listener = listener;
    }

    public DownloadUtil(Context context, @NonNull File parentDir,
                        @NonNull String fileName, @NonNull String sourceLink,
                        DownloadStateListener listener) {
        // 初始化目录
        initFile(parentDir);
        this.context = context;
        this.parentDir = parentDir;
        this.fileName = fileName;
        this.sourceLink = sourceLink;
        this.listener = listener;
    }

    /**
     * 构造方法
     *
     * @param parentDir   存储文件的目录
     * @param sourceLinks 要下载的文件链接集合
     * @param listener    下载状态监听器
     */
    public DownloadUtil(Context context, @NonNull File parentDir,
                        @NonNull List<String> sourceLinks, DownloadStateListener listener) {
        // 初始化目录
        initFile(parentDir);
        this.context = context;
        this.parentDir = parentDir;
        this.sourceLinks = sourceLinks;
        this.listener = listener;
    }

    private void initFile(File parentDir) {
        if (parentDir == null) {
            throw new RuntimeException("存储文件的目录不能为NULL");
        } else {
            if (parentDir.exists()) {
                return;
            }
            if (parentDir.mkdirs()) {
                return;
            }
            throw new RuntimeException("存储文件的目录创建失败，请检查权限是否正常等情况");
        }
    }

    public void update(@NonNull String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public void update(@NonNull String fileName, @NonNull String sourceLink) {
        this.fileName = fileName;
        this.sourceLink = sourceLink;
    }

    public void startDownload() {
        // 线程放入线程池，增加isShutdown()判断
        if (!executorService.isShutdown()) {
            // 开始执行
            if (listener != null) {
                listener.onStart();
            }
            if (getSingleDownload()) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        saveFile(0, sourceLink);
                    }
                });
            } else {
                for (int i = 0; i < sourceLinks.size(); i++) {
                    final int index = i;
                    final String url = sourceLinks.get(index);
                    if (!TextUtils.isEmpty(url)) {
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                saveFile(index, url);
                            }
                        });
                    }
                }
            }
        } else {
            Log.e(TAG, "线程已关闭");
        }
    }

    private void saveFile(int index, String url) {
        int retryTimes = 0;
        do {
            if (downloadFile(url)) {
                finishedCount++;
                if (getSingleDownload()) {
                    if (finishedCount == 1) {
                        allFinished();
                    }
                } else {
                    if (finishedCount == sourceLinks.size()) {
                        allFinished();
                    }
                }
                return;
            }
            retryTimes++;
        } while (retryTimes < MAX_RETRY_TIMES);

        failureIndexs.add(index);
        finishedCount++;

        if (getSingleDownload()) {
            if (finishedCount == 1) {
                allFinished();
            }
        } else {
            if (finishedCount == sourceLinks.size()) {
                allFinished();
            }
        }
    }

    private boolean downloadFile(String urlString) {
        if (TextUtils.isEmpty(urlString)) {
            return true;
        }

        String decodeUrl = UrlUtil.decodeUrl(urlString);

        File file;
        if (TextUtils.isEmpty(fileName)) {
            file = new File(parentDir, decodeUrl.substring(decodeUrl.lastIndexOf("/") + 1));
        } else {
            file = new File(parentDir, fileName);
        }

        if (decodeUrl.startsWith("//")) {
            decodeUrl = "http:" + decodeUrl;
        }
        if (!decodeUrl.startsWith("http")) {
            // 非网络文件
            decodeUrl = decodeUrl.substring(7);
            // 拷贝文件到parentDir
            FileUtil.copyFile(decodeUrl, file.getAbsolutePath());
            // 更新图库
            updateGallery(decodeUrl, file);
            return true;
        }

        HttpURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        try {
            // 构造URL
            URL url = new URL(UrlUtil.encodeUrl(urlString));
            // 打开连接
            conn = (HttpURLConnection) url.openConnection();
            // 输入流
            is = conn.getInputStream();
            // 1K的数据缓冲
            byte[] b = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            os = new FileOutputStream(file);
            // 开始读取
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }
            // 更新图库
            updateGallery(decodeUrl, file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onError(e);
            }
        } finally {
            // 完毕，关闭所有链接
            if (null != conn) {
                conn.disconnect();
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 全部完成时调用
     */
    private void allFinished() {
        finishedCount = 0;
        if (failureIndexs.size() == 0) {
            // 全部下载成功
            if (listener != null) {
                listener.onAllSucceed(parentDir.getAbsolutePath());
            }
        } else {
            if (listener != null) {
                listener.onFailure(failureIndexs);
            }
        }
        failureIndexs.clear();
    }

    /**
     * 扫描文件通知相册更新
     */
    private void updateGallery(String url, File file) {
        if ("image".equals(
                FileUtil.getFileType(url.substring(url.lastIndexOf(".") + 1)))) {
            GalleryUtil.saveImageByScanFile(context, file);
        }
    }

    /**
     * true：下载单个链接，false：下载链接集合
     */
    private boolean getSingleDownload() {
        return sourceLinks == null && !TextUtils.isEmpty(sourceLink);
    }

}
