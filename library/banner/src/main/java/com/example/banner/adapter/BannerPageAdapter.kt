package com.example.banner.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.banner.holder.Holder
import com.example.banner.view.BannerViewPager

class BannerPageAdapter<T>(private val holderCreator: Holder<T>, private var mData: List<T>?) : PagerAdapter() {

    private var viewPager: BannerViewPager<T>? = null

    private var canLoop = true // 是否支持无限循环

    private var childCount = 0

    fun setCanLoop(canLoop: Boolean) {
        this.canLoop = canLoop
    }

    override fun getCount(): Int {
        val realCount = getRealCount()
        if (0 == realCount) return 0
        return if (canLoop) Int.MAX_VALUE else realCount
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    fun getRealCount(): Int {
        return mData?.size ?: 0
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val realPosition = getRealPosition(position)
        val view = holderCreator.createView(container.context)
        container.addView(view)
        //
        mData?.let { mData ->
            if (mData.isNotEmpty() && realPosition < mData.size) {
                holderCreator.updateUI(container.context, view, realPosition, mData[realPosition])
            }
        }
        return view
    }

    fun getRealPosition(position: Int): Int {
        val realCount = getRealCount()
        if (0 == realCount) return 0
        return if (canLoop) position % realCount else position
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }

    override fun finishUpdate(container: ViewGroup) {
        viewPager?.let { viewPager ->
            var position = viewPager.currentItem
            if (position == 0) {
                position = viewPager.getFirstItem()
            } else if (position == count - 1) {
                position = viewPager.getLastItem()
            }
            try {
                viewPager.setCurrentItem(position, false)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    fun setViewPager(viewPager: BannerViewPager<T>?) {
        this.viewPager = viewPager
    }

    override fun getItemPosition(`object`: Any): Int {
        if (childCount > 0) {
            childCount--
            return POSITION_NONE
        }
        return super.getItemPosition(`object`)
    }

    override fun notifyDataSetChanged() {
        childCount = count
        super.notifyDataSetChanged()
    }

    fun setData(data: List<T>?) {
        this.mData = data
        notifyDataSetChanged()
    }
}