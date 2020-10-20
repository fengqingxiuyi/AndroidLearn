package com.example.utils.view

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.opengl.GLES10
import android.os.Build
import android.text.TextUtils
import android.util.Base64
import android.view.View
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import kotlin.math.ceil

/**
 * 图片操作工具类
 */
object ImageUtil {
    @JvmStatic
    fun getLocalImagePath(parentDir: File?, image: String?): String? {
        if (parentDir == null || image.isNullOrEmpty()) {
            return null
        }
        return if (image.startsWith("http")) {
            val fileName = image.substring(image.lastIndexOf("/") + 1)
            if (TextUtils.isEmpty(fileName)) {
                return null
            }
            if (!parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    return null
                }
            }
            val file = File(parentDir, fileName)
            if (!file.exists()) {
                try {
                    if (!file.createNewFile()) {
                        return null
                    }
                } catch (e: IOException) {
                    return null
                }
            }
            val success = downloadImage(file, image)
            if (success) {
                file.absolutePath
            } else {
                null
            }
        } else {
            image
        }
    }

    /**
     * 下载图片
     *
     * @param file     图片存放位置
     * @param imageUrl 图片地址
     * @return true 下载成功 false 下载失败
     */
    @JvmStatic
    fun downloadImage(file: File?, imageUrl: String?): Boolean {
        if (file == null || imageUrl.isNullOrEmpty()) {
            return false
        }
        var conn: HttpURLConnection? = null
        var `is`: InputStream? = null
        var os: OutputStream? = null
        try {
            // 构造URL
            val url = URL(imageUrl)
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
            return true
        } catch (e: IOException) {
            e.printStackTrace()
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
     * 将图片地址转换为Bitmap
     *
     * @param imageUrl 图片地址
     * @return Bitmap
     */
    fun getImageBitmap(imageUrl: String?, maxSize: Long): Bitmap? {
        if (imageUrl.isNullOrEmpty()) {
            return null
        }
        if (!imageUrl.startsWith("http")) {
            return compressBitmapGetBitmap(
                imageUrl,
                null,
                imageUrl,
                maxSize
            )
        }
        try {
            return compressBitmapGetBitmap(
                null,
                URL(imageUrl),
                imageUrl,
                maxSize
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 将图片地址转换为byte[]
     *
     * @param imageUrl 图片地址
     * @return byte[]
     */
    @JvmStatic
    fun getImageByte(imageUrl: String?, maxSize: Long): ByteArray? {
        if (imageUrl.isNullOrEmpty()) {
            return null
        }
        if (!imageUrl.startsWith("http")) {
            return compressBitmapGetByte(
                imageUrl,
                null,
                imageUrl,
                maxSize
            )
        }
        try {
            return compressBitmapGetByte(
                null,
                URL(imageUrl),
                imageUrl,
                maxSize
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 图片压缩
     *
     * @param url 图片地址
     * @return Bitmap
     */
    fun compressBitmapGetBitmap(
        localFilePath: String?,
        url: URL?,
        imageUrl: String,
        maxSize: Long
    ): Bitmap? {
        return compressBitmapQualityGetBitmap(
            compressBitmapSize(localFilePath, url), maxSize, imageUrl
        )
    }

    /**
     * 图片压缩
     *
     * @param url 图片地址
     * @return byte[]
     */
    fun compressBitmapGetByte(
        localFilePath: String?,
        url: URL?,
        imageUrl: String,
        maxSize: Long
    ): ByteArray? {
        return compressBitmapQualityGetByte(
            compressBitmapSize(localFilePath, url), maxSize, imageUrl
        )
    }

    /**
     * 尺寸压缩 限制图片的最大边长为200px
     *
     * @param url 图片地址
     * @return Bitmap
     */
    fun compressBitmapSize(
        localFilePath: String?,
        url: URL?
    ): Bitmap? {
        if (url == null) {
            return null
        }
        val options = BitmapFactory.Options()
        // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
        options.inJustDecodeBounds = true
        if (!TextUtils.isEmpty(localFilePath)) {
            BitmapFactory.decodeFile(localFilePath)
        } else {
            try {
                //打开连接
                val conn = url.openConnection() as HttpURLConnection
                BitmapFactory.decodeStream(conn.inputStream, null, options)
                //关闭连接
                conn.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        }
        // 获取图片宽高
        val height = options.outHeight
        val width = options.outWidth
        // 默认像素压缩比例，压缩为原图的1/1
        var inSampleSize = 1
        // 获取图片的最大边长
        val maxLen = height.coerceAtLeast(width)
        if (maxLen > 200) {
            //向上取整
            inSampleSize = ceil(maxLen / 200f.toDouble()).toInt()
        }
        // 计算好压缩比例后，这次可以去加载原图了
        options.inJustDecodeBounds = false
        // 设置为刚才计算的压缩比例
        options.inSampleSize = inSampleSize
        //
        options.inPreferredConfig = Bitmap.Config.RGB_565
        return if (!TextUtils.isEmpty(localFilePath)) {
            BitmapFactory.decodeFile(localFilePath)
        } else {
            try {
                //重新打开连接
                val conn = url.openConnection() as HttpURLConnection
                val bitmap = BitmapFactory.decodeStream(conn.inputStream, null, options)
                //重新关闭连接
                conn.disconnect()
                bitmap
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    fun compressBitmapQualityGetBitmap(
        bitmap: Bitmap?,
        maxSize: Long,
        imageUrl: String
    ): Bitmap? {
        val byteArray =
            compressBitmapQuality(bitmap, maxSize, imageUrl)?.toByteArray() ?: return null
        return BitmapFactory.decodeStream(ByteArrayInputStream(byteArray))
    }

    fun compressBitmapQualityGetByte(
        bitmap: Bitmap?,
        maxSize: Long,
        imageUrl: String
    ): ByteArray? {
        return compressBitmapQuality(bitmap, maxSize, imageUrl)?.toByteArray()
    }

    /**
     * 质量压缩 压缩Bitmap到指定的大小范围内
     *
     * @param maxSize Bitmap被允许占有的最大大小，单位为KB
     * @return Bitmap
     */
    private fun compressBitmapQuality(
        bitmap: Bitmap?,
        maxSize: Long,
        imageUrl: String
    ): ByteArrayOutputStream? {
        if (bitmap == null) {
            return null
        }
        // Bitmap默认质量为100，表示从未被压缩过
        var quality = 100
        val baos = ByteArrayOutputStream()
        val format = getBmpFormat(imageUrl)
        bitmap.compress(format, quality, baos)
        while (baos.toByteArray().size * 1024 > maxSize && quality > 20) {
            baos.reset()
            bitmap.compress(format, quality, baos)
            quality -= 10
        }
        bitmap.recycle()
        try {
            baos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return baos
    }

    fun getBmpFormat(filePath: String): CompressFormat {
        if (filePath.isEmpty()) {
            return CompressFormat.JPEG
        }
        val pathLower = filePath.toLowerCase(Locale.getDefault())
        val format: CompressFormat
        format = if (!pathLower.endsWith("png") && !pathLower.endsWith("gif")) {
            if (!pathLower.endsWith("jpg") && !pathLower.endsWith("jpeg") &&
                !pathLower.endsWith("bmp") && !pathLower.endsWith("tif")
            ) {
                val mime = getMime(filePath)
                if (!mime.endsWith("png") && !mime.endsWith("gif")) {
                    CompressFormat.JPEG
                } else {
                    CompressFormat.PNG
                }
            } else {
                CompressFormat.JPEG
            }
        } else {
            CompressFormat.PNG
        }
        return format
    }

    fun getMime(file: String?): String {
        return try {
            val fis = FileInputStream(file)
            val bytes = ByteArray(8)
            fis.read(bytes)
            fis.close()
            getMime(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    private fun getMime(bytes: ByteArray): String {
        val jpeg = byteArrayOf(-1, -40, -1, -32)
        val jpeg2 = byteArrayOf(-1, -40, -1, -31)
        return if (!bytesStartWith(bytes, jpeg) && !bytesStartWith(bytes, jpeg2)
        ) {
            val png = byteArrayOf(-119, 80, 78, 71)
            if (bytesStartWith(bytes, png)) {
                "png"
            } else {
                val gif = "GIF".toByteArray()
                if (bytesStartWith(bytes, gif)) {
                    "gif"
                } else {
                    val bmp = "BM".toByteArray()
                    if (bytesStartWith(bytes, bmp)) {
                        "bmp"
                    } else {
                        val tiff = byteArrayOf(73, 73, 42)
                        val tiff2 = byteArrayOf(77, 77, 42)
                        if (!bytesStartWith(bytes, tiff) && !bytesStartWith(
                                bytes,
                                tiff2
                            )
                        ) "" else "tif"
                    }
                }
            }
        } else {
            "jpg"
        }
    }

    private fun bytesStartWith(
        target: ByteArray?,
        prefix: ByteArray?
    ): Boolean {
        return if (target == prefix) {
            true
        } else if (target != null && prefix != null) {
            if (target.size < prefix.size) {
                false
            } else {
                for (i in prefix.indices) {
                    if (target[i] != prefix[i]) {
                        return false
                    }
                }
                true
            }
        } else {
            false
        }
    }

    @JvmStatic
    fun getBitmap(
        path: String,
        maxWidth: Int,
        maxHeight: Int
    ): Bitmap? {
        //先解析图片边框的大小
        var bm: Bitmap? = null
        val file = File(path)
        if (file.exists()) {
            val ops = BitmapFactory.Options()
            ops.inJustDecodeBounds = true
            ops.inSampleSize = 1
            BitmapFactory.decodeFile(path, ops)
            val oHeight = ops.outHeight
            val oWidth = ops.outWidth

            //控制压缩比
            if (oHeight.toFloat() / maxWidth < oWidth.toFloat() / maxHeight) {
                ops.inSampleSize =
                    ceil(oWidth.toFloat() / maxHeight.toDouble()).toInt()
            } else {
                ops.inSampleSize =
                    ceil(oHeight.toFloat() / maxWidth.toDouble()).toInt()
            }
            ops.inJustDecodeBounds = false
            bm = BitmapFactory.decodeFile(path, ops)
        }
        return bm
    }

    /**
     * bitmap转为base64
     */
    @JvmStatic
    @Throws(IOException::class)
    fun bitmapToBase64(bitmap: Bitmap?): String? {
        var result: String? = null
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                baos = ByteArrayOutputStream()
                bitmap.compress(CompressFormat.JPEG, 100, baos)
                baos.flush()
                baos.close()
                val bitmapBytes = baos.toByteArray()
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            throw e
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: IOException) {
                throw e
            }
        }
        return result
    }

    /**
     * base64转为bitmap
     */
    @JvmStatic
    fun base64ToBitmap(base64Data: String?): Bitmap {
        val bytes = Base64.decode(base64Data, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    /**
     * 将view转化为Bitmap并保存在指定的文件中
     *
     * @param fileDir 指定的保存Bitmap的目录
     * @param fileName 指定的保存Bitmap的文件名
     * @param quality Bitmap的质量 [0, 100] 之间
     * @return 指定的保存Bitmap的文件的路径
     */
    @JvmOverloads
    @JvmStatic
    fun viewToBitmap(
        view: View?,
        fileDir: File?,
        fileName: String?,
        quality: Int = 100
    ): String? {
        return if (quality < 0 || quality > 100) {
            null
        } else saveBitmap(
            viewToBitmap(view), fileDir, fileName
        )
    }

    /**
     * 将view转化为Bitmap
     */
    fun viewToBitmap(view: View?): Bitmap? {
        if (view == null) {
            return null
        }
        val height = view.measuredHeight
        val width = view.measuredWidth
        if (0 >= height || 0 >= width) {
            return null
        }
        val bitmap = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.RGB_565
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    /**
     * 保存Bitmap到指定文件
     *
     * @param fileDir 指定的保存Bitmap的目录
     * @param fileName 指定的保存Bitmap的文件名
     * @return 指定的保存Bitmap的文件的路径
     */
    fun saveBitmap(
        bitmap: Bitmap?,
        fileDir: File?,
        fileName: String?
    ): String? {
        if (fileDir == null || fileName.isNullOrEmpty() || bitmap == null) {
            return null
        }
        var fos: FileOutputStream? = null
        try {
            if (!fileDir.exists()) {
                if (!fileDir.mkdirs()) {
                    return null
                }
            }
            val file = File(fileDir, fileName)
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return null
                }
            }
            fos = FileOutputStream(file)
            bitmap.compress(CompressFormat.JPEG, 100, fos)
            fos.flush()
            return file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            bitmap.recycle()
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    private var sGLESTextureLimit = 0 // canvas允许绘制的Bitmap的最大长度，保存在变量中，不用每次读取计算

    /**
     * 获取当前设备，canvas允许绘制的Bitmap的最大长度
     * 注释：不知道为什么，在(设备名称：Galaxy Note3)(型号：SM-N9008S)(Android版本：Android5.0)设备上获取到的值为4096，但是设置为3913的时候才能显示图片，所以每次减少1000，看性能消耗相对于其它值而言会好一点
     */
    @JvmStatic
    fun getGLESTextureLimit(): Int {
        if (sGLESTextureLimit != 0) {
            return sGLESTextureLimit
        }
        sGLESTextureLimit = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getGLESTextureLimitEqualAboveLollipop()
        } else {
            getGLESTextureLimitBelowLollipop()
        }
        sGLESTextureLimit -= 1000
        return sGLESTextureLimit
    }

    private fun getGLESTextureLimitBelowLollipop(): Int {
        val maxSize = IntArray(1)
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0)
        return maxSize[0]
    }// missing in EGL10

    // TROUBLE! No config found.
    private fun getGLESTextureLimitEqualAboveLollipop(): Int {
        val egl = EGLContext.getEGL() as EGL10
        val dpy =
            egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
        val vers = IntArray(2)
        egl.eglInitialize(dpy, vers)
        val configAttr = intArrayOf(
            EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RGB_BUFFER,
            EGL10.EGL_LEVEL, 0,
            EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PBUFFER_BIT,
            EGL10.EGL_NONE
        )
        val configs =
            arrayOfNulls<EGLConfig>(1)
        val numConfig = IntArray(1)
        egl.eglChooseConfig(dpy, configAttr, configs, 1, numConfig)
        if (numConfig[0] == 0) { // TROUBLE! No config found.
        }
        val config = configs[0]
        val surfAttr = intArrayOf(
            EGL10.EGL_WIDTH, 64,
            EGL10.EGL_HEIGHT, 64,
            EGL10.EGL_NONE
        )
        val surf =
            egl.eglCreatePbufferSurface(dpy, config, surfAttr)
        val EGL_CONTEXT_CLIENT_VERSION = 0x3098 // missing in EGL10
        val ctxAttrib = intArrayOf(
            EGL_CONTEXT_CLIENT_VERSION, 1,
            EGL10.EGL_NONE
        )
        val ctx =
            egl.eglCreateContext(dpy, config, EGL10.EGL_NO_CONTEXT, ctxAttrib)
        egl.eglMakeCurrent(dpy, surf, surf, ctx)
        val maxSize = IntArray(1)
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0)
        egl.eglMakeCurrent(
            dpy, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
            EGL10.EGL_NO_CONTEXT
        )
        egl.eglDestroySurface(dpy, surf)
        egl.eglDestroyContext(dpy, ctx)
        egl.eglTerminate(dpy)
        return maxSize[0]
    }
}