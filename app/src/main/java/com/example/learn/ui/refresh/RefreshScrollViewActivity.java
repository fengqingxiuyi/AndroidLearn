package com.example.learn.ui.refresh;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learn.R;
import com.example.refresh.PullToRefreshView;

public class RefreshScrollViewActivity extends AppCompatActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    ScrollView scrollView;
    PullToRefreshView pullToRefreshView;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_scollview);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.pullToRefreshView);

        pullToRefreshView.setOnHeaderRefreshListener(this);
        pullToRefreshView.setOnFooterRefreshListener(this);

    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshView.onHeaderRefreshComplete();
            }
        }, 1000);
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshView.onFooterRefreshComplete();
            }
        }, 1000);
    }

}
