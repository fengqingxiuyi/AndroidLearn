package com.example.utils.device

import android.content.*
import android.net.Uri

/**
 * @author fqxyi
 * @date 2020/8/20
 * 复制粘贴文本的三种类型
 */
object ClipboardUtil {

    /**
     * 复制 类型一: text
     */
    fun copy(context: Context, text: String?) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val textCd = ClipData.newPlainText("text", text)
        clipboard.setPrimaryClip(textCd)
    }

    /**
     * 粘贴 类型一: text
     */
    fun pasteText(context: Context): CharSequence? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == true) {
            return clipboard.primaryClip?.getItemAt(0)?.text
        }
        return null
    }

    /**
     * 复制 类型二: Uri
     */
    fun copy(context: Context, uri: Uri?) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipUri = ClipData.newUri(context.contentResolver, "Uri", uri)
        clipboard.setPrimaryClip(clipUri)
    }

    /**
     * 粘贴 类型二: Uri
     */
    fun pasteUri(context: Context): Uri? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_URILIST) == true) {
            return clipboard.primaryClip?.getItemAt(0)?.uri
        }
        return null
    }

    /**
     * 复制 类型三: Intent
     */
    fun copy(context: Context, intent: Intent?) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipIntent = ClipData.newIntent("Intent", intent)
        clipboard.setPrimaryClip(clipIntent)
    }

    /**
     * 粘贴 类型三: Intent
     */
    fun pasteIntent(context: Context): Intent? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_INTENT) == true) {
            return clipboard.primaryClip?.getItemAt(0)?.intent
        }
        return null
    }
}