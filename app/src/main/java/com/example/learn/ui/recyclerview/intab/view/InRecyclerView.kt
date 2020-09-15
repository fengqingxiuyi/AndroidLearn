package com.example.learn.ui.recyclerview.intab.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView

class InRecyclerView: RecyclerView {

    private var tabTop = 0
    private var location = intArrayOf()

    private var listener: ParentInterceptListener? = null

    constructor(mContext: Context) : super(mContext)

    constructor(mContext: Context, mAttributeSet: AttributeSet) : super(mContext, mAttributeSet)

    constructor(mContext: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int): super(mContext, attrs, defStyleAttr)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when(ev.action) {
            MotionEvent.ACTION_DOWN -> {
                //获取view在窗口中的位置
                location = intArrayOf(0, 0)
                getLocationOnScreen(location)
                listener?.intercept(location[1] > tabTop || (location[1] <= tabTop && !canScrollVertically(-1)))
                Log.d("InRecyclerView", "onInterceptTouchEvent ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("InRecyclerView", "onInterceptTouchEvent ACTION_MOVE")
            }
            MotionEvent.ACTION_UP -> {
                Log.d("InRecyclerView", "onInterceptTouchEvent ACTION_UP")
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d("InRecyclerView", "onInterceptTouchEvent ACTION_CANCEL")
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when(ev.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("InRecyclerView", "onTouchEvent ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("InRecyclerView", "onTouchEvent ACTION_MOVE")
            }
            MotionEvent.ACTION_UP -> {
                Log.d("InRecyclerView", "onTouchEvent ACTION_UP")
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d("InRecyclerView", "onTouchEvent ACTION_CANCEL")
            }
        }
        return super.onTouchEvent(ev)
    }

    fun setTabTop(tabTop: Int) {
        this.tabTop = tabTop
    }

    fun setParentInterceptListener(listener: ParentInterceptListener) {
        this.listener = listener
    }

    interface ParentInterceptListener {
        /**
         * @param intercept true 父View拦截 false 父View不拦截
         */
        fun intercept(intercept: Boolean)
    }

}