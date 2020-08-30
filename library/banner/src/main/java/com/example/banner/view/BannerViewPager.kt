package com.example.banner.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import com.example.banner.adapter.BannerPageAdapter
import com.example.banner.listener.BannerItemClickListener
import kotlin.math.abs

class BannerViewPager<T> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ViewPager(context, attrs) {

    private var onPageChangeListener = object : OnPageChangeListener {
        private var previousPosition = -1f

        override fun onPageSelected(position: Int) {
            val realPosition = adapter.getRealPosition(position)
            if (previousPosition != realPosition.toFloat()) {
                previousPosition = realPosition.toFloat()
                listener?.onPageSelected(realPosition)
            }
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            if (position != adapter.getRealCount() - 1) {
                listener?.onPageScrolled(position, positionOffset, positionOffsetPixels)
            } else {
                if (positionOffset > .5) {
                    listener?.onPageScrolled(0, 0f, 0)
                } else {
                    listener?.onPageScrolled(position, 0f, 0)
                }
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            listener?.onPageScrollStateChanged(state)
        }
    }

    private lateinit var adapter: BannerPageAdapter<T>
    private var listener: OnPageChangeListener? = null

    var isCanScroll = true // 能否滑动视图
    private var canLoop = true // 是否支持无限循环

    private var bannerItemClickListener: BannerItemClickListener? = null

    private var oldX = 0f

    companion object {
        private const val sens = 5f
    }

    init {
        addOnPageChangeListener(onPageChangeListener)
    }

    fun setPageChangeListener(listener: OnPageChangeListener?) {
        this.listener = listener
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (isCanScroll) {
            try {
                return super.onInterceptTouchEvent(ev)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (isCanScroll) {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> oldX = ev.x
                MotionEvent.ACTION_UP -> {
                    val newX = ev.x
                    if (abs(oldX - newX) < sens) {
                        bannerItemClickListener?.onItemClick(getRealItem())
                    }
                    oldX = 0f
                }
            }
            try {
                return super.onTouchEvent(ev)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    fun setBannerItemClickListener(bannerItemClickListener: BannerItemClickListener?) {
        this.bannerItemClickListener = bannerItemClickListener
    }

    fun setAdapter(adapter: BannerPageAdapter<T>, canLoop: Boolean) {
        this.adapter = adapter
        this.adapter.setCanLoop(canLoop)
        this.adapter.setViewPager(this)
        super.setAdapter(this.adapter)

        setCurrentItem(getFirstItem(), false)
    }

    override fun getAdapter(): BannerPageAdapter<T>? {
        return adapter
    }

    fun getFirstItem(): Int {
        return if (canLoop) adapter.getRealCount() else 0
    }

    fun getLastItem(): Int {
        return adapter.getRealCount() - 1
    }

    fun getRealItem(): Int {
        return adapter.getRealPosition(super.getCurrentItem())
    }

    fun setCanLoop(canLoop: Boolean) {
        this.canLoop = canLoop
        if (!canLoop) {
            setCurrentItem(getRealItem(), false)
        }
        adapter.setCanLoop(canLoop)
        adapter.notifyDataSetChanged()
    }

    fun isCanLoop(): Boolean {
        return canLoop
    }

    fun destroy() {
        removeOnPageChangeListener(onPageChangeListener)
    }
}