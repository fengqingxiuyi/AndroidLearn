package com.example.ui.tablayout

import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout

/**
 * 略
 *
 * @author: shenbf
 */
object TabLayoutHelper {

  private const val TAG = "TabLayoutHelper"

  fun getTabTextView(tab: TabLayout.Tab): TextView? {
    return try {
      val tabView = tab.view
      val tabTextViewField = TabLayout.TabView::class.java.getDeclaredField("textView")
      tabTextViewField.isAccessible = true
      tabTextViewField.get(tabView) as TextView
    } catch (e: Exception) {
      Log.e(TAG, "", e)
      null
    }
  }

  fun addDrawableEnd(context: Context, tab: TabLayout.Tab, drawableEndResId: Int, padding: Int = 0) {
    try {
      val tabTextView = getTabTextView(tab) ?: return
      tabTextView.compoundDrawablePadding = padding
      val drawable = ContextCompat.getDrawable(context, drawableEndResId)
      drawable?.let {
        it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
        tabTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, it, null)
      }
    } catch (e: Exception) {
      Log.e(TAG, "", e)
    }
  }

  fun removeDrawableEnd(tab: TabLayout.Tab) {
    try {
      val tabTextView = getTabTextView(tab) ?: return
      tabTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
    } catch (e: Exception) {
      Log.e(TAG, "", e)
    }
  }

}