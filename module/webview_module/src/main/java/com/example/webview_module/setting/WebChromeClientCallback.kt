package com.example.webview_module.setting

import android.webkit.WebView

interface WebChromeClientCallback {
    fun onProgressChanged(view: WebView?, newProgress: Int)
    fun onReceivedTitle(view: WebView?, title: String?)
}