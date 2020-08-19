package com.example.learn.webview

import android.os.Build
import android.os.Bundle
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import com.example.common.BaseActivity
import com.example.learn.R
import kotlinx.android.synthetic.main.activity_webview.*

/**
 * @author fqxyi
 * @date 2020/8/18
 */
class WebViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        testWebView()
    }

    private fun testWebView() {
        val webSettings = webView.settings

        // 设置与Js交互的权限
        webSettings.javaScriptEnabled = true

        callJsBtn.setOnClickListener {
            // 通过Handler发送消息
            webView.post {
                // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    webView.loadUrl("javascript:alert(window.screen.width)")
                } else {
                    webView.evaluateJavascript("window.screen.width") { value ->
                        // 此处为js返回的结果
                        Toast.makeText(this@WebViewActivity, value, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        // 设置响应js的Alert()函数
        webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(
                view: WebView,
                url: String,
                message: String,
                result: JsResult
            ): Boolean {
                Toast.makeText(this@WebViewActivity, message, Toast.LENGTH_SHORT).show()
                result.confirm()
                return true
            }
        }
    }
}