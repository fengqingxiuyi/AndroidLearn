package com.example.webview_module;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.log.LogUtil;
import com.example.webview_module.binder.WebviewBinderCallback;
import com.example.webview_module.binder.WebviewBinderHelper;
import com.example.webview_module.constants.WebviewConstant;
import com.example.webview_module.cookie.CustomCookieManager;
import com.example.webview_module.router.WebviewRouter;
import com.example.webview_module.setting.WebChromeClientCallback;
import com.example.webview_module.setting.WebviewClientCallback;
import com.example.webview_module.setting.WebviewSettingHelper;
import com.example.webview_module.utils.WebviewUtil;
import com.example.webview_module.widget.BaseWebview;
import com.example.webview_module.widget.titlebar.WebviewTitleBar;
import com.example.webview_module.widget.titlebar.WebviewTitleBarCallback;

/**
 * 子进程中的Webview
 */
public class WebviewActivity extends AppCompatActivity implements WebviewBinderCallback, WebChromeClientCallback, WebviewClientCallback {

    private final int CODE_CALL_JS = 1;
    private WebviewHandler handler;
    //findView
    private WebviewTitleBar bar;
    private BaseWebview webview;
    //
    private String url;
    //
    private WebviewBinderHelper binderHelper;
    private WebviewSettingHelper settingHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            initView();
            initData(savedInstanceState);
        } catch (Throwable thr) {
            LogUtil.e(thr);
            WebviewUtil.killWebviewProcess();
        }
    }

    @Override
    protected void onStart() {
        try {
            super.onStart();
        } catch (Throwable thr) {
            LogUtil.e(thr);
            WebviewUtil.killWebviewProcess();
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
        } catch (Throwable thr) {
            LogUtil.e(thr);
            WebviewUtil.killWebviewProcess();
        }
    }

    private void initView() {
        setContentView(R.layout.webview_activity);
        // 应用启动同步cookie信息(在创建webview之前同步cookie)
        CustomCookieManager.getInstance().synCookies(this);
        findView();
        initEvent();
    }

    private void findView() {
        bar = (WebviewTitleBar) findViewById(R.id.bar);
        webview = (BaseWebview) findViewById(R.id.webview);
    }

    private void initEvent() {
        bar.setCallback(new WebviewTitleBarCallback() {
            @Override
            public void clickBarBack() {
                if (webview != null) {
                    if (webview.canGoBack()) {
                        webview.goBack();
                    } else {
                        finish();
                    }
                }
            }

            @Override
            public void clickBarClose() {
                finish();
            }
        });
    }

    private void initData(@Nullable Bundle savedInstanceState) {
        getIntentData();
        initHelper();
    }

    private void getIntentData() {
        url = getIntent().getStringExtra(WebviewConstant.EXTRA_URL);
    }

    private void initHelper() {
        handler = new WebviewHandler();
        binderHelper = new WebviewBinderHelper(this, this);
        binderHelper.initJsInterface();
        binderHelper.bindMainRemoteService(this);
        settingHelper = new WebviewSettingHelper(this);
        settingHelper.initWebview(webview, binderHelper, this, this);
    }

    @Override
    public void onServiceConnected() {
        if (webview != null && !TextUtils.isEmpty(url)) {
            webview.loadUrl(url);
        }
    }

    @Override
    public void callJs(String params) {
        if (handler != null) {
            Message message = Message.obtain();
            message.what = CODE_CALL_JS;
            message.obj = params;
            handler.sendMessage(message);
        }
    }

    /***                               WebChromeClientCallback                               ***/

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (bar != null) {
            bar.onProgressChanged(view, newProgress);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (bar != null) {
            bar.onReceivedTitle(view, title);
        }
    }

    /***                                WebviewClientCallback                                ***/

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (binderHelper == null) {
            return false;
        }
        if (WebviewRouter.isUrlRouters(url)) {
            return binderHelper.execute(url);
        }
        return false;
    }

    private class WebviewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_CALL_JS:
                    String params = (String) msg.obj;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        webview.evaluateJavascript(params, null);
                    } else {
                        webview.loadUrl(params);
                    }
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webview != null) {
            if (webview.canGoBack()) {
                webview.goBack();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binderHelper != null) {
            binderHelper.destroyConnection();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

}
