package com.example.common.ui.empty.utils

import android.R
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.utils.LogUtil

/**
 * 略
 *
 * @author: shenbf
 */
object ErrorViewUtils {

  private const val TAG = "ErrorViewUtils"

  fun addErrorLayout(act: Activity, errorView: View?): View? {
    try {
      if (act.window.decorView is ViewGroup) {
        val viewGroup = act.window.decorView.findViewById<View>(R.id.content) as ViewGroup
        viewGroup.addView(errorView)
      }
    } catch (e: Exception) {
      LogUtil.e(TAG, e)
    }
    return null
  }

  fun addErrorLayout(act: Activity, errorView: View?, topHeight: Int): View? {
    try {
      if (act.window.decorView is ViewGroup) {
        val viewGroup = act.window.decorView.findViewById<View>(R.id.content) as ViewGroup
        val layoutParams = FrameLayout.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT
        )
        layoutParams.topMargin = topHeight
        viewGroup.addView(errorView, layoutParams)
      }
    } catch (e: Exception) {
      LogUtil.e(TAG, e)
    }
    return null
  }

  fun addErrorLayout(fragment: Fragment?, errorView: View?): View? {
    try {
      if (null != fragment && fragment.isAdded && null != fragment.activity) {
        val viewGroup = fragment.view as ViewGroup?
        viewGroup?.addView(errorView)
      }
    } catch (e: Exception) {
      LogUtil.e(TAG, e)
    }
    return null
  }

  fun addErrorLayout(fragment: Fragment?, errorView: View?, topHeight: Int): View? {
    try {
      if (null != fragment && fragment.isAdded && null != fragment.activity) {
        val viewGroup = fragment.view as ViewGroup? ?: return null
        when (viewGroup) {
          is ConstraintLayout -> {
            val layoutParams = ConstraintLayout.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT
            )
            layoutParams.topMargin = topHeight
            viewGroup.addView(errorView, layoutParams)
          }
          is FrameLayout -> {
            val layoutParams = FrameLayout.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT
            )
            layoutParams.topMargin = topHeight
            viewGroup.addView(errorView, layoutParams)
          }
          is RelativeLayout -> {
            val layoutParams = RelativeLayout.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT
            )
            layoutParams.topMargin = topHeight
            viewGroup.addView(errorView, layoutParams)
          }
        }
      }
    } catch (e: Exception) {
      LogUtil.e(TAG, e)
    }
    return null
  }

  fun addErrorLayout(viewGroup: ViewGroup?, errorView: View?): View? {
    try {
      viewGroup?.addView(errorView)
    } catch (e: Exception) {
      LogUtil.e(TAG, e)
    }
    return null
  }

  @JvmStatic
	fun addErrorLayout(viewGroup: ViewGroup?, errorView: View?, topHeight: Int): View? {
    try {
      if (null == viewGroup) {
        return null
      }
      when (viewGroup) {
        is ConstraintLayout -> {
          val layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
          )
          layoutParams.topMargin = topHeight
          viewGroup.addView(errorView, layoutParams)
        }
        is FrameLayout -> {
          val layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
          )
          layoutParams.topMargin = topHeight
          viewGroup.addView(errorView, layoutParams)
        }
        is RelativeLayout -> {
          val layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
          )
          layoutParams.topMargin = topHeight
          viewGroup.addView(errorView, layoutParams)
        }
        is LinearLayout -> {
          val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
          )
          layoutParams.topMargin = topHeight
          viewGroup.addView(errorView, layoutParams)
        }
      }
    } catch (e: Exception) {
      LogUtil.e(TAG, e)
    }
    return null
  }
}