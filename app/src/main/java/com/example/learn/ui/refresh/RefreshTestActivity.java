package com.example.learn.ui.refresh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learn.R;

public class RefreshTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_refresh_test);
    }

    public void ListView(View view) {
        startActivity(new Intent(this, RefreshListViewActivity.class));
    }

    public void GridView(View view) {
        startActivity(new Intent(this, RefreshGridViewActivity.class));
    }

    public void ScrollView(View view) {
        startActivity(new Intent(this, RefreshScrollViewActivity.class));
    }

    public void WebView(View view) {
        startActivity(new Intent(this, RefreshWebViewActivity.class));
    }

}
