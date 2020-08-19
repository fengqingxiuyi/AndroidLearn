package com.example.common.global

import android.app.Application
import android.content.Context

object AppGlobal {
    /**
     * 必须要用application的时候才用这个
     */
    lateinit var application: Application

    /**
     * 默认使用该全局上下文
     */
    lateinit var appContext: Context
}