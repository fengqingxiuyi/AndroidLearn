package com.example.banner.view

import android.content.Context
import android.widget.Scroller

class ViewPagerScroller(context: Context?) : Scroller(context) {

    // 滑动速度,值越大滑动越慢，滑动太快会使3d效果不明显
    var scrollDuration = 800
    var isZero = false

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, if (isZero) 0 else scrollDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, if (isZero) 0 else scrollDuration)
    }
}