package com.example.webview;

/**
 * 子进程
 */
interface IWebviewBinderCallback {

    /**
     * 处理JS调用Java的方法之后，将数据回调给JS
     */
    void callJs(String params);

}