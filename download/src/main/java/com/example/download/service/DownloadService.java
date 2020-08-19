package com.example.download.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.example.download.bean.FileInfo;
import com.example.download.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author ShenBF
 * @描述:
 * @date 2018/4/20
 */
public class DownloadService extends Service {

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";

    public static final String DOWNLOAD_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";

    public static final int MSG_INIT = 0;

    private DownloadTask task = null;

    private Handler handler = new DownloadHandler();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) { //获得Activity传来的参数
            if (ACTION_START.equals(intent.getAction())) {
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(FileInfo.KEY);
                LogUtil.d("DownloadService start: " + fileInfo.toString());
                //启动初始化线程
                new InitThread(fileInfo).start();
            } else if (ACTION_STOP.equals(intent.getAction())) {
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(FileInfo.KEY);
                LogUtil.d("DownloadService stop: " + fileInfo.toString());
                if (task != null) {
                    task.isPause = true;
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class DownloadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INIT:
                    FileInfo fileInfo = (FileInfo) msg.obj;
                    if (fileInfo == null) {
                        LogUtil.d("DownloadHandler fileInfo == null");
                        return;
                    }
                    LogUtil.d("DownloadHandler init: " + fileInfo.toString());
                    //启动下载任务
                    task = new DownloadTask(DownloadService.this, fileInfo);
                    task.download();
                    break;
            }
        }
    }

    /**
     * 初始化子线程
     */
    class InitThread extends Thread {
        private FileInfo fileInfo;

        public InitThread(FileInfo fileInfo) {
            this.fileInfo = fileInfo;
        }

        @Override
        public void run() {
            if (fileInfo == null) {
                LogUtil.d("InitThread fileInfo == null");
                return;
            }
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            try {
                //连接网络文件
                URL url = new URL(fileInfo.url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                int length = -1;
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    //获取文件长度
                    length = conn.getContentLength();
                }
                if (length <= 0) {
                    LogUtil.d("InitThread length <= 0");
                    return;
                }
                File dir = new File(DOWNLOAD_PATH);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                //在本地创建文件
                File file = new File(dir, fileInfo.fileName);
                raf = new RandomAccessFile(file, "rwd");
                //设置文件长度
                raf.setLength(length);
                fileInfo.length = length;
                handler.obtainMessage(MSG_INIT, fileInfo).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                try {
                    if (raf != null) {
                        raf.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
