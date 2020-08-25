package com.example.learn.ui.anim.star.item

import android.animation.Animator
import android.animation.ValueAnimator
import android.widget.ImageView
import com.example.utils.device.DensityUtil

/**
 * @author fqxyi
 * @date 2018/3/17
 * 星星2
 */
class StarAnimStarThree(var imageView: ImageView) {

    var halfParentWidth = DensityUtil.dp2px(imageView.context, 135f)
    var halfParentHeight = DensityUtil.dp2px(imageView.context, 130f)

    fun createAnim(): Animator {
        onRest()
        val valueAnimator = ValueAnimator.ofInt(0, 4800)
        // 获得当前动画的进度值，整型
        valueAnimator.addUpdateListener { animator ->
            val currentValue = animator.animatedValue as Int
            // 平移
            imageView.x = halfParentWidth + currentValue / 36.toFloat()
            imageView.y = halfParentHeight - currentValue / 36.toFloat()
            // 缩放
            when {
                currentValue < 2000 -> {
                    imageView.scaleX = currentValue / 2000f
                    imageView.scaleY = currentValue / 2000f
                }
                currentValue < 2800 -> {
                    imageView.scaleX = 1f - (currentValue - 2000) / 800f
                    imageView.scaleY = 1f - (currentValue - 2000) / 800f
                }
                currentValue < 4000 -> {
                    imageView.scaleX = (currentValue - 2800) / 1200f
                    imageView.scaleY = (currentValue - 2800) / 1200f
                }
                currentValue < 4400 -> {
                    imageView.scaleX = 1f - (currentValue - 4000) / 400f * 0.34f
                    imageView.scaleY = 1f - (currentValue - 4000) / 400f * 0.34f
                }
                currentValue < 4800 -> {
                    imageView.scaleX = 0.66f - (currentValue - 4400) / 400f * 0.33f
                    imageView.scaleY = 0.66f - (currentValue - 4400) / 400f * 0.33f
                }
                currentValue == 4800 -> {
                    imageView.scaleX = 0.33f
                    imageView.scaleY = 0.33f
                }
            }
        }
        valueAnimator.duration = 480
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