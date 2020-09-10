package com.example.learn.uiutils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.learn.R;

/**
 * DrawableUtil测试类
 */
public class TestDrawableActivity extends Activity {

    private View testDrawableView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_drawable);

        testDrawableView = (View) findViewById(R.id.test_drawable_view);
    }

    public void test(View view) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(new float[]{
                0,0,
                0,0,
                0,0,
                0,0
        });
        drawable.setAlpha(255);
        drawable.setColor(Color.BLACK);
        drawable.setCornerRadius(50f);
        drawable.setStroke(1, Color.GREEN);
        drawable.setDither(true); //防抖动
        drawable.setGradientCenter(0,0);
        drawable.setGradientRadius(50f);
        drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        drawable.setSize(100, 100);
        testDrawableView.setBackgroundDrawable(drawable);
    }

}
