package com.example.webview_module.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * WebView基础类，处理一些基础的公有操作
 */
public class BaseWebview extends WebView {

    public BaseWebview(Context context) {
        super(context);
    }

    public BaseWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
