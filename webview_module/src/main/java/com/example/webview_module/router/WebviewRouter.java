package com.example.webview_module.router;

import android.content.Context;
import android.text.TextUtils;

import com.example.webview_module.IWebviewBinderCallback;
import com.example.webview_module.js.JsBridge;

/**
 * webview 业务 路由中转类
 */
public class WebviewRouter {

    public static final String URL_ROUTERS_DEBUG = "m.mamahao.com/routes";
    public static final String URL_ROUTERS_RELEASE = "m.mamhao.com/routes";

    public static void h5ToNative(Context context, String url, IWebviewBinderCallback callback) {
        //合法性判断
        if (context == null || TextUtils.isEmpty(url)) return;
        //走路由交互类JsInterfaceImpl.java
        WebviewRouterParams params = new WebviewRouterParams(url);
        JsBridge.get().callJava(params.getTarget(), context, params.getParams(), callback);
    }

    /**
     * 判断URL是不是路由地址
     */
    public static boolean isUrlRouters(String url) {
        if (TextUtils.isEmpty(url)) return false;
        return url.contains(URL_ROUTERS_DEBUG) || url.contains(URL_ROUTERS_RELEASE);
    }

}
