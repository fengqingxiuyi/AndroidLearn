package com.example.learn.ui.appbarlayout

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class AppbarLayoutViewPager : ViewPager {

    private var pagingEnabled: Boolean = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    fun setPagingEnabled(enabled: Boolean) {
        pagingEnabled = enabled
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (!pagingEnabled) return false
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (!pagingEnabled) return false
        return super.onTouchEvent(ev)
    }
}