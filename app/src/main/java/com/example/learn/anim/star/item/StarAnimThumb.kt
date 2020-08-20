package com.example.learn.anim.star.item

import android.animation.Animator
import android.animation.ObjectAnimator
import android.widget.ImageView

/**
 * @author fqxyi
 * @date 2018/3/17
 * 点赞大拇指
 */
class StarAnimThumb(var imageView: ImageView) {

    fun createAnim(): Animator {
        onRest()
        val animator = ObjectAnimator.ofFloat(
            imageView, "translationY", 0f, 0f, -60f, 0f, -40f, 0f, 0f
        )
        animator.duration = 640
        return animator
    }

    fun onRest() {
        imageView.clearAnimation()
    }

}