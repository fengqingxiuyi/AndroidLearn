package com.example.utils.view;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

/**
 * 动态实现drawable文件夹下常见的背景制作xml文件，例如：
 <?xml version="1.0" encoding="utf-8"?>
 <shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <corners android:radius="7dp" />
    <solid android:color="@color/C21" />
 </shape>
 */
public class ShapeUtil {

    private GradientDrawable drawable;

    private ShapeUtil() {
        drawable = new GradientDrawable();
    }

    private static ShapeUtil instance = new ShapeUtil();

    public static ShapeUtil getInstance() {
        return instance;
    }

    public void setDrawableBg(View view, int shape, int radius, int strokeWidth, int strokeColor, int bgColor) {
        drawable.setShape(shape);
        if (radius != 0) {
            drawable.setCornerRadius(radius);
        }
        if (strokeWidth != 0 && strokeColor != 0) {
            drawable.setStroke(strokeWidth, strokeColor);
        }
        if (bgColor != 0) {
            drawable.setColor(bgColor);
        }
        view.setBackgroundDrawable(drawable);
    }

}
