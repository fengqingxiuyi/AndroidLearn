package com.example.learn.ui.recyclerview.intab.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.example.learn.ui.recyclerview.intab.utils.ViewUtil

class OutRecyclerView: RecyclerView {

    private var mDownX = 0f
    private var mDownY = 0f

    private var intercept = true

    constructor(mContext: Context) : super(mContext)

    constructor(mContext: Context, mAttributeSet: AttributeSet) : super(mContext, mAttributeSet)

    constructor(mContext: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int): super(mContext, attrs, defStyleAttr)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when(ev.action) {
            MotionEvent.ACTION_DOWN -> {
                //记录按下位置
                mDownX = ev.x
                mDownY = ev.y
                Log.d("OutRecyclerView", "onInterceptTouchEvent ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                //计算滑动距离
                val mDistanceX = ev.x - mDownX
                val mDistanceY = ev.y - mDownY
                //判断滑动方向
                val orientation = ViewUtil.getOrientation(mDistanceX, mDistanceY)
                when (orientation) {
                    't' -> {
                        //false 不能向上滑动了 true 可以继续向上滑动 会触发多次
                        return canScrollVertically(1)
                    }
                    'b' -> {
                        return intercept
                    }
                    'l','r' -> {
                        return false
                    }
                }
                Log.d("OutRecyclerView", "onInterceptTouchEvent ACTION_MOVE")
            }
            MotionEvent.ACTION_UP -> {
                Log.d("OutRecyclerView", "onInterceptTouchEvent ACTION_UP")
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d("OutRecyclerView", "onInterceptTouchEvent ACTION_CANCEL")
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when(ev.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("OutRecyclerView", "onTouchEvent ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("OutRecyclerView", "onTouchEvent ACTION_MOVE")
            }
            MotionEvent.ACTION_UP -> {
                Log.d("OutRecyclerView", "onTouchEvent ACTION_UP")
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d("OutRecyclerView", "onTouchEvent ACTION_CANCEL")
            }
        }
        return super.onTouchEvent(ev)
    }

    fun setIntercept(intercept: Boolean) {
        this.intercept = intercept
    }

}