package com.example.utils.download

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.example.utils.device.GalleryUtil.saveImageByScanFile
import com.example.utils.network.UrlUtil
import com.example.utils.storage.FileUtil
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.Executors

/**
 * 多文件下载类（支持单文件下载）
 */
class DownloadUtil {

    companion object {
        private val TAG = DownloadUtil::class.java.simpleName

        // 最大重新总执行次数
        private const val MAX_RETRY_TIMES = 2
    }

    private var context: Context

    // 线程池
    private val executorService = Executors.newFixedThreadPool(3)

    // 存储文件夹
    private var parentDir: File

    // 存储文件名
    private var fileName: String? = null

    // 原始下载链接
    private var sourceLink: String? = null

    // 原始下载链接集合
    private lateinit var sourceLinks: List<String>

    // 失败的链接的索引的集合
    private val failureIndexs = ArrayList<Int>()

    // 下载状态监听，提供回调
    private var listener: DownloadStateListener?
    private var finishedCount = 0

    interface DownloadStateListener {
        fun onStart()
        fun onFailure(failureIndexs: List<Int>?)
        fun onAllSucceed(fileDownloadDir: String?)
        fun onError(e: IOException?)
    }

    constructor(
        context: Context, parentDir: File,
        sourceLink: String, listener: DownloadStateListener?
    ) {
        // 初始化目录
        initFile(parentDir)
        this.context = context
        this.parentDir = parentDir
        this.sourceLink = sourceLink
        this.listener = listener
    }

    constructor(
        context: Context, parentDir: File,
        fileName: String, sourceLink: String,
        listener: DownloadStateListener?
    ) {
        // 初始化目录
        initFile(parentDir)
        this.context = context
        this.parentDir = parentDir
        this.fileName = fileName
        this.sourceLink = sourceLink
        this.listener = listener
    }

    /**
     * 构造方法
     *
     * @param parentDir   存储文件的目录
     * @param sourceLinks 要下载的文件链接集合
     * @param listener    下载状态监听器
     */
    constructor(
        context: Context, parentDir: File,
        sourceLinks: List<String>, listener: DownloadStateListener?
    ) {
        // 初始化目录
        initFile(parentDir)
        this.context = context
        this.parentDir = parentDir
        this.sourceLinks = sourceLinks
        this.listener = listener
    }

    private fun initFile(parentDir: File?) {
        if (parentDir == null) {
            throw RuntimeException("存储文件的目录不能为NULL")
        } else {
            if (parentDir.exists()) {
                return
            }
            if (parentDir.mkdirs()) {
                return
            }
            throw RuntimeException("存储文件的目录创建失败，请检查权限是否正常等情况")
        }
    }

    fun update(sourceLink: String) {
        this.sourceLink = sourceLink
    }

    fun update(fileName: String, sourceLink: String) {
        this.fileName = fileName
        this.sourceLink = sourceLink
    }

    fun startDownload() {
        // 线程放入线程池，增加isShutdown()判断
        if (!executorService.isShutdown) {
            // 开始执行
            listener?.onStart()
            if (getSingleDownload()) {
                executorService.execute { saveFile(0, sourceLink) }
            } else {
                for (i in sourceLinks.indices) {
                    val url = sourceLinks[i]
                    if (!TextUtils.isEmpty(url)) {
                        executorService.execute { saveFile(i, url) }
                    }
                }
            }
        } else {
            Log.e(TAG, "线程已关闭")
        }
    }

    private fun saveFile(index: Int, url: String?) {
        var retryTimes = 0
        do {
            if (downloadFile(url)) {
                finishedCount++
                if (getSingleDownload()) {
                    if (finishedCount == 1) {
                        allFinished()
                    }
                } else {
                    if (finishedCount == sourceLinks.size) {
                        allFinished()
                    }
                }
                return
            }
            retryTimes++
        } while (retryTimes < MAX_RETRY_TIMES)
        failureIndexs.add(index)
        finishedCount++
        if (getSingleDownload()) {
            if (finishedCount == 1) {
                allFinished()
            }
        } else {
            if (finishedCount == sourceLinks.size) {
                allFinished()
            }
        }
    }

    private fun downloadFile(urlString: String?): Boolean {
        if (TextUtils.isEmpty(urlString)) {
            return true
        }
        var decodeUrl = UrlUtil.decodeUrl(urlString!!)
        val file: File
        file = if (TextUtils.isEmpty(fileName)) {
            File(parentDir, decodeUrl.substring(decodeUrl.lastIndexOf("/") + 1))
        } else {
            File(parentDir, fileName!!)
        }
        if (decodeUrl.startsWith("//")) {
            decodeUrl = "http:$decodeUrl"
        }
        if (!decodeUrl.startsWith("http")) {
            // 非网络文件
            decodeUrl = decodeUrl.substring(7)
            // 拷贝文件到parentDir
            FileUtil.copyFile(decodeUrl, file.absolutePath)
            // 更新图库
            updateGallery(decodeUrl, file)
            return true
        }
        var conn: HttpURLConnection? = null
        var `is`: InputStream? = null
        var os: OutputStream? = null
        try {
            // 构造URL
            val url =
                URL(UrlUtil.encodeUrl(urlString))
            // 打开连接
            conn = url.openConnection() as HttpURLConnection
            // 输入流
            `is` = conn.inputStream
            // 1K的数据缓冲
            val b = ByteArray(1024)
            // 读取到的数据长度
            var len: Int
            // 输出的文件流
            os = FileOutputStream(file)
            // 开始读取
            while (`is`.read(b).also { len = it } != -1) {
                os.write(b, 0, len)
            }
            // 更新图库
            updateGallery(decodeUrl, file)
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            listener?.onError(e)
        } finally {
            // 完毕，关闭所有链接
            conn?.disconnect()
            try {
                `is`?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return false
    }

    /**
     * 全部完成时调用
     */
    private fun allFinished() {
        finishedCount = 0
        if (failureIndexs.size == 0) {
            // 全部下载成功
            listener?.onAllSucceed(parentDir.absolutePath)
        } else {
            listener?.onFailure(failureIndexs)
        }
        failureIndexs.clear()
    }

    /**
     * 扫描文件通知相册更新
     */
    private fun updateGallery(url: String, file: File) {
        if ("image" == FileUtil.getFileType(url.substring(url.lastIndexOf(".") + 1))) {
            saveImageByScanFile(context, file)
        }
    }

    /**
     * true：下载单个链接，false：下载链接集合
     */
    private fun getSingleDownload(): Boolean {
        return !sourceLink.isNullOrEmpty()
    }
}