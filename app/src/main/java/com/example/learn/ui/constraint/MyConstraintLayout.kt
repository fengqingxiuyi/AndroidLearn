package com.example.learn.ui.constraint

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.log.LogUtil

/**
 * @author fqxyi
 * @date 2020/8/13
 *
//将MyTextView和MyEditText的宽度设置为wrap_content，会使权重失效，输出内容如下：
CONSTRAINT_TEST: MyTextView onMeasure
CONSTRAINT_TEST: MyEditText onMeasure
CONSTRAINT_TEST: MyImageView onMeasure
CONSTRAINT_TEST: MyConstraintLayout onMeasure
CONSTRAINT_TEST: MyTextView onMeasure
CONSTRAINT_TEST: MyEditText onMeasure
CONSTRAINT_TEST: MyImageView onMeasure
CONSTRAINT_TEST: MyConstraintLayout onMeasure
CONSTRAINT_TEST: MyTextView onLayout
CONSTRAINT_TEST: MyEditText onLayout
CONSTRAINT_TEST: MyImageView onLayout
CONSTRAINT_TEST: MyConstraintLayout onLayout
CONSTRAINT_TEST: MyTextView onDraw
CONSTRAINT_TEST: MyEditText onDraw
CONSTRAINT_TEST: MyImageView onDraw
//将MyTextView和MyEditText的宽度设置为0dp，会使权重生效，输出内容如下：
CONSTRAINT_TEST: MyTextView onMeasure
CONSTRAINT_TEST: MyEditText onMeasure
CONSTRAINT_TEST: MyImageView onMeasure
CONSTRAINT_TEST: MyTextView onMeasure
CONSTRAINT_TEST: MyEditText onMeasure
CONSTRAINT_TEST: MyConstraintLayout onMeasure
CONSTRAINT_TEST: MyTextView onMeasure
CONSTRAINT_TEST: MyEditText onMeasure
CONSTRAINT_TEST: MyImageView onMeasure
CONSTRAINT_TEST: MyTextView onMeasure
CONSTRAINT_TEST: MyEditText onMeasure
CONSTRAINT_TEST: MyConstraintLayout onMeasure
CONSTRAINT_TEST: MyTextView onLayout
CONSTRAINT_TEST: MyEditText onLayout
CONSTRAINT_TEST: MyImageView onLayout
CONSTRAINT_TEST: MyConstraintLayout onLayout
CONSTRAINT_TEST: MyTextView onDraw
CONSTRAINT_TEST: MyEditText onDraw
CONSTRAINT_TEST: MyImageView onDraw
 *
 * 集合了RelativeLayout和LinearLayout的功能，既可以设置权重，也可以设置约束，相对于RelativeLayout的相对关系，功能更为丰富
 * 如果不设置权重，子View只绘制一次(internalMeasureChildren)，
 * 设置了权重(sizeDependentWidgetsCount > 0条件满足)，有权重关系的子View会多绘制一次(internalMeasureChildren -> measure)
 * 而sizeDependentWidgetsCount是在ConstraintLayout.onMeasure中先调用updateHierarchy函数去增加的，并且针对每一个ConstraintLayout实例该函数只会被调用一次。
 */
class MyConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        LogUtil.i("CONSTRAINT_TEST", "MyConstraintLayout onMeasure")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        LogUtil.i("CONSTRAINT_TEST", "MyConstraintLayout onLayout")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        LogUtil.i("CONSTRAINT_TEST", "MyConstraintLayout onDraw")
    }

}