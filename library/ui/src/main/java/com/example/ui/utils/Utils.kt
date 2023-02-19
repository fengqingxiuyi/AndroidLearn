package com.example.ui.utils

import android.content.Context
import android.view.View

/**
 * 
 * 
 * @author shenBF
 */
fun isRtlLayout(context: Context): Boolean {
  return context.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
}