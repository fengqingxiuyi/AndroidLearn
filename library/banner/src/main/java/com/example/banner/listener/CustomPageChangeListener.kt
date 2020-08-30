package com.example.banner.listener

import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener

/**
 * 翻页指示器适配器
 */
class CustomPageChangeListener : OnPageChangeListener {

    // Image类型指示点view和id集合
    private var pointImgViews: List<ImageView>? = null
    private lateinit var pointImgIds: IntArray

    // 自定义类型的布局数据
    private var pointView: View? = null
    private var pointSize = 0
    private var pointChangeListener: PointChangeListener? = null

    // 接口回调
    private var listener: OnPageChangeListener? = null
    fun setPageChangeListener(listener: OnPageChangeListener?) {
        this.listener = listener
    }

    // 构造方法
    constructor(pointView: View?, pointSize: Int, pointChangeListener: PointChangeListener?) {
        this.pointView = pointView
        this.pointSize = pointSize
        this.pointChangeListener = pointChangeListener
    }

    constructor(pointImgViews: List<ImageView>?, pointImgIds: IntArray) {
        this.pointImgViews = pointImgViews
        this.pointImgIds = pointImgIds
    }

    override fun onPageScrollStateChanged(state: Int) {
        listener?.onPageScrollStateChanged(state)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        listener?.onPageScrolled(position, positionOffset, positionOffsetPixels)
    }

    override fun onPageSelected(index: Int) {
        listener?.onPageSelected(index)
        pointChangeListener?.getInfo(pointView, index, pointSize)
        // 指示点切换
        pointImgViews?.let { pointImgViews ->
            if (pointImgViews.isNotEmpty()) {
                for (i in pointImgViews.indices) {
                    if (index == i) {
                        pointImgViews[index].setImageResource(pointImgIds[1])
                    } else {
                        pointImgViews[i].setImageResource(pointImgIds[0])
                    }
                }
            }
        }
    }

    fun setPointImgData(pointImgViews: List<ImageView>?, pointImgIds: IntArray) {
        this.pointImgViews = pointImgViews
        this.pointImgIds = pointImgIds
    }

    fun setPointChangeListener(view: View?, size: Int, pointChangeListener: PointChangeListener?) {
        pointView = view
        pointSize = size
        this.pointChangeListener = pointChangeListener
    }
}