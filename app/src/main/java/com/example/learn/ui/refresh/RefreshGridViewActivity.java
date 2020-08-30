package com.example.learn.ui.refresh;

import android.os.Bundle;
import android.os.Handler;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learn.R;
import com.example.refresh.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

public class RefreshGridViewActivity extends AppCompatActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    GridView gridView;
    PullToRefreshView pullToRefreshView;

    RefreshListGridAdapter adapter;
    List<String> data = new ArrayList<>();
    List<String> originData = new ArrayList<>();
    int count = 0;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_gridview);

        gridView = (GridView) findViewById(R.id.gridView);
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.pullToRefreshView);

        pullToRefreshView.setOnHeaderRefreshListener(this);
        pullToRefreshView.setOnFooterRefreshListener(this);

        initData();
    }

    public void initData() {
        // init data
        for (int i=0; i<10; i++) {
            data.add(String.valueOf(i));
            count++;
        }
        // backup data
        originData.addAll(data);
        // show data
        adapter = new RefreshListGridAdapter(this, data);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setData(originData);
                pullToRefreshView.onHeaderRefreshComplete();
            }
        }, 1000);
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                data.add(String.valueOf(count++));
                adapter.setData(data);
                pullToRefreshView.onFooterRefreshComplete();
            }
        }, 1000);
    }

}
