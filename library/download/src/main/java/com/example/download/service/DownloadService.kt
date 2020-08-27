package com.example.download.service

import android.app.Service
import android.content.Intent
import android.os.Environment
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.example.download.bean.FileInfo
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author fqxyi
 * @date 2018/4/20
 */
class DownloadService : Service() {

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_UPDATE = "ACTION_UPDATE"

        val DOWNLOAD_PATH = Environment.getExternalStorageDirectory().absolutePath + "/downloads/"

        const val MSG_INIT = 0
    }

    private var task: DownloadTask? = null
    private var handler = DownloadHandler()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) { //获得Activity传来的参数
            if (ACTION_START == intent.action) {
                val fileInfo = intent.getSerializableExtra(FileInfo.KEY) as FileInfo?
                println("DownloadService start: $fileInfo")
                //启动初始化线程
                InitThread(fileInfo).start()
            } else if (ACTION_STOP == intent.action) {
                val fileInfo = intent.getSerializableExtra(FileInfo.KEY) as FileInfo?
                println("DownloadService stop: $fileInfo")
                task?.isPause = true
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    inner class DownloadHandler : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == MSG_INIT) {
                val fileInfo = msg.obj as FileInfo?
                if (fileInfo == null) {
                    println("DownloadHandler fileInfo == null")
                    return
                }
                println("DownloadHandler init: $fileInfo")
                //启动下载任务
                task = DownloadTask(this@DownloadService, fileInfo)
                task!!.download()
            }
        }
    }

    /**
     * 初始化子线程
     */
    inner class InitThread(private val fileInfo: FileInfo?) : Thread() {
        override fun run() {
            if (fileInfo == null) {
                println("InitThread fileInfo == null")
                return
            }
            var conn: HttpURLConnection? = null
            var raf: RandomAccessFile? = null
            try {
                //连接网络文件
                val url = URL(fileInfo.url)
                conn = url.openConnection() as HttpURLConnection
                conn.connectTimeout = 3000
                conn.requestMethod = "GET"
                var length = -1
                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    //获取文件长度
                    length = conn.contentLength
                }
                if (length <= 0) {
                    println("InitThread length <= 0")
                    return
                }
                val dir = File(DOWNLOAD_PATH)
                if (!dir.exists()) {
                    dir.mkdir()
                }
                //在本地创建文件
                val file = File(dir, fileInfo.fileName)
                raf = RandomAccessFile(file, "rwd")
                //设置文件长度
                raf.setLength(length.toLong())
                fileInfo.length = length
                handler.obtainMessage(MSG_INIT, fileInfo).sendToTarget()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                conn?.disconnect()
                try {
                    raf?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

}