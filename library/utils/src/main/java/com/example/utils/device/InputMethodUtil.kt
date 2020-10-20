package com.example.utils.device

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * 输入法的显示与隐藏
 */
object InputMethodUtil {
    /**
     * 切换软键盘的状态
     * 如当前为收起变为弹出,若当前为弹出变为收起
     */
    fun toggleInput(edittext: EditText) {
        val inputMethodManager = edittext.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            0,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    /**
     * 强制隐藏输入法键盘
     */
    @JvmStatic
    fun hideInput(edittext: EditText) {
        val inputMethodManager = edittext.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive) {
            inputMethodManager.hideSoftInputFromWindow(edittext.windowToken, 0)
        }
    }

    /**
     * 强制显示输入法键盘
     */
    @JvmStatic
    fun showInput(edittext: EditText) {
        val inputMethodManager = edittext.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(
            edittext,
            InputMethodManager.SHOW_FORCED
        )
    }

    /**
     * 输入法是否显示
     */
    @JvmStatic
    fun isShowing(activity: Activity): Boolean {
        /**
         * 方式一：并没什么用
         * if(getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
         * Toast.makeText(Main3Activity.this, "显示", Toast.LENGTH_SHORT).show();
         * }else {
         * Toast.makeText(Main3Activity.this, "没显示", Toast.LENGTH_SHORT).show();
         * }
         */
        /**
         * 方式二：isActive方法只要editText有焦点，它就返回true
         * if(inputMethodManager.isActive(editText)) {
         * Toast.makeText(Main3Activity.this, "显示", Toast.LENGTH_SHORT).show();
         * }else {
         * Toast.makeText(Main3Activity.this, "没显示", Toast.LENGTH_SHORT).show();
         * }
         */
        /**
         * 方式三：通过动态计算布局来判断软键盘是否在显示。
         */
        // 获取当前屏幕内容的高度
        val screenHeight = activity.window.decorView.height
        // 获得底部虚拟按键栏的高度
        val barHeight = getSoftButtonsBarHeight(activity)
        // 获取View可见区域的bottom
        val rect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        return screenHeight - rect.bottom - barHeight != 0
    }

    /**
     * 底部虚拟按键栏的高度
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun getSoftButtonsBarHeight(activity: Activity): Int {
        val metrics = DisplayMetrics()
        // 这个方法获取可能不是真实屏幕的高度
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        val usableHeight = metrics.heightPixels
        // 获取当前屏幕的真实高度
        activity.windowManager.defaultDisplay.getRealMetrics(metrics)
        val realHeight = metrics.heightPixels
        return if (realHeight > usableHeight) {
            realHeight - usableHeight
        } else {
            0
        }
    }
}