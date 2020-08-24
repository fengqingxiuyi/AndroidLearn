package com.example.webview;

import com.example.webview.IWebviewBinderCallback;

/**
 * 主进程
 */
interface IWebviewBinder {

    /**
     * 处理JS调用Java的方法
     */
    void handleJsFunc(String url, IWebviewBinderCallback callback);

}