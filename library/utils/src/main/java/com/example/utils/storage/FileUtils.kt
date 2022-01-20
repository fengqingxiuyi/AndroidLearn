package com.example.utils.storage

import android.content.ContentResolver
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.example.utils.ext.toFile
import com.example.utils.encrypt.md5
import com.example.utils.image.ImageUtils
import com.example.utils.other.Utils
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLConnection
import java.net.URLEncoder

object FileUtils {

  fun guessMimeType(path: String): String {
    val fileNameMap = URLConnection.getFileNameMap()
    var contentTypeFor: String? = null
    try {
      contentTypeFor =
        fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"))
    } catch (e: UnsupportedEncodingException) {
      e.printStackTrace()
    }
    if (contentTypeFor == null) {
      contentTypeFor = "application/octet-stream"
    }
    return contentTypeFor
  }

  fun getLength(uri: Uri): Long =
    when (uri.scheme) {
      ContentResolver.SCHEME_FILE -> File(uri.path!!).length()
      ContentResolver.SCHEME_CONTENT -> {
        val cr = Utils.context.contentResolver
        cr.query(uri, null, null, null, null).use {
          if (it != null && it.moveToFirst()) {
            File(it.getString(it.getColumnIndex("_data"))).length()
          } else let {
            try {
              cr.openFileDescriptor(uri, "r")?.statSize ?: 0
            } catch (e: Exception) {
              0L
            }
          }
        }
      }
      else -> 0L
    }

  fun getFileExtension(any: Any?): String {
    return when (any) {
      is Uri -> {
        getFileExtension(any)
      }
      is File -> {
        getFileExtension(any)
      }
      is String -> {
        getFileExtension(any)

      }
      else -> ""
    }
  }

  fun getFileExtension(mimeType: String): String {
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: ""
  }

  fun getFileExtension(uri: Uri): String {
    return ImageUtils.getImageExtension(uri) ?: let {
      val fileName = getFileName(uri)
      val split = fileName.split(".")
      if (split.size > 1) {
        split[split.size - 1]
      } else {
        ""
      }
    }
  }

  fun getFileExtension(file: File): String {
    return ImageUtils.getImageExtension(file)
      ?: MimeTypeMap.getFileExtensionFromUrl(file.absolutePath) ?: ""
  }

  fun getFileMimeType(uri: Uri): String {
    return Utils.context.contentResolver.getType(uri) ?: ""
  }

  fun getFileMimeType(file: File): String {
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(getFileExtension(file)) ?: ""

  }

  fun isFileExists(uri: Uri): Boolean {
    val file = when {
      uri.scheme?.equals("file") == true -> {
        uri.toFile()
      }
      uri.scheme?.equals("content") == true -> {
        getFilePath(uri).toFile()
      }
      else -> null
    }
    if (file != null) {
      return file.exists()
    }
    return uri.path?.run { File(this).exists() } ?: false
  }

  fun getFileName(uri: Uri): String {
    var result: String? = null
    if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
      val cursor = Utils.context.contentResolver.query(uri, null, null, null, null)
      try {
        if (cursor != null && cursor.moveToFirst()) {
          result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
      } catch (e: Exception) {
        e.printStackTrace()
      } finally {
        cursor?.close()
      }
    }
    if (result == null) {
      result = uri.path
      val cut = result!!.lastIndexOf(File.separator)
      if (cut != -1) {
        result = result.substring(cut + 1)
      }
    }
    return result
  }

  fun getFilePath(uri: Uri): String {
    var result: String? = null
    if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
      val cursor = Utils.context.contentResolver.query(uri, null, null, null, null)
      try {
        if (cursor != null && cursor.moveToFirst()) {
          val columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
          result = cursor.getString(columnIndex)
        }
      } catch (e: Exception) {
        e.printStackTrace()
      } finally {
        cursor?.close()
      }
    }
    if (result == null) {
      result = uri.path
      val cut = result!!.lastIndexOf(File.separator)
      if (cut != -1) {
        result = result.substring(cut + 1)
      }
    }
    return result
  }

  fun copyFile(uri: Uri): File? {
    Utils.context.contentResolver.openInputStream(uri)?.use { input ->
      val dir = File(Utils.context.cacheDir, "copy")
      if (!dir.exists()) dir.mkdirs()
      val file = File(dir, "${uri.toString().md5()}.${getFileExtension(uri)}")
      if (file.exists()) return file
      file.outputStream().use { output ->
        input.copyTo(output)
      }
      return file
    }
    return null
  }

  fun getFileInfo(
    file: File,
    result: (width: Int, height: Int, duration: Long, size: Long, ext: String) -> Unit
  ) {
    try {
      val imageSize = ImageUtils.getImageSize(file)
      val ext = getFileExtension(file)
      val size = file.length()
      if (imageSize.first() <= 0 || imageSize.last() <= 0) {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(file.absolutePath)
        val videoWidth =
          retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            ?.toIntOrNull() ?: 0
        val videoHeight =
          retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            ?.toIntOrNull() ?: 0
        val videoRotation =
          retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)
            ?.toIntOrNull() ?: 0
        val duration =
          retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            ?.toLongOrNull() ?: 0
        retriever.release()
        result.invoke(
          if (videoRotation == 0 || videoRotation == 180) videoWidth else videoHeight,
          if (videoRotation == 0 || videoRotation == 180) videoHeight else videoWidth,
          duration,
          size,
          ext
        )
      } else {
        result.invoke(imageSize.first(), imageSize.last(), 0L, size, ext)
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  fun getAudioDuration(file: File): Int {
    val metaRetriever = MediaMetadataRetriever()
    metaRetriever.setDataSource(Utils.context, file.toUri())
    // convert duration to minute:seconds
    val duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
    // close object
    metaRetriever.release()
    return duration?.toIntOrNull() ?: 0
  }

  fun isExits(path: String?): Boolean {
    if (path.isNullOrEmpty()) return false
    return path.toFile().exists()
  }
}
