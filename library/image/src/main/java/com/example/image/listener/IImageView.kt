package com.example.image.listener

import android.graphics.drawable.Drawable
import com.facebook.drawee.drawable.ScalingUtils

/**
 * @author fqxyi
 * @date 2018/2/22
 */
interface IImageView {
    fun setImageCircle()
    fun setImageRound(radius: Float)

    /**
     * 设置图片自适应宽高
     * 适用场景：宽和高 都可以是 WRAP_CONTENT，但是不能为 0
     *
     * @param enableWrapContent 是否自适应
     */
    fun setEnableWrapContent(enableWrapContent: Boolean)
    fun setPlaceholderImage(resource: Int)
    fun setPlaceholderImage(resource: Int, scaleType: ScalingUtils.ScaleType?)
    fun setPlaceholderImage(drawable: Drawable?)
    fun setPlaceholderImage(drawable: Drawable?, scaleType: ScalingUtils.ScaleType?)
    fun setImageUrl(resId: Int)
    fun setImageUrl(resId: Int, loadListener: IImageLoadListener?)
    fun setImageUrl(resId: Int, width: Int, height: Int)
    fun setImageUrl(url: String?)
    fun setImageUrl(url: String?, loadListener: IImageLoadListener?)
    fun setImageUrl(url: String?, width: Int, height: Int)
    fun getImageUrl(): String?
    fun getImageRes(): Int
}