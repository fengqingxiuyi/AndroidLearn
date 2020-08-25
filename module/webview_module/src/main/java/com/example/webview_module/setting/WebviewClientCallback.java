package com.example.webview_module.setting;

import android.webkit.WebView;

public interface WebviewClientCallback {

    boolean shouldOverrideUrlLoading(WebView view, String url);

}
