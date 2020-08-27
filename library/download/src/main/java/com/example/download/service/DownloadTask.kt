package com.example.download.service

import android.content.Context
import android.content.Intent
import com.example.download.bean.FileInfo
import com.example.download.bean.ThreadInfo
import com.example.download.db.ThreadDAOImpl
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author fqxyi
 * @date 2018/4/21
 * 下载任务类
 */
class DownloadTask(val context: Context, val fileInfo: FileInfo?) {

    private var dao = ThreadDAOImpl(context)
    private var finished = 0
    var isPause = false

    fun download() {
        if (fileInfo == null) {
            println("DownloadTask fileInfo == null")
            return
        }
        //读取数据库的线程信息
        val threadInfoList = dao.getThreads(fileInfo.url)
        val threadInfo = if (threadInfoList.isNullOrEmpty()) {
            //初始化线程信息对象
            ThreadInfo(0, fileInfo.url, 0, fileInfo.length, 0)
        } else {
            threadInfoList[0]!! // 单线程，所以是0
        }
        //创建子线程进行下载
        DownloadThread(threadInfo).start()
    }

    /**
     * 下载线程
     */
    inner class DownloadThread(private val threadInfo: ThreadInfo) : Thread() {

        override fun run() {
            //向数据库插入线程信息
            if (!dao.isExists(threadInfo.url, threadInfo.id)) {
                dao.insertThread(threadInfo)
            }
            var conn: HttpURLConnection? = null
            var input: InputStream? = null
            var raf: RandomAccessFile? = null
            try {
                //设置下载位置
                val url = URL(threadInfo.url)
                conn = url.openConnection() as HttpURLConnection
                conn.connectTimeout = 3000
                conn.requestMethod = "GET"
                //设置下载位置
                val start = threadInfo.start + threadInfo.finished
                conn.setRequestProperty("Range", "bytes=" + start + "-" + threadInfo.end)
                //设置文件写入位置
                val file = File(DownloadService.DOWNLOAD_PATH, fileInfo!!.fileName)
                raf = RandomAccessFile(file, "rwd")
                raf.seek(start.toLong())
                //
                val intent = Intent(DownloadService.ACTION_UPDATE)
                finished += threadInfo.finished
                //开始下载
                if (conn.responseCode == HttpURLConnection.HTTP_PARTIAL) {
                    //读取数据
                    input = conn.inputStream
                    val buffer = ByteArray(1024 * 4)
                    var len = -1
                    val time = System.currentTimeMillis()
                    while (input.read(buffer).also { len = it } != -1) {
                        //写入文件
                        raf.write(buffer, 0, len)
                        //把下载进入发送广播给Activity
                        finished += len
                        if (System.currentTimeMillis() - time > 500) {
                            intent.putExtra(
                                ThreadInfo.KEY_FINISHED,
                                finished * 100 / fileInfo.length
                            )
                            context.sendBroadcast(intent)
                        }
                        //在下载暂停时，保存下载进度
                        if (isPause) {
                            dao.updateThread(threadInfo.url, threadInfo.id, finished)
                            return
                        }
                    }
                    //删除线程信息
                    dao.deleteThread(threadInfo.url, threadInfo.id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    conn?.disconnect()
                    input?.close()
                    raf?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}