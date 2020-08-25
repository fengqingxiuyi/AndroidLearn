package com.example.learn.ui.anim.star.item

import android.animation.Animator
import android.animation.ValueAnimator
import android.widget.TextView

/**
 * @author fqxyi
 * @date 2018/3/17
 * 标题文案
 */
class StarAnimTitle(var textView: TextView) {

    fun createAnim(): Animator {
        onRest()
        val valueAnimator = ValueAnimator.ofInt(0, 5600)
        // 获得当前动画的进度值，整型
        valueAnimator.addUpdateListener { animator ->
            val currentValue = animator.animatedValue as Int
            // 缩放
            textView.scaleX = 0.75f + currentValue / 5600f * 0.25f
            textView.scaleY = 0.75f + currentValue / 5600f * 0.25f
            // 透明度
            textView.alpha = currentValue / 5600f
        }
        valueAnimator.duration = 560
        return valueAnimator
    }

    fun onRest() {
        textView.clearAnimation()
        textView.scaleX = 0f
        textView.scaleY = 0f
    }

}