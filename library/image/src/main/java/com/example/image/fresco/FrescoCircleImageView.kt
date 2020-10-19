package com.example.image.fresco

import android.content.Context
import android.util.AttributeSet
import com.example.image.listener.ICircleImageView
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.RoundingParams

/**
 * @author fqxyi
 * @date 2018/2/22
 * 圆形图
 */
open class FrescoCircleImageView : FrescoImageView, ICircleImageView {
    constructor(context: Context, hierarchy: GenericDraweeHierarchy) : super(
        context, hierarchy
    ) {
        init()
    }

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
    ) : super(
        context, attrs, defStyleAttr, defStyleRes
    ) {
        init()
    }

    private fun init() {
        mHierarchy.roundingParams = getRoundingParams()
    }

    override fun setBorderColor(color: Int) {
        val roundingParams = getRoundingParams()
        roundingParams.setBorder(color, roundingParams.borderWidth)
        mHierarchy.roundingParams = roundingParams
    }

    override fun setBorderWidth(width: Int) {
        val roundingParams = getRoundingParams()
        roundingParams.setBorder(roundingParams.borderColor, width.toFloat())
        mHierarchy.roundingParams = roundingParams
    }

    private fun getRoundingParams(): RoundingParams {
        var roundingParams = mHierarchy.roundingParams
        if (null == roundingParams) {
            roundingParams = RoundingParams.asCircle()
        } else {
            roundingParams.roundAsCircle = true
        }
        return roundingParams!!
    }
}