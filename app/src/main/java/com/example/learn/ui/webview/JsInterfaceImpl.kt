package com.example.learn.ui.webview

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import com.example.learn.BuildConfig
import com.example.ui.toast.ToastUtil
import com.example.utils.activity.ActivitiesManager.Companion.getInstance
import com.example.webview_module.IWebviewBinderCallback
import kotlin.math.abs

/**
 * JS交互实现类，在[get][JsBridge]类中注册
 *
 * 重点：
 * 一般情况下Context均为Application，所以在执行跳转页面等必须要用Activity的时候，请使用以下语句：
 * ActivitiesManager.getInstance().getLastAliveActivity()
 */
class JsInterfaceImpl {

    /**
     * 因为Webview处于独立进程中，所以需要通过以下方式获取上一个Activity
     */
    private fun getActivity(context: Context): Activity? {
        return if (context is Activity) {
            context
        } else {
            getInstance().getLastAliveActivity()
        }
    }

    fun switchApi(context: Context, params: String, callback: IWebviewBinderCallback?) {
        if (checkContextAndParamsError(context, params)) {
            return
        }
        //拿到params之后执行切换API的业务逻辑
    }

    /***                               提供给本类的工具方法                                  */

    private var lastClickTime: Long = 0

    private fun isFastClick(): Boolean {
        val time = System.currentTimeMillis()
        if (abs(time - lastClickTime) < 500) {
            return true
        }
        lastClickTime = time
        return false
    }

    private fun checkContextAndParamsError(context: Context, params: String): Boolean {
        return checkContextError(context) || checkParamsError(params)
    }

    private fun checkContextError(context: Context): Boolean {
        if (context is Activity) {
            return false
        }
        toast("context 不是 activity")
        return true
    }

    private fun checkParamsError(params: String): Boolean {
        if (!TextUtils.isEmpty(params)) {
            return false
        }
        toast("params为空")
        return true
    }

    /**
     * 仅DEBUG包弹toast提示
     */
    private fun toast(msg: String) {
        if (BuildConfig.DEBUG) {
            ToastUtil.toast(msg)
        }
    }
}