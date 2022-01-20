package com.example.webview_module.setting;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.common.UserAgent;
import com.example.log.LogUtil;
import com.example.webview_module.binder.WebviewBinderHelper;
import com.example.webview_module.widget.BaseWebview;

import java.io.File;

public class WebviewSettingHelper {

    private Activity activity;
    private Context context;

    public WebviewSettingHelper(Activity activity) {
        this.activity = activity;
        context = activity.getApplicationContext();
    }

    public void initWebview(BaseWebview webView) {
        initWebviewSetting(webView);
    }

    public void initWebview(BaseWebview webView, WebviewBinderHelper binderHelper,
                            WebChromeClientCallback webChromeClientCallback,
                            WebviewClientCallback webviewClientCallback) {
        initWebviewSetting(webView);
        if (binderHelper != null) {
            webView.addJavascriptInterface(binderHelper.getJsInterface(), "demo");
        }
        if (webChromeClientCallback != null) {
            webView.setWebChromeClient(new CustomWebChromeClient(webChromeClientCallback));
        }
        if (webviewClientCallback != null) {
            webView.setWebViewClient(new CustomWebviewClient(webviewClientCallback));
        }
    }

    private void initWebviewSetting(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        /**
         * 允许js代码
         * 在Android 4.3版本调用WebSettings.setJavaScriptEnabled()方法时会调用一下reload方法，
         * 同时会回调多次WebChromeClient.onJsPrompt()。
         * 如果有业务逻辑依赖于这两个方法，就需要注意判断回调多次是否会带来影响了。
         */
        webSettings.setJavaScriptEnabled(true);
        //允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(true);
        //禁用放缩
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);
        //禁用文字缩放
        webSettings.setTextZoom(100);
        //10M缓存，api 18后，系统自动管理。
        webSettings.setAppCacheMaxSize(10 * 1024 * 1024);
        //允许WebView使用File协议
        webSettings.setAllowFileAccess(true);
        //不保存密码
        webSettings.setSavePassword(false);
        //设置UA
        webSettings.setUserAgentString(UserAgent.getUserAgent(context));
        //移除部分系统JavaScript接口
        removeJavascriptInterfaces(webView);
        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //
        if (context != null) {
            File appCacheFile = context.getDir("appCache", Context.MODE_PRIVATE);
            if (appCacheFile != null) {
                //允许缓存，设置缓存位置
                webSettings.setAppCacheEnabled(true);
                webSettings.setAppCachePath(appCacheFile.getPath());
            }
            File dbCacheFile = context.getDir("dbCache", Context.MODE_PRIVATE);
            if (dbCacheFile != null) {
                //开启 database storage API 功能
                webSettings.setDatabaseEnabled(true);
                //设置数据库缓存路径
                webSettings.setDatabasePath(dbCacheFile.getPath());
            }
        }
        //
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
    }

    /**
     * 如果启用了JavaScript，务必做好安全措施，防止远程执行漏洞
     * 参考文章：https://blog.csdn.net/self_study/article/details/55046348
     */
    private void removeJavascriptInterfaces(WebView webView) {
        try {
            if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT < 17) {
                webView.removeJavascriptInterface("searchBoxJavaBridge_");
                webView.removeJavascriptInterface("accessibility");
                webView.removeJavascriptInterface("accessibilityTraversal");
            }
        } catch (Throwable e) {
            LogUtil.e(e);
        }
    }

}
