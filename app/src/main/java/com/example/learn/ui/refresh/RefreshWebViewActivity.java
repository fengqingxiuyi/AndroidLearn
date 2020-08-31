package com.example.learn.ui.refresh;

import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.common.base.BaseActivity;
import com.example.learn.R;
import com.example.refresh.PullToRefreshView;

public class RefreshWebViewActivity extends BaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener {

    WebView webView;
    PullToRefreshView pullToRefreshView;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_webview);

        webView = (WebView) findViewById(R.id.webView);
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.pullToRefreshView);

        pullToRefreshView.setOnHeaderRefreshListener(this);
        pullToRefreshView.setEnableLoadMore(false);

        webView.loadUrl("http://www.baidu.com/");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.reload();
                Toast.makeText(RefreshWebViewActivity.this, "重新加载页面", Toast.LENGTH_SHORT).show();

                pullToRefreshView.onHeaderRefreshComplete();
            }
        }, 1000);
    }

}
