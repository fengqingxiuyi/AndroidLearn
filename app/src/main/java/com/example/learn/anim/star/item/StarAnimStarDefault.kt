package com.example.learn.anim.star.item

import android.animation.Animator
import android.animation.ValueAnimator
import android.widget.ImageView

/**
 * @author fqxyi
 * @date 2018/3/17
 * 星星合集
 */
class StarAnimStarDefault(var imageView: ImageView) {

    fun createAnim(): Animator {
        onRest()
        val valueAnimator = ValueAnimator.ofInt(0, 3600)
        // 获得当前动画的进度值，整型
        valueAnimator.addUpdateListener { animator ->
            val currentValue = animator.animatedValue as Int
            // 缩放
            if (currentValue < 3600) {
                imageView.scaleX = currentValue / 3600f
                imageView.scaleY = currentValue / 3600f
            } else {
                imageView.scaleX = 1f
                imageView.scaleY = 1f
            }
        }
        valueAnimator.duration = 360
        return valueAnimator
    }

    fun onRest() {
        imageView.clearAnimation()
        imageView.scaleX = 0f
        imageView.scaleY = 0f
    }

}