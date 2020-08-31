# WebViewSimpleActivity

Java与JS的交互总结

# Simple类

- `src/main/assets/test.html`
- `com.example.learn.ui.webview.WebViewSimpleActivity.kt`

# Base Operate

```kotlin
val webSettings = webViewSimple.settings
// base-step1: 使WebView支持加载JavaScript
webSettings.javaScriptEnabled = true
// base-step2: load url from assets
webViewSimple.loadUrl("file:///android_asset/test.html")
```

# Java Call JS

```javascript
<!-- step1: 在Web页面中定义提供给 Java 访问的 JS 方法 -->
<script type="text/javascript">
    function javaCallJS(message) {
        alert(message);
    }
</script>
```

```kotlin
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
```

# JS Call Java

```kotlin
// function2[JS Call Java]-step1:
// 在 Java 对象中定义提供给 JS 访问的方法
// 注意：方法权限必须为public，另外必须要添加注解@JavascriptInterface
@JavascriptInterface
fun JSCallJava(message: String?) {
    LogUtil.i("thread", Thread.currentThread().name) // JavaBridge
    ToastUtil.toast(message)
}
// function2[JS Call Java]-step2: 将提供给 JS 访问的接口内容所属的 Java 对象注入 WebView 中
webViewSimple.addJavascriptInterface(this, "jsObj")
```

```html
<!-- function2[JS Call Java]-step3: JS 按照指定的接口名访问 Java 代码 -->
<button type="button" onClick="javascript:jsObj.JSCallJava('Message From JS By javascript')" >JS Call Java By javascript</button>
<button type="button" onClick="window.jsObj.JSCallJava('Message From JS By window')" >JS Call Java By window</button>
```

# FAQ

1、使用 loadUrl() 方法实现 Java 调用 JS 功能时，必须放置在主线程中，否则会发生崩溃异常，如果在子线程中遇到调用 JS 的功能，需要将其转换到主线程中。

2、当 JS 调用 Java 时，如果需要 Java 继续回调 JS，千万别在 JavascriptInterface 方法体中直接执行 loadUrl() 方法，而是像前面一样进行线程切换操作。

3、代码混淆时，记得保持 JavascriptInterface 内容，在 proguard 文件中添加如下类似规则：

```
# 使用注解需要添加
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

-keep public class android.webkit.**
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
```

4、其他的 Java 与 JS 交互方式：通过 shouldOverrideUrlLoading 拦截 URL 实现。

```kotlin
// Other: 通过该方法拦截URL，实现 Native 与 Web 之间的交互动作
webViewSimple.webViewClient = object : WebViewClient() {
    override fun shouldOverrideUrlLoading(
        view: WebView,
        request: WebResourceRequest
    ): Boolean {
        return super.shouldOverrideUrlLoading(view, request)
    }
}
```
