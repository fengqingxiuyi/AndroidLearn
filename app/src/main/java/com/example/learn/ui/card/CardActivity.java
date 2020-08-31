package com.example.learn.ui.card;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.common.base.BaseActivity;
import com.example.learn.R;
import com.example.learn.ui.card.adapter.TubatuAdapter;
import com.example.ui.card.ClipViewPager;
import com.example.utils.device.DensityUtil;
import com.example.utils.device.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

public class CardActivity extends BaseActivity {

    private ClipViewPager mViewPager;
    private TubatuAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        initView();
        initData();
    }

    private void initView() {
        mViewPager = (ClipViewPager) findViewById(R.id.viewpager);

        findViewById(R.id.page_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });

        initViewPager();
    }

    private void initViewPager() {
        float factor = DeviceUtil.getScreenWidth(this) / 375f;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mViewPager.getLayoutParams();
        params.width = (int) (factor * 320 + DensityUtil.dp2px(this, 30));
        params.height = (int) (factor * 130);
        mViewPager.setLayoutParams(params);

        mPagerAdapter = new TubatuAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void initData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i+"");
        }
        //设置OffscreenPageLimit
        mViewPager.setOffscreenPageLimit(Math.min(list.size(), 5));
        mPagerAdapter.addAll(list);
    }
}