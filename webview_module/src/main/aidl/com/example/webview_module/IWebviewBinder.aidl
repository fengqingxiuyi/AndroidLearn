package com.example.webview_module;

import com.example.webview_module.IWebviewBinderCallback;

/**
 * 主进程
 */
interface IWebviewBinder {

    /**
     * 处理JS调用Java的方法
     */
    void handleJsFunc(String url, IWebviewBinderCallback callback);

}