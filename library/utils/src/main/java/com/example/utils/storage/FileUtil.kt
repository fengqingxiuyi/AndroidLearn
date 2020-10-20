package com.example.utils.storage

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.*

object FileUtil {
    private const val TAG = "FileUtil"
    private const val B: Long = 1
    private const val KB = B * 1024
    private const val MB = KB * 1024
    private const val GB = MB * 1024

    /**
     * 格式化文件大小
     */
    fun formatFileSize(size: Long): String {
        val sb = StringBuilder()
        var u: String? = null
        var tmpSize = 0.0
        when {
            size < KB -> {
                sb.append(size).append("B")
                return sb.toString()
            }
            size < MB -> {
                tmpSize = size.toDouble() / KB.toDouble()
                u = "KB"
            }
            size < GB -> {
                tmpSize = size.toDouble() / MB.toDouble()
                u = "MB"
            }
            else -> {
                tmpSize = size.toDouble() / GB.toDouble()
                u = "GB"
            }
        }
        return sb.append(String.format("%.2f", tmpSize)).append(u).toString()
    }

    /**
     * 获取文件大小，单位为byte（若为目录，则包括所有子目录和文件）
     */
    fun getFileSize(file: File): Long {
        var size: Long = 0
        if (file.exists()) {
            if (file.isDirectory) {
                file.listFiles()?.let {
                    for (subFile in it) {
                        size += getFileSize(subFile)
                    }
                }
            } else {
                size += file.length()
            }
        }
        return size
    }

