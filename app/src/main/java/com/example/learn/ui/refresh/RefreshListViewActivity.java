package com.example.learn.ui.refresh;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import com.example.common.base.BaseActivity;
import com.example.learn.R;
import com.example.refresh.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

public class RefreshListViewActivity extends BaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    ListView listView;
    PullToRefreshView pullToRefreshView;

    RefreshListGridAdapter adapter;
    List<String> data = new ArrayList<>();
    List<String> originData = new ArrayList<>();
    int count = 0;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_listview);

        listView = (ListView) findViewById(R.id.listView);
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.pullToRefreshView);

        pullToRefreshView.setOnHeaderRefreshListener(this);
        pullToRefreshView.setOnFooterRefreshListener(this);

        initData();
    }

    public void initData() {
        // init data
        for (int i=0; i<20; i++) {
            data.add(String.valueOf(i));
            count++;
        }
        // backup data
        originData.addAll(data);
        // show data
        adapter = new RefreshListGridAdapter(this, data);
        listView.setAdapter(adapter);
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
