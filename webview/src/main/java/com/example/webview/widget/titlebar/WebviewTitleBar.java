package com.example.webview.widget.titlebar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.webview.R;

public class WebviewTitleBar extends RelativeLayout {

    // findView
    private LinearLayout barLeft;
    private TextView barBack;
    private TextView barClose;
    private TextView barTitle;
    private LinearLayout barRight;
    private View barLine;
    private ProgressBar barProgress;
    //
    private WebviewTitleBarCallback callback;

    public WebviewTitleBar(Context context) {
        super(context);
        init(context);
    }

    public WebviewTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WebviewTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.webview_title_bar, this);
        setBackgroundColor(Color.WHITE);
        findView();
        initEvent();
    }

    private void findView() {
        barLeft = (LinearLayout) findViewById(R.id.bar_left);
        barBack = (TextView) findViewById(R.id.bar_back);
        barClose = (TextView) findViewById(R.id.bar_close);
        barTitle = (TextView) findViewById(R.id.bar_title);
        barRight = (LinearLayout) findViewById(R.id.bar_right);
        barLine = (View) findViewById(R.id.bar_line);
        barProgress = (ProgressBar) findViewById(R.id.bar_progress);
    }

    private void initEvent() {
        barBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.clickBarBack();
                }
            }
        });
        barClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.clickBarClose();
                }
            }
        });
    }

    /**
     * 进度条
     */
    public void onProgressChanged(WebView view, int newProgress) {
        if (barProgress == null) {
            return;
        }
        if (newProgress > 0 && newProgress < 100) {
            if (barProgress.getVisibility() == View.GONE) {
                barProgress.setVisibility(View.VISIBLE);
            }
            barProgress.setProgress(newProgress);
        } else {
            if (barProgress.getVisibility() == View.VISIBLE) {
                barProgress.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 标题
     */
    public void onReceivedTitle(WebView view, String title) {
        if (barTitle == null) {
            return;
        }
        barTitle.setText(title);
        //
        int maxWidth = 0;
        if (barLeft != null && barRight != null) {
            if (barLeft.getWidth() > barRight.getWidth()) {
                maxWidth = barLeft.getWidth();
            } else {
                maxWidth = barRight.getWidth();
            }
        }
        LayoutParams params = (LayoutParams) barTitle.getLayoutParams();
        params.leftMargin = maxWidth;
        params.rightMargin = maxWidth;
        barTitle.setLayoutParams(params);
    }

    public void setCallback(WebviewTitleBarCallback callback) {
        this.callback = callback;
    }

}
