package com.example.utils.ext

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.example.utils.other.Utils

fun string(resId: Int): String {
  return Utils.context.resources.getString(resId)
}

fun color(resId: Int, context: Context = Utils.context): Int {
  return ResourcesCompat.getColor(context.theme.resources, resId, context.theme)
}

fun drawable(resId: Int, context: Context = Utils.context): Drawable? {
  return ResourcesCompat.getDrawable(context.theme.resources, resId, context.theme)
}