package com.example.learn.anim.star.item

import android.animation.Animator
import android.animation.ValueAnimator
import android.widget.ImageView
import com.example.utils.DensityUtil

/**
 * @author fqxyi
 * @date 2018/3/17
 * 星星1
 */
class StarAnimStarOne(var imageView: ImageView) {

    private var halfParentWidth = DensityUtil.dp2px(imageView.context, 135f)
    private var halfParentHeight = DensityUtil.dp2px(imageView.context, 130f)

    fun createAnim(): Animator {
        onRest()
        val valueAnimator = ValueAnimator.ofInt(0, 4400)
        // 获得当前动画的进度值，整型
        valueAnimator.addUpdateListener { animator ->
            val currentValue = animator.animatedValue as Int
            // 平移
            imageView.x = halfParentWidth - currentValue / 11.toFloat()
            imageView.y = halfParentHeight + currentValue / 36.toFloat()
            // 缩放
            when {
                currentValue < 1600 -> {
                    imageView.scaleX = currentValue / 1600f
                    imageView.scaleY = currentValue / 1600f
                }
                currentValue < 2400 -> {
                    imageView.scaleX = 1f - (currentValue - 1600) / 800f
                    imageView.scaleY = 1f - (currentValue - 1600) / 800f
                }
                currentValue < 3200 -> {
                    imageView.scaleX = (currentValue - 2400) / 800f * 1.32f
                    imageView.scaleY = (currentValue - 2400) / 800f * 1.32f
                }
                currentValue < 4000 -> {
                    imageView.scaleX = 1.32f - (currentValue - 3200) / 800f * 0.82f
                    imageView.scaleY = 1.32f - (currentValue - 3200) / 800f * 0.82f
                }
                currentValue < 4400 -> {
                    imageView.scaleX = 0.5f + (currentValue - 4000) / 400f * 0.26f
                    imageView.scaleY = 0.5f + (currentValue - 4000) / 400f * 0.26f
                }
                currentValue == 4400 -> {
                    imageView.scaleX = 1.09f
                    imageView.scaleY = 1.09f
                }
            }
        }
        valueAnimator.duration = 440
        return valueAnimator
    }

    fun onRest() {
        imageView.clearAnimation()
        imageView.scaleX = 1f
        imageView.scaleY = 1f
        imageView.x = halfParentWidth.toFloat()
        imageView.y = halfParentHeight.toFloat()
    }

}