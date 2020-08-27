package com.example.ui.toast

import android.app.Activity
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.ui.BuildConfig
import com.example.ui.R
import java.util.*
import java.util.concurrent.LinkedBlockingDeque

/**
 * 自定义Toast，控制Toast的显示
 */
object ToastUtil {

    interface ToastListener {
        fun getLastAliveActivity(): Activity?
        fun isForeground(): Boolean
    }

    private lateinit var sToastListener: ToastListener
    private lateinit var handler: Handler

    private var dialog: ToastDialog? = null

    private const val MSG_NORMAL = 0
    private const val MSG_REMOVE = 1

    // 动画时间 ToastDialog显示隐藏动画合计为400，故写400
    private const val ANIMATION_DURATION = 400

    // 消失时间
    private const val DEFAULT_DELAY_MILLIS: Long = 2000
    private var DELAY_MILLIS = DEFAULT_DELAY_MILLIS

    private val queue = LinkedBlockingDeque<String>()
    private val activityRunnableMap = HashMap<String, Runnable>()

    private const val splitStr = "【ToastUtil】"

    private class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            try {
                when (msg.what) {
                    MSG_REMOVE -> {
                        dialog?.dismiss()
                        if (queue.size > 0) {
                            val message = queue.remove()
                            val objArr = message.split(splitStr.toRegex()).toTypedArray()
                            show(objArr[0], objArr[1].toInt())
                        }
                    }
                    else -> {
                        val message = msg.obj as String
                        if (dialog?.isShowing == true) {
                            //先remove再add，是为了保证已存在的消息放在最后一个位置
                            queue.remove(message)
                            queue.add(message)
                        } else {
                            val objArr = message.split(splitStr.toRegex()).toTypedArray()
                            show(objArr[0], objArr[1].toInt())
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun init(toastListener: ToastListener) {
        sToastListener = toastListener
        handler = MyHandler()
    }

    @JvmStatic
    @JvmOverloads
    fun toast(msg: String?, type: Int = ToastType.TYPE_NORMAL) {
        if (BuildConfig.DEBUG) {
            toast(msg, DEFAULT_DELAY_MILLIS * 2, type)
        } else {
            toast(msg, DEFAULT_DELAY_MILLIS, type)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun toast(msg: String?, delayMillis: Long, type: Int = ToastType.TYPE_NORMAL) {
        if (TextUtils.isEmpty(msg) || !sToastListener.isForeground()) {
            return
        }
        DELAY_MILLIS = delayMillis
        val message = Message.obtain()
        message.what = MSG_NORMAL
        message.obj = msg + splitStr + type
        handler.sendMessage(message)
    }

    private fun show(msg: String, type: Int) {
        show(sToastListener.getLastAliveActivity(), msg, type)
    }

    private fun show(activity: Activity?, msg: String, type: Int) {
        try {
            if (activity == null || !sToastListener.isForeground() || dialog?.isShowing == true) {
                return
            }
            if (dialog?.getActivity() !== activity) {
                dialog = ToastDialog(activity)
                dialog!!.setContentView(R.layout.toast_view)
            }
            //set flag
            val toastFlag = dialog!!.findViewById<View>(R.id.toastFlag) as ImageView
            when (type) {
                ToastType.TYPE_SUCCESS -> {
                    toastFlag.visibility = View.VISIBLE
                    toastFlag.setImageResource(R.drawable.toast_success)
                }
                ToastType.TYPE_ERROR -> {
                    toastFlag.visibility = View.VISIBLE
                    toastFlag.setImageResource(R.drawable.toast_error)
                }
                else -> {
                    toastFlag.visibility = View.GONE
                }
            }
            //set text
            val toastMsg = dialog!!.findViewById<View>(R.id.toastMsg) as TextView
            toastMsg.text = msg.trim()
            //显示
            dialog!!.show()
            //隐藏
            if (DELAY_MILLIS < ANIMATION_DURATION) {
                DELAY_MILLIS = DEFAULT_DELAY_MILLIS
            }
            var runnable = activityRunnableMap[getActivityName(activity)]
            if (runnable == null) {
                runnable = Runnable {
                    val message = Message.obtain()
                    message.what = MSG_REMOVE
                    handler.sendMessage(message)
                }
                activityRunnableMap[getActivityName(activity)] = runnable
            }
            handler.postDelayed(runnable, DELAY_MILLIS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onDestroy(activity: Activity) {
        try {
            if (dialog?.getActivity() !== activity) {
                return
            }
            dialog?.dismiss()
            val runnable = activityRunnableMap[getActivityName(activity)]
            if (runnable != null) {
                handler.removeCallbacks(runnable)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onAppExit() {
        try {
            dialog?.dismiss()
            handler.removeCallbacksAndMessages(null)
            queue.clear()
            activityRunnableMap.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getActivityName(activity: Activity): String {
        var name = "unknown"
        try {
            name = activity.javaClass.simpleName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return name
    }
}