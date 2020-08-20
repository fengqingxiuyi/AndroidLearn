package com.example.utils;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author fqxyi
 * @date 2020/8/20
 * 复制粘贴文本的三种类型
 */
public class ClipboardUtil {
    /**
     * 复制 类型一: text
     */
    public static void copy(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData textCd = ClipData.newPlainText("text", text);
        clipboard.setPrimaryClip(textCd);
    }

    /**
     * 粘贴 类型一: text
     */
    public static CharSequence pasteText(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData cdText = clipboard.getPrimaryClip();
            // 此处是 text
            ClipData.Item item = cdText.getItemAt(0);
            return item.getText();
        }
        return null;
    }

    /**
     * 复制 类型二: Uri
     */
    public static void copy(Context context, Uri uri) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipUri = ClipData.newUri(context.getContentResolver(), "Uri", uri);
        clipboard.setPrimaryClip(clipUri);
    }

    /**
     * 粘贴 类型二: Uri
     */
    public static Uri pasteUri(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_URILIST)) {
            ClipData cdUri = clipboard.getPrimaryClip();
            // 此处是 Uri
            ClipData.Item item = cdUri.getItemAt(0);
            return item.getUri();
        }
        return null;
    }

    /**
     * 复制 类型三: Intent
     */
    public static void copy(Context context, Intent intent) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipIntent = ClipData.newIntent("Intent", intent);
        clipboard.setPrimaryClip(clipIntent);
    }

    /**
     * 粘贴 类型三: Intent
     */
    public static Intent pasteIntent(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_INTENT)) {
            ClipData cdIntent = clipboard.getPrimaryClip();
            // 此处是 Intent
            ClipData.Item item = cdIntent.getItemAt(0);
            return item.getIntent();
        }
        return null;
    }
}
