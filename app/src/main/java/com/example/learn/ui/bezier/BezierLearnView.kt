package com.example.learn.ui.bezier

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.example.utils.device.DensityUtil
import com.example.utils.device.DeviceUtil

/**
 * @author fqxyi
 * @date 2020/8/24
 * 贝塞尔曲线学习
 */
class BezierLearnView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val screenWidth = DeviceUtil.getScreenWidth(context)
    private val dp1 = DensityUtil.dp2px(context, 95f)
    private val dp2 = DensityUtil.dp2px(context, 110f)

    private val mPaint = Paint()
    private val mPath = Path()

    init {
        mPaint.color = -0xf2b4
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPath.reset()

        mPath.moveTo(0f, 0f)
        mPath.lineTo(screenWidth.toFloat(), 0f)
        mPath.lineTo(screenWidth.toFloat(), dp1.toFloat())
        mPath.quadTo(screenWidth / 2f, dp2.toFloat(), 0f, dp1.toFloat())
        mPath.lineTo(0f, 0f)

        canvas.drawPath(mPath, mPaint)
    }

}
