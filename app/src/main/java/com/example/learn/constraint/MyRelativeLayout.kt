package com.example.learn.constraint

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.example.utils.LogUtil

/**
 * @author fqxyi
 * @date 2020/8/13
 *
CONSTRAINT_TEST: MyImageView onMeasure
CONSTRAINT_TEST: MyTextView onMeasure
CONSTRAINT_TEST: MyEditText onMeasure
CONSTRAINT_TEST: MyImageView onMeasure
CONSTRAINT_TEST: MyEditText onMeasure
CONSTRAINT_TEST: MyTextView onMeasure
CONSTRAINT_TEST: MyRelativeLayout onMeasure
CONSTRAINT_TEST: MyImageView onMeasure
CONSTRAINT_TEST: MyTextView onMeasure
CONSTRAINT_TEST: MyEditText onMeasure
CONSTRAINT_TEST: MyImageView onMeasure
CONSTRAINT_TEST: MyEditText onMeasure
CONSTRAINT_TEST: MyTextView onMeasure
CONSTRAINT_TEST: MyRelativeLayout onMeasure
CONSTRAINT_TEST: MyTextView onLayout
CONSTRAINT_TEST: MyEditText onLayout
CONSTRAINT_TEST: MyImageView onLayout
CONSTRAINT_TEST: MyRelativeLayout onLayout
CONSTRAINT_TEST: MyTextView onDraw
CONSTRAINT_TEST: MyEditText onDraw
CONSTRAINT_TEST: MyImageView onDraw
 *
 * 每一个子View都会多绘制一次，并且无法设置权重，无法让MyTextView的底部与MyImageView的顶部对齐
 * 阅读RelativeLayout.onMeasure源码可知：它需要先执行measureChildHorizontal进行横向测量，再执行measureChild进行竖向测量
 */
class MyRelativeLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        LogUtil.i("CONSTRAINT_TEST", "MyRelativeLayout onMeasure")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        LogUtil.i("CONSTRAINT_TEST", "MyRelativeLayout onLayout")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        LogUtil.i("CONSTRAINT_TEST", "MyRelativeLayout onDraw")
    }

}