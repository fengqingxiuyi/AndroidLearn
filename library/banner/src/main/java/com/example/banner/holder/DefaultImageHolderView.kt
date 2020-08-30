package com.example.banner.holder

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType

/**
 * @author fqxyi
 * @date 2017/9/1
 * 本地、网络图片加载
 */
class DefaultImageHolderView<T> constructor(private var bannerHeight: Int = ViewGroup.LayoutParams.MATCH_PARENT) : Holder<T> {

    private var bannerBgColor = 0

    private var scaleType = ScaleType.CENTER_CROP

    // 显示图片事件
    private var imageShowListener: ImageShowListener<T>? = null

    interface ImageShowListener<T> {
        fun showImage(imageView: ImageView, data: T)
    }

    fun setImageShowListener(imageShowListener: ImageShowListener<T>?) {
        this.imageShowListener = imageShowListener
    }

    // 点击事件
    private var clickListener: ImageClickListener<T>? = null

    interface ImageClickListener<T> {
        fun click(view: View, position: Int, data: T)
    }

    fun setClickListener(clickListener: ImageClickListener<T>?) {
        this.clickListener = clickListener
    }

    // 长按事件
    private var longListener: ImageLongClickListener<T>? = null

    interface ImageLongClickListener<T> {
        fun longClick(view: View, position: Int, data: T)
    }

    fun setLongClickListener(longListener: ImageLongClickListener<T>?) {
        this.longListener = longListener
    }

    /**
     * 你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
     */
    override fun createView(context: Context?): View {
        val imageView = ImageView(context)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bannerHeight)
        imageView.layoutParams = params
        imageView.scaleType = scaleType
        if (bannerBgColor != 0) {
            imageView.setBackgroundColor(bannerBgColor)
        }
        return imageView
    }

    override fun updateUI(context: Context?, view: View, position: Int, data: T) {
        if (view is ImageView) {
            imageShowListener?.showImage(view, data)
        }
        view.setOnClickListener {
            clickListener?.click(view, position, data)
        }
        view.setOnLongClickListener {
            longListener?.longClick(view, position, data)
            false
        }
    }

    fun setBannerHeight(bannerHeight: Int) {
        this.bannerHeight = bannerHeight
    }

    fun setScaleType(scaleType: ScaleType) {
        this.scaleType = scaleType
    }

    fun setBannerBgColor(color: Int) {
        bannerBgColor = color
    }
}