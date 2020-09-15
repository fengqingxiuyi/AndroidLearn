package com.example.learn.ui.recyclerview.intab.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import com.example.learn.ui.recyclerview.intab.utils.ViewUtil

class RecyclerInTabViewPager : ViewPager {

    private var mDownX = 0f
    private var mDownY = 0f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when(ev.action) {
            MotionEvent.ACTION_DOWN -> {
                //记录按下位置
                mDownX = ev.x
                mDownY = ev.y
                Log.d("RecyclerInTabViewPager", "onInterceptTouchEvent ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                //计算滑动距离
                val mDistanceX = ev.x - mDownX
                val mDistanceY = ev.y - mDownY
                //判断滑动方向
                val orientation = ViewUtil.getOrientation(mDistanceX, mDistanceY)
                when(orientation) {
                    'l','r' -> {
                        return true
                    }
                }
                Log.d("RecyclerInTabViewPager", "onInterceptTouchEvent ACTION_MOVE")
            }
            MotionEvent.ACTION_UP -> {
                Log.d("RecyclerInTabViewPager", "onInterceptTouchEvent ACTION_UP")
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d("RecyclerInTabViewPager", "onInterceptTouchEvent ACTION_CANCEL")
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when(ev.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("RecyclerInTabViewPager", "onTouchEvent ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("RecyclerInTabViewPager", "onTouchEvent ACTION_MOVE")
            }
            MotionEvent.ACTION_UP -> {
                Log.d("RecyclerInTabViewPager", "onTouchEvent ACTION_UP")
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d("RecyclerInTabViewPager", "onTouchEvent ACTION_CANCEL")
            }
        }
        return super.onTouchEvent(ev)
    }

}
