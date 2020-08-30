package com.example.banner.holder

import android.content.Context
import android.view.View

interface Holder<T> {
    fun createView(context: Context?): View
    fun updateUI(context: Context?, view: View, position: Int, data: T)
}