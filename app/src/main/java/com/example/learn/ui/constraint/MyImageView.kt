package com.example.learn.ui.constraint

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.example.log.LogUtil

/**
 * @author fqxyi
 * @date 2020/8/13
 */
class MyImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        LogUtil.i("CONSTRAINT_TEST", "MyImageView onMeasure")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        LogUtil.i("CONSTRAINT_TEST", "MyImageView onLayout")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        LogUtil.i("CONSTRAINT_TEST", "MyImageView onDraw")
    }

}