package com.example.utils.anim

import android.animation.Animator
import android.app.Activity
import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference
import java.util.*


/**
 * @author fqxyi
 * @date 2018/3/18
 * 动画集合
 */
class AnimGather(activity: Activity) {

    companion object {
        // 正常动画
        private const val TYPE_ANIM = 1
        // 使用 handler 回调
        private const val TYPE_CALL = 2
    }

    private var animCache = HashMap<Animator, AnimNote>()
    private var animNoteList = ArrayList<AnimNote>()
    private var mHandler: Handler = MyHandler(activity)
    private var handlerCall: HandlerCall? = null

    fun addAnim(animator: Animator, delay: Int) {
        val animNote = getAnimNote(animator, delay)
        animNoteList.add(animNote)
    }

    fun startAnim() {
        if (animNoteList.size > 0) {
            if (isRunning()) {
                cancel()
            }
            for (animNote in animNoteList) {
                val message = Message()
                message.obj = animNote.animator
                message.what = TYPE_ANIM
                mHandler.sendMessageDelayed(message, animNote.delay.toLong())
            }
        }
    }

    fun cancel() {
        if (animNoteList.size > 0) {
            if (isRunning()) {
                for (animNote in animNoteList) {
                    animNote.animator.cancel()
                }
            }
        }
    }

    fun isRunning(): Boolean {
        if (animNoteList.size > 0) {
            for (animNote in animNoteList) {
                val running = animNote.animator.isRunning
                if (running) {
                    return true
                }
            }
        }
        return false
    }

    /***
     * 借用 这个类的handler 用一下
     * @param handlerCall
     * @param delay
     */
    fun postRunOnUI(handlerCall: HandlerCall, message: Message, delay: Int) {
        setHandlerCall(handlerCall)
        message.what = TYPE_CALL
        mHandler.sendMessageDelayed(message, delay.toLong())
    }

    private fun setHandlerCall(handlerCall: HandlerCall) {
        this.handlerCall = handlerCall
    }

    fun onDestroy() {
        cancel()
        mHandler.removeCallbacksAndMessages(null)
        animCache.clear()
        animNoteList.clear()
        handlerCall = null
    }

    private inner class MyHandler(activity: Activity) : Handler() {

        private val mActivity: WeakReference<Activity> = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            if (mActivity.get() == null) {
                return
            }
            if (msg.what == TYPE_ANIM) {
                if (msg.obj is Animator) {
                    val animator = msg.obj as Animator
                    animator.start()
                }
            }
            if (msg.what == TYPE_CALL) {
                handlerCall?.call(msg)
            }
        }

    }

    private fun getAnimNote(animator: Animator, delay: Int): AnimNote {
        if (animCache.containsKey(animator)) {
            return animCache[animator]!!
        }
        val animNote = AnimNote(animator, delay)
        animCache[animator] = animNote
        return animNote
    }

    inner class AnimNote(var animator: Animator, var delay: Int)

    interface HandlerCall {
        fun call(message: Message?)
    }

}