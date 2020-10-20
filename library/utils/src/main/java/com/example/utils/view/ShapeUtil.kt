package com.example.utils.view

import android.graphics.drawable.GradientDrawable
import android.view.View

/**
 * 动态实现drawable文件夹下常见的背景制作xml文件，例如：
 *
 * <shape xmlns:android="http://schemas.android.com/apk/res/android" android:shape="oval">
 * <corners android:radius="7dp"></corners>
 * <solid android:color="@color/C21"></solid>
</shape> *
 */
class ShapeUtil private constructor() {

    companion object {
        val instance = ShapeUtil()
    }

    private val drawable = GradientDrawable()

    fun setDrawableBg(
        view: View,
        shape: Int,
        radius: Int,
        strokeWidth: Int,
        strokeColor: Int,
        bgColor: Int
    ) {
        drawable.shape = shape
        if (radius != 0) {
            drawable.cornerRadius = radius.toFloat()
        }
        if (strokeWidth != 0 && strokeColor != 0) {
            drawable.setStroke(strokeWidth, strokeColor)
        }
        if (bgColor != 0) {
            drawable.setColor(bgColor)
        }
        view.setBackgroundDrawable(drawable)
    }

}