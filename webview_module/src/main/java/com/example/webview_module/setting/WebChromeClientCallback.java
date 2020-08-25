package com.example.webview_module.setting;

import android.webkit.WebView;

public interface WebChromeClientCallback {

    void onProgressChanged(WebView view, int newProgress);

    void onReceivedTitle(WebView view, String title);

}
