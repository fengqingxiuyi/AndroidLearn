package com.example.image

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.example.image.fresco.FrescoImageView
import com.example.image.listener.IImageLoadListener
import com.example.image.listener.IImageView
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchy

/**
 * @author fqxyi
 * @date 2018/2/22
 * 业务实现类
 */
class FImageView : FrescoImageView, IImageView {
    constructor(context: Context, hierarchy: GenericDraweeHierarchy) : super(
        context,
        hierarchy
    ) {
    }

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun setImageCircle() {
        super.setImageCircle()
    }

    override fun setImageRound(radius: Float) {
        super.setImageRound(radius)
    }

    override fun setEnableWrapContent(enableWrapContent: Boolean) {
        super.setEnableWrapContent(enableWrapContent)
    }

    override fun setPlaceholderImage(resource: Int) {
        super.setPlaceholderImage(resource)
    }

    override fun setPlaceholderImage(resource: Int, scaleType: ScalingUtils.ScaleType?) {
        super.setPlaceholderImage(resource, scaleType)
    }

    override fun setPlaceholderImage(drawable: Drawable?) {
        super.setPlaceholderImage(drawable)
    }

    override fun setPlaceholderImage(drawable: Drawable?, scaleType: ScalingUtils.ScaleType?) {
        super.setPlaceholderImage(drawable, scaleType)
    }

    override fun setImageUrl(resId: Int) {
        super.setImageUrl(resId)
    }

    override fun setImageUrl(resId: Int, loadListener: IImageLoadListener?) {
        super.setImageUrl(resId, loadListener)
    }

    override fun setImageUrl(resId: Int, width: Int, height: Int) {
        super.setImageUrl(resId, width, height)
    }

    override fun setImageUrl(url: String?) {
        super.setImageUrl(url)
    }

    override fun setImageUrl(url: String?, loadListener: IImageLoadListener?) {
        super.setImageUrl(url, loadListener)
    }

    override fun setImageUrl(url: String?, width: Int, height: Int) {
        super.setImageUrl(url, width, height)
    }

    override fun getImageUrl(): String? {
        return super.getImageUrl()
    }

    override fun getImageRes(): Int {
        return super.getImageRes()
    }
}