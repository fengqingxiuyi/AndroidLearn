package com.example.learn.ui.webview

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.webkit.*
import com.example.common.base.BaseActivity
import com.example.learn.R
import com.example.ui.toast.ToastUtil
import com.example.log.LogUtil
import kotlinx.android.synthetic.main.activity_webview_simple.*

/**
 * @author fqxyi
 * @date 2020/8/18
 */
class WebViewSimpleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_simple)
        settingWebview()
        testWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun settingWebview() {
        val webSettings = webViewSimple.settings
        // base-step1: 使WebView支持加载JavaScript
        webSettings.javaScriptEnabled = true
    }

    private fun testWebView() {
        // base-step2: load url from assets
        webViewSimple.loadUrl("file:///android_asset/test.html")
        // function1[Java Call JS]-step2:
        // 在 Java 代码中使用 WebView 加载 "javascript:方法名(参数值, ...)" 格式的 url
        // 注意String类型的参数值需要在其外部用单引号包括，其他基本类型和复杂类型无需如此
        callJsBtn.setOnClickListener { // 注意: 单引号必须存在，否则将无法调用javaCallJS方法
            // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                // 返回结果会在第70行通过toast弹出
                webViewSimple.loadUrl("javascript:javaCallJS('Message From Java')")
            } else {
                webViewSimple.evaluateJavascript("javaCallJS('Message From Java')") { value ->
                    // 此处为js返回的错误结果，返回结果为null字符串；正确的结果会在第70行通过toast弹出
                    ToastUtil.toast(value)
                }
            }
            //Demo2: 获取屏幕宽度
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                // 返回结果会在第70行通过toast弹出
                webViewSimple.loadUrl("javascript:alert(window.screen.width)")
            } else {
                webViewSimple.evaluateJavascript("window.screen.width") { value ->
                    // 此处为js返回的正确结果
                    ToastUtil.toast(value)
                }
            }
        }
        // function1[Java Call JS]-step3: 非必要操作
        // Tell the client to display a javascript alert dialog.
        webViewSimple.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(
                view: WebView,
                url: String,
                message: String,
                result: JsResult
            ): Boolean {
                ToastUtil.toast(message) //前端alert函数在移动端的展示
                result.confirm()
                return true
            }
        }

        // function2[JS Call Java]-step2: 将提供给 JS 访问的接口内容所属的 Java 对象注入 WebView 中
        webViewSimple.addJavascriptInterface(this, "jsObj")

        // Other: 通过该方法拦截URL，实现 Native 与 Web 之间的交互动作
        webViewSimple.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
    }

    // function2[JS Call Java]-step1:
    // 在 Java 对象中定义提供给 JS 访问的方法
    // 注意：方法权限必须为public，另外必须要添加注解@JavascriptInterface
    @JavascriptInterface
    fun JSCallJava(message: String?) {
        LogUtil.i("thread", Thread.currentThread().name) // JavaBridge
        ToastUtil.toast(message)
    }
}