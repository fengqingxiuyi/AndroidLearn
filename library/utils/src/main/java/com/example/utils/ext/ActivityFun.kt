package com.example.utils.ext

import android.app.Activity
import android.content.res.Resources
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo

/**
 * 状态栏高度
 */
fun Activity.statusBarHeight(): Int {
    val resources = Resources.getSystem()
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
}
fun isKeyboardSubmit(actionId: Int, event: KeyEvent?): Boolean =
    actionId == EditorInfo.IME_ACTION_GO ||
            actionId == EditorInfo.IME_ACTION_DONE ||
            (event != null && event.action == KeyEvent.ACTION_UP && event.keyCode == KeyEvent.KEYCODE_ENTER)
