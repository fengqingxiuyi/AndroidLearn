package com.example.webview.setting;

import android.webkit.WebView;

public interface WebviewClientCallback {

    boolean shouldOverrideUrlLoading(WebView view, String url);

}
