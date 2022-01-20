package com.example.utils.image

import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import com.example.utils.other.Utils
import java.io.File

object ImageUtils {

    fun getImageSize(file: File): IntArray {
        val exifInterface = ExifInterface(file)
        val orientation = exifInterface.rotationDegrees
        var x = exifInterface.getAttributeInt(ExifInterface.TAG_PIXEL_X_DIMENSION, 0)
        var y = exifInterface.getAttributeInt(ExifInterface.TAG_PIXEL_Y_DIMENSION, 0)
        if (x == 0 || y == 0) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(file.absolutePath, options)
            options.inJustDecodeBounds = false
            x = options.outWidth
            y = options.outHeight
        }
        return if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            intArrayOf(y, x)
        } else {
            intArrayOf(x, y)
        }
    }

    fun getImageSize(uri: Uri): IntArray {
        Utils.context.contentResolver.openInputStream(uri)?.use {
            val exifInterface = ExifInterface(it)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            val x = exifInterface.getAttributeInt(ExifInterface.TAG_PIXEL_X_DIMENSION, 0)
            val y = exifInterface.getAttributeInt(ExifInterface.TAG_PIXEL_Y_DIMENSION, 0)
            return if (x > 0 && y > 0) {
                if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    intArrayOf(y, x)
                } else intArrayOf(x, y)
            } else {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeStream(it, null, options)
                options.inJustDecodeBounds = false
                intArrayOf(options.outWidth, options.outHeight)
            }
        }
        return intArrayOf(0, 0)
    }

    /**
     * 是否是图片
     */
    fun isImage(file: File): Boolean {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            file.inputStream().use {
                BitmapFactory.decodeStream(it, null, options)
                options.outMimeType.startsWith("image/")
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 是否是图片
     */
    fun isImage(uri: Uri): Boolean {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            Utils.context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it, null, options)
                options.outMimeType.startsWith("image/")
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

    fun getImageExtension(uri: Uri): String? {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            Utils.context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(
                    it,
                    null,
                    options
                )
                options.outMimeType.replace("image/", "")
            }
        } catch (e: Exception) {
            null
        }
    }

    fun getImageExtension(file: File): String? {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            file.inputStream().use {
                BitmapFactory.decodeStream(it, null, options)
                options.outMimeType.replace("image/", "")
            }
        } catch (e: Exception) {
            null
        }
    }
}