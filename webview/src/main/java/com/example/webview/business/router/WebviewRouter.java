package com.example.webview.business.router;

import android.content.Context;
import android.text.TextUtils;

import com.example.webview.IWebviewBinderCallback;
import com.example.webview.js.JsBridge;

/**
 * webview 业务 路由中转类
 */
public class WebviewRouter {

    public static void h5ToNative(Context context, String url, IWebviewBinderCallback callback) {
        //合法性判断
        if (context == null || TextUtils.isEmpty(url)) return;
        //走路由交互类JsInterfaceImpl.java
        WebviewRouterParams params = new WebviewRouterParams(url);
        JsBridge.get().callJava(params.getTarget(), context, params.getParams(), callback);
    }

}
