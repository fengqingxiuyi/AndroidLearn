package com.example.learn.anim.star.item

import android.animation.Animator
import android.animation.ValueAnimator
import android.widget.ImageView
import com.example.utils.device.DensityUtil

/**
 * @author fqxyi
 * @date 2018/3/17
 * 星星3
 */
class StarAnimStarTwo(var imageView: ImageView) {

    var halfParentWidth = DensityUtil.dp2px(imageView.context, 135f)
    var halfParentHeight = DensityUtil.dp2px(imageView.context, 130f)

    fun createAnim(): Animator {
        onRest()
        val valueAnimator = ValueAnimator.ofInt(0, 4400)
        // 获得当前动画的进度值，整型
        valueAnimator.addUpdateListener { animator ->
            val currentValue = animator.animatedValue as Int
            // 平移
            imageView.x = halfParentWidth + currentValue / 18.toFloat()
            imageView.y = halfParentHeight - currentValue / 72.toFloat()
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
                    imageView.scaleX = (currentValue - 2400) / 800f
                    imageView.scaleY = (currentValue - 2400) / 800f
                }
                currentValue < 3600 -> {
                    imageView.scaleX = 1f - (currentValue - 3200) / 400f * 0.61f
                    imageView.scaleY = 1f - (currentValue - 3200) / 400f * 0.61f
                }
                currentValue < 4000 -> {
                    imageView.scaleX = 0.39f + (currentValue - 3600) / 400f * 0.35f
                    imageView.scaleY = 0.39f + (currentValue - 3600) / 400f * 0.35f
                }
                currentValue < 4400 -> {
                    imageView.scaleX = 0.74f + (currentValue - 4000) / 400f * 0.34f
                    imageView.scaleY = 0.74f + (currentValue - 4000) / 400f * 0.34f
                }
                currentValue == 4400 -> {
                    imageView.scaleX = 1.08f
                    imageView.scaleY = 1.08f
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