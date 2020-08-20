package com.example.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout


/**
 * @author fqxyi
 * @date 2020/8/20
 * Android 状态栏 研究，具体请查看文章[《探索 单个Fragment实现沉浸式，其余Fragment不实现的解决办法》](https://www.fqxyi.top/archives/205/)
 */
object StatusBarUtil {

    // 全局保存状态栏颜色
    private var statusBarColor: Int = Color.BLACK

    // 全局保存状态栏高度
    private val statusBarHeight = 0

    // 全局保存自己绘制的状态栏View
    private var statusView: View? = null

    /**
     * 使状态栏透明并且兼容有虚拟按键的手机
     */
    fun setStatusBarTransparent(activity: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        // 实现功能
        val window: Window = activity.window
        window.clearFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = window.statusBarColor
        window.statusBarColor = Color.TRANSPARENT
    }

    /**
     * 为布局文件中新增的状态栏布局设置背景色和高度
     */
    fun setStatusViewAttr(view: View?, activity: Activity?) {
        if (view == null || activity == null) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        val layoutParams: ViewGroup.LayoutParams = view.layoutParams
        layoutParams.height = getStatusBarHeight(activity)
        view.layoutParams = layoutParams
        view.setBackgroundColor(getStatusBarColor(activity))
    }

    /**
     * 绘制一个和状态栏一样高的View，并添加到decorView中
     */
    fun createStatusView(activity: Activity?) {
        if (activity == null) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        // 绘制一个和状态栏一样高的View
        statusView = View(activity)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            getStatusBarHeight(activity)
        )
        statusView!!.layoutParams = params
        // 设置背景色
        statusView!!.setBackgroundColor(getStatusBarColor(activity))
        // 添加 statusView 到布局中
        val decorView: ViewGroup = activity.window.decorView as ViewGroup
        decorView.addView(statusView)
    }

    /**
     * 是否显示沉浸式效果
     * @param activity 上下文
     * @param hide true 沉浸式 false 非沉浸式
     */
    fun hideStatusView(activity: Activity, hide: Boolean) {
        if (statusView == null) {
            return
        }
        val decorView: ViewGroup = activity.window.decorView as ViewGroup
        val firstView: View = decorView.getChildAt(0)
        val firstParams =
            firstView.layoutParams as FrameLayout.LayoutParams
        if (hide) {
            firstParams.topMargin = 0
            statusView!!.visibility = View.GONE
        } else {
            firstParams.topMargin = getStatusBarHeight(activity)
            statusView!!.visibility = View.VISIBLE
        }
        firstView.layoutParams = firstParams
    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Context?): Int {
        if (context?.resources == null) {
            return 0
        }
        if (statusBarHeight != 0) {
            return statusBarHeight
        }
        val resourceId: Int =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId == 0) {
            0
        } else context.resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 获取状态栏颜色
     */
    fun getStatusBarColor(activity: Activity?): Int {
        if (activity?.window == null) {
            return Color.BLACK
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return Color.BLACK
        }
        if (statusBarColor == Color.BLACK) {
            return activity.window.statusBarColor
        }
        return if (statusBarColor == Color.TRANSPARENT) {
            Color.BLACK
        } else statusBarColor
    }

}