package com.example.image

import android.content.Context
import android.util.AttributeSet
import com.example.image.fresco.FrescoCircleImageView
import com.facebook.drawee.generic.GenericDraweeHierarchy

/**
 * @author fqxyi
 * @date 2018/2/22
 * 圆形图 业务实现类
 */
class FCircleImageView : FrescoCircleImageView {

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

    override fun setBorderColor(color: Int) {
        super.setBorderColor(color)
    }

    override fun setBorderWidth(width: Int) {
        super.setBorderWidth(width)
    }
}