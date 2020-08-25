package com.example.webview_module.binder;

public interface WebviewBinderCallback {

    void onServiceConnected();

    void callJs(String params);

}
