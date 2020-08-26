package com.example.webview_module.binder

interface WebviewBinderCallback {
    fun onServiceConnected()
    fun callJs(params: String?)
}