    /**
     * 检查是否存在SD卡
     */
    fun hasSDCard(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * 判断某目录下文件是否存在
     */
    fun isFileExists(dir: File, fileName: String): Boolean {
        return File(dir, fileName).exists()
    }

    /**
     * 创建目录
     */
    fun createFileDir(context: Context, dirName: String): File? {
        // 如SD卡已存在，则存储；反之存在data目录下
        val filePath = if (hasSDCard()) {
            // SD卡路径
            Environment.getExternalStorageDirectory().toString() + File.separator + dirName
        } else {
            context.cacheDir.path + File.separator + dirName
        }
        return createFileDir(filePath)
    }

    /**
     * 创建目录
     */
    fun createFileDir(filePath: String): File? {
        if (!hasSDCard()) {
            return null
        }
        val destDir = File(filePath)
        if (!destDir.exists()) {
            val isCreate = destDir.mkdirs()
            Log.i(TAG, "$filePath has created. $isCreate")
        }
        return destDir
    }

    /**
     * 删除文件（若为目录，则递归删除子目录和文件）
     */
    fun deleteFile(file: File) {
        if (!file.exists()) {
            return
        }
        if (file.isDirectory) {
            file.listFiles()?.let {
                for (subFile in it) {
                    deleteFile(subFile)
                }
            }
        } else {
            file.delete()
        }
    }

    /**
     * 删除文件（若为目录，则递归删除子目录和文件）
     */
    fun deleteFile(filePath: String) {
        deleteFile(File(filePath))
    }

    /**
     * 以UTF-8编码格式将内容写入SDCard
     */
    fun writeFile(file: File, content: String?) {
        try {
            if (!file.exists()) {
                file.parent?.let {
                    File(it).mkdirs()
                }
                file.createNewFile()
            }
            val fos = FileOutputStream(file)
            val osw = OutputStreamWriter(fos, "UTF-8")
            osw.write(content)
            osw.flush()
            osw.close()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 以UTF-8编码格式读取SDCard中文件的内容
     */
    @JvmStatic
    fun readFile(file: File): String {
        var res = ""
        try {
            val fis = FileInputStream(file)
            val isr = InputStreamReader(fis, "UTF-8")
            val br = BufferedReader(isr)
            val sb = StringBuilder("")
            var line: String?
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
                sb.append("\n")
            }
            res = sb.toString()
            br.close()
            isr.close()
            fis.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return res
    }

    /**
     * 拷贝本地文件到新的文件路径中
     *
     * @param oldPath 旧的文件路径
     * @param newPath 新的文件路径
     */
    fun copyFile(oldPath: String, newPath: String): Boolean {
        try {
            return copyFile(FileInputStream(oldPath), FileOutputStream(newPath))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 将assets目录下的文件拷贝到新路径中
     *
     * @param oldPath assets目录下文件的相对路径
     */
    fun copyFileFromAssets(
        context: Context,
        oldPath: String,
        newPath: String
    ): Boolean {
        try {
            return copyFile(context.resources.assets.open(oldPath), FileOutputStream(newPath))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    private fun copyFile(`is`: InputStream?, os: OutputStream?): Boolean {
        if (`is` == null || os == null) {
            return false
        }
        try {
            val buffer = ByteArray(1024)
            var byteRead: Int
            while (`is`.read(buffer).also { byteRead = it } != -1) {
                os.write(buffer, 0, byteRead)
            }
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                os.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return false
    }

    /**
     * 文件类型判断工具类
     * 参考自：/libcore/luni/src/main/java/libcore/net/MimeUtils.java
     */
    fun getFileType(suffix: String?): String? {
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix)
        return if (!TextUtils.isEmpty(mimeType)) {
            mimeType!!.split("/".toRegex()).toTypedArray()[0]
        } else null
    }

    /**
     * 默认的文件保存路径
     */
    @JvmStatic
    fun getDefaultFilePath(context: Context): File {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            Environment.getExternalStoragePublicDirectory(context.packageName)
        } else {
            context.getFileStreamPath(context.packageName)
        }
    }
    /*************************************** 缓存  */
    /**
     * 保存缓存文件
     */
    fun saveCacheFile(
        context: Context,
        fileName: String,
        `object`: Any
    ) {
        try {
            val file = getCacheFilePath(context, fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
            saveFile(`object`, file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 保存文件
     */
    private fun saveFile(obj: Any, saveFile: File) {
        var fileOutputStream: FileOutputStream? = null
        var bufferedOutputStream: BufferedOutputStream? = null
        var objectOutputStream: ObjectOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(saveFile.toString())
            bufferedOutputStream = BufferedOutputStream(fileOutputStream)
            objectOutputStream = ObjectOutputStream(bufferedOutputStream)
            objectOutputStream.writeObject(obj)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                objectOutputStream?.close()
                bufferedOutputStream?.close()
                fileOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 获取缓存文件
     */
    fun getCacheFile(context: Context, fileName: String): Any? {
        return try {
            val file = getCacheFilePath(context, fileName)
            getFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 获取文件
     */
    private fun getFile(saveFiles: File): Any? {
        var `object`: Any? = null
        var fileInputStream: FileInputStream? = null
        var bufferedInputStream: BufferedInputStream? = null
        var objectInputStream: ObjectInputStream? = null

        //获取文件
        try {
            if (saveFiles.exists() && saveFiles.isFile) {
                fileInputStream = FileInputStream(saveFiles.toString())
                bufferedInputStream = BufferedInputStream(fileInputStream)
                objectInputStream = ObjectInputStream(bufferedInputStream)
                `object` = objectInputStream.readObject()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fileInputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                bufferedInputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                objectInputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return `object`
    }

    /**
     * 删除文件
     */
    fun delCacheFile(context: Context, fileName: String) {
        if (TextUtils.isEmpty(fileName)) return
        try {
            val file = getCacheFilePath(context, fileName)
            deleteFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取路径
     */
    private fun getCacheFilePath(
        context: Context,
        fileName: String
    ): File {
        // 存入数据
        val dirFile = getCacheDirectory(context, true)
        if (!dirFile.exists()) {
            dirFile.mkdir()
        }
        return File(dirFile.absolutePath + "/" + fileName)
    }

    /**
     * 删除缓存目录
     */
    fun delCacheDir(context: Context) {
        try {
            val file = getCacheDirectory(context, true)
            deleteFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取缓存目录
     */
    private fun getCacheDirectory(
        context: Context,
        preferExternal: Boolean
    ): File {
        var appCacheDir: File? = null
        val externalStorageState = try {
            Environment.getExternalStorageState()
        } catch (e: NullPointerException) {
            ""
        }
        if (preferExternal && "mounted" == externalStorageState
            && hasExternalStoragePermission(context)
        ) {
            appCacheDir = getExternalCacheDir(context, "cache")
        }
        if (appCacheDir == null) {
            appCacheDir = context.cacheDir
        }
        if (appCacheDir == null) {
            val cacheDirPath = "/data/data/" + context.packageName + "/cache/"
            appCacheDir = File(cacheDirPath)
        }
        return appCacheDir
    }

    /**
     * 获取外部存储器的缓存目录
     */
    private fun getExternalCacheDir(
        context: Context,
        fileName: String
    ): File? {
        val dataDir = File(
            File(
                Environment.getExternalStorageDirectory(),
                "Android"
            ), "data"
        )
        val appCacheDir =
            File(File(dataDir, context.packageName), fileName)
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null
            }
            try {
                File(appCacheDir, ".nomedia").createNewFile()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return appCacheDir
    }

    /**
     * 判断是否有存储权限
     */
    private fun hasExternalStoragePermission(context: Context): Boolean {
        return context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0
    }

    /**
     * 获取应用缓存总大小
     */
    @JvmStatic
    fun getTotalCacheSize(context: Context): String {
        var cacheSize = getFileSize(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            cacheSize += getFileSize(context.externalCacheDir!!)
        }
        return formatFileSize(cacheSize)
    }

    /**
     * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * *
     */
    fun cleanInternalCache(context: Context) {
        deleteFile(context.cacheDir)
    }

    /**
     * * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * *
     */
    fun cleanDatabases(context: Context) {
        deleteFile(File("/data/data/" + context.packageName + "/databases"))
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) *
     */
    fun cleanSharedPreference(context: Context) {
        deleteFile(File("/data/data/" + context.packageName + "/shared_prefs"))
    }

    /**
     * * 清除/data/data/com.xxx.xxx/files下的内容 * *
     */
    fun cleanFiles(context: Context) {
        deleteFile(context.filesDir)
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     */
    fun cleanExternalCache(context: Context) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteFile(context.externalCacheDir!!)
        }
    }

    /**
     * 清除所有缓存
     */
    @JvmStatic
    fun clearAllCache(context: Context) {
        try {
            cleanInternalCache(context)
            cleanExternalCache(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 清除所有数据，不包括缓存
     */
    fun clearAllData(context: Context) {
        try {
            cleanDatabases(context)
            cleanSharedPreference(context)
            cleanFiles(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 清除所有缓存和数据
     */
    fun clearAllCacheAndData(context: Context) {
        clearAllCache(context)
        clearAllData(context)
    }
}