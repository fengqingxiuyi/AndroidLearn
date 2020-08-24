package com.example.webview.js;

import android.webkit.JavascriptInterface;

/**
 * 子进程中执行
 */
public final class JsInterface {

    private JsFuncCallback mCallback;

    public void setCallback(JsFuncCallback callback) {
        this.mCallback = callback;
    }

    public interface JsFuncCallback {
        boolean execute(String url);
    }

    /**
     * 测试代码参考：app/src/debug/assets/test.html
     * 加载测试页面：webview.loadUrl("file:////android_asset/test.html")
     *
     * androidJS函数运行在子进程的JavaBridge线程中
     */
    @JavascriptInterface
    public void androidJS(String url) {
        if (mCallback != null) {
            mCallback.execute(url);
        }
    }

}
