package com.example.webview_module.setting

import android.webkit.WebView

interface WebviewClientCallback {
    fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean
}