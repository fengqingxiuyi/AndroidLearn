package com.example.learn.ui.appbarlayout;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.learn.R;
import com.google.android.material.appbar.AppBarLayout;

public class AppbarLayoutActivity2 extends AppCompatActivity {

    private CoordinatorLayout homeTop;
    private ViewPager homePager;
    private AppBarLayout homeAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar_layout_activity2);

        homeTop = (CoordinatorLayout) findViewById(R.id.homeTop);
        homePager = (ViewPager) findViewById(R.id.homePager);
        homeAppBar = (AppBarLayout) findViewById(R.id.homeAppBar);

        homePager.setAdapter(new AppbarLayoutPagerAdapter(getSupportFragmentManager()));

    }
}
