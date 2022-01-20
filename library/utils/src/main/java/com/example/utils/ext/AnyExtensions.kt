package com.example.utils.ext

import android.text.TextUtils
import android.view.View
import java.util.*


fun Any?.notNull(): Boolean{
  return this != null
}

fun Any?.isNull(): Boolean{
  return this == null
}

/**
 * Returns true if the current layout direction is [View.LAYOUT_DIRECTION_RTL].
 *
 * @return
 *
 *This always returns false on versions below JELLY_BEAN_MR1.
 * warn: 此方法有bug，切换语言后值不变，建议使用view.isRtl()判断
 */
fun isRtlLayout() =
  TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL
