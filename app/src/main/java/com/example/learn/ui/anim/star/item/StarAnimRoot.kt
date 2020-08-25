package com.example.learn.ui.anim.star.item

import android.animation.Animator
import android.animation.ValueAnimator
import android.widget.RelativeLayout
import com.example.utils.anim.AnimStatusListener

/**
 * @author fqxyi
 * @date 2018/3/17
 * 根布局
 */
class StarAnimRoot(var rootView: RelativeLayout) {

    fun createAnim(animUpdateListener: AnimStatusListener?): Animator {
        onRest()
        val valueAnimator = ValueAnimator.ofInt(0, 11600)
        // 获得当前动画的进度值，整型
        valueAnimator.addUpdateListener { animator ->
            val currentValue = animator.animatedValue as Int
            // 透明度
            if (currentValue <= 800) {
                rootView.alpha = currentValue / 800f
            }
            if (currentValue > 800 && currentValue < 9600) {
                rootView.alpha = 1f
            }
            if (currentValue >= 9600) {
                rootView.alpha = (11600 - currentValue) / 2000f
            }
            if (currentValue == 11600) {
                animUpdateListener?.onFinishListener()
            }
        }
        valueAnimator.duration = 1160
        return valueAnimator
    }

    fun onRest() {
        rootView.clearAnimation()
        rootView.alpha = 0f
    }

}