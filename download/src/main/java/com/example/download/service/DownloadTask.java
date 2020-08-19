package com.example.download.service;

import android.content.Context;
import android.content.Intent;

import com.example.download.bean.FileInfo;
import com.example.download.bean.ThreadInfo;
import com.example.download.db.ThreadDAO;
import com.example.download.db.ThreadDAOImpl;
import com.example.download.util.LogUtil;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * @author ShenBF
 * @描述: 下载任务类
 * @date 2018/4/21
 */
public class DownloadTask {

    private Context context = null;
    private FileInfo fileInfo = null;
    private ThreadDAO dao = null;
    private int finished = 0;
    public boolean isPause = false;

    public DownloadTask(Context context, FileInfo fileInfo) {
        this.context = context;
        this.fileInfo = fileInfo;
        dao = new ThreadDAOImpl(context);
    }

    public void download() {
        if (fileInfo == null) {
            LogUtil.d("DownloadTask fileInfo == null");
            return;
        }
        //读取数据库的线程信息
        List<ThreadInfo> threadInfoList = dao.getThreads(fileInfo.url);
        ThreadInfo threadInfo = null;
        if (threadInfoList == null || threadInfoList.size() == 0) {
            //初始化线程信息对象
            threadInfo = new ThreadInfo(0, fileInfo.url, 0, fileInfo.length, 0);
        } else {
            threadInfo = threadInfoList.get(0); // 单线程，所以是0
        }
        //创建子线程进行下载
        new DownloadThread(threadInfo).start();
    }

    /**
     * 下载线程
     */
    class DownloadThread extends Thread {

        private ThreadInfo threadInfo = null;

        public DownloadThread(ThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }

        @Override
        public void run() {
            if (threadInfo == null) {
                LogUtil.d("DownloadTask threadInfo == null");
                return;
            }
            //向数据库插入线程信息
            if (!dao.isExists(threadInfo.url, threadInfo.id)) {
                dao.insertThread(threadInfo);
            }
            HttpURLConnection conn = null;
            InputStream input = null;
            RandomAccessFile raf = null;
            try {
                //设置下载位置
                URL url = new URL(threadInfo.url);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                //设置下载位置
                int start = threadInfo.start + threadInfo.finished;
                conn.setRequestProperty("Range", "bytes=" + start + "-" + threadInfo.end);
                //设置文件写入位置
                File file = new File(DownloadService.DOWNLOAD_PATH, fileInfo.fileName);
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);
                //
                Intent intent = new Intent(DownloadService.ACTION_UPDATE);
                finished += threadInfo.finished;
                //开始下载
                if (conn.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
                    //读取数据
                    input = conn.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int len = -1;
                    long time = System.currentTimeMillis();
                    while ((len = input.read(buffer)) != -1) {
                        //写入文件
                        raf.write(buffer, 0, len);
                        //把下载进入发送广播给Activity
                        finished += len;
                        if (System.currentTimeMillis() - time > 500) {
                            intent.putExtra(ThreadInfo.KEY_FINISHED, finished * 100 / fileInfo.length);
                            context.sendBroadcast(intent);
                        }
                        //在下载暂停时，保存下载进度
                        if (isPause) {
                            dao.updateThread(threadInfo.url, threadInfo.id, finished);
                            return;
                        }
                    }
                    //删除线程信息
                    dao.deleteThread(threadInfo.url, threadInfo.id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.disconnect();
                    }
                    if (input != null) {
                        input.close();
                    }
                    if (raf != null) {
                        raf.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
