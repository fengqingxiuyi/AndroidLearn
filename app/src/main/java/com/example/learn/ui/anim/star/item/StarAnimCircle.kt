package com.example.learn.ui.anim.star.item

import android.animation.Animator
import android.animation.ValueAnimator
import android.widget.ImageView

/**
 * @author fqxyi
 * @date 2018/3/17
 * 椭圆
 */
class StarAnimCircle(var imageView: ImageView) {

    fun createAnim(): Animator {
        onRest()
        val valueAnimator = ValueAnimator.ofInt(0, 8800)
        // 获得当前动画的进度值，整型
        valueAnimator.addUpdateListener { animator ->
            val currentValue = animator.animatedValue as Int
            // 缩放
            if (currentValue <= 2000) {
                imageView.scaleX = 0.5f * currentValue / 2000f + 0.5f
                imageView.scaleY = 0.5f * currentValue / 2000f + 0.5f
            }
            if (currentValue in 8001..8800) {
                imageView.scaleX = 0.5f * (8800 - currentValue) / 800f
                imageView.scaleY = 0.5f * (8800 - currentValue) / 800f
            }
        }
        valueAnimator.duration = 880
        return valueAnimator
    }

    fun onRest() {
        imageView.clearAnimation()
        imageView.scaleX = 0f
        imageView.scaleY = 0f
    }

}