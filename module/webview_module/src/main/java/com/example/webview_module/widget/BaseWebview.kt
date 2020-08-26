package com.example.webview_module.widget

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

/**
 * WebView基础类，处理一些基础的公有操作
 */
class BaseWebview @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr)