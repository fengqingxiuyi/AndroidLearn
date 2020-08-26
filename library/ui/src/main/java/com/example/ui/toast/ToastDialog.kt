package com.example.ui.toast

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.example.ui.R

class ToastDialog(private val ctx: Context) : Dialog(ctx, R.style.toast_transparent_style) {

    init {
        val params = window?.attributes
        //这条就是控制点击背景的时候  如果被覆盖的view有点击事件那么就会直接触发(dialog消失并且触发背景下面view的点击事件)
        params?.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        window?.attributes = params
    }

    fun getActivity(): Activity? {
        return if (ctx is Activity) {
            ctx
        } else {
            null
        }
    }
}