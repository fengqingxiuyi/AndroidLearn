package com.example.ui.utils

import android.content.Context
import android.util.TypedValue

/**
 *
 *
 * @author shenBF
 */
object DimenUtil {

  fun dp2px(context: Context, dp: Float): Int {
    val displayMetrics = context.resources.displayMetrics
    return (TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics
    ) + 0.5f).toInt()
  }

  fun dp2pxFloat(context: Context, dp: Float): Float {
    val displayMetrics = context.resources.displayMetrics
    return TypedValue.applyDimension(
      TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics
    )
  }
}