package com.example.utils.device

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.*
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import com.example.utils.other.Utils
import kotlin.math.abs

object KeyboardUtils {
  private const val TAG_ON_GLOBAL_LAYOUT_LISTENER = -8

  /**
   * Show the soft input.
   */
  fun showSoftInput() {
    val imm = Utils.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
  }

  /**
   * Show the soft input.
   */
  fun showSoftInput(activity: Activity) {
    if (!isSoftInputVisible(activity)) {
      toggleSoftInput()
    }
  }
  /**
   * Show the soft input.
   *
   * @param view  The view.
   * @param flags Provides additional operating flags.  Currently may be
   * 0 or have the [InputMethodManager.SHOW_IMPLICIT] bit set.
   */
  /**
   * Show the soft input.
   *
   * @param view The view.
   */
  @JvmOverloads
  fun showSoftInput(view: View, flags: Int = 0) {
    val imm = Utils.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    view.isFocusable = true
    view.isFocusableInTouchMode = true
    view.requestFocus()
    imm.showSoftInput(view, flags, object : ResultReceiver(Handler()) {
      override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN
            || resultCode == InputMethodManager.RESULT_HIDDEN) {
          toggleSoftInput()
        }
      }
    })
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
  }

  /**
   * Hide the soft input.
   *
   * @param activity The activity.
   */
  fun hideSoftInput(activity: Activity) {
    hideSoftInput(activity.window)
  }

  /**
   * Hide the soft input.
   *
   * @param window The window.
   */
  fun hideSoftInput(window: Window) {
    var view = window.currentFocus
    if (view == null) {
      val decorView = window.decorView
      val focusView = decorView.findViewWithTag<View>("keyboardTagView")
      if (focusView == null) {
        view = EditText(window.context)
        view.setTag("keyboardTagView")
        (decorView as ViewGroup).addView(view, 0, 0)
      } else {
        view = focusView
      }
      view.requestFocus()
    }
    hideSoftInput(view)
  }

  /**
   * Hide the soft input.
   *
   * @param view The view.
   */
  fun hideSoftInput(view: View) {
    val imm = Utils.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
  }

  private var millis: Long = 0

  /**
   * Hide the soft input.
   *
   * @param activity The activity.
   */
  fun hideSoftInputByToggle(activity: Activity) {
    val nowMillis = SystemClock.elapsedRealtime()
    val delta = nowMillis - millis
    if (abs(delta) > 500 && isSoftInputVisible(activity)) {
      toggleSoftInput()
    }
    millis = nowMillis
  }

  /**
   * Toggle the soft input display or not.
   */
  fun toggleSoftInput() {
    val imm = Utils.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(0, 0)
  }

  private var sDecorViewDelta = 0

  /**
   * Return whether soft input is visible.
   *
   * @param activity The activity.
   * @return `true`: yes<br></br>`false`: no
   */
  fun isSoftInputVisible(activity: Activity): Boolean {
    return getDecorViewInvisibleHeight(activity.window) > 0
  }

  private fun getDecorViewInvisibleHeight(window: Window): Int {
    val decorView = window.decorView
    val outRect = Rect()
    decorView.getWindowVisibleDisplayFrame(outRect)
    Log.d("KeyboardUtils", "getDecorViewInvisibleHeight: " + (decorView.bottom - outRect.bottom))
    val delta = abs(decorView.bottom - outRect.bottom)
    if (delta <= navBarHeight + statusBarHeight) {
      sDecorViewDelta = delta
      return 0
    }
    return delta - sDecorViewDelta
  }

  /**
   * Register soft input changed listener.
   *
   * @param activity The activity.
   * @param listener The soft input changed listener.
   */

  fun registerSoftInputChangedListener(activity: Activity, listener: (t: Int) -> Unit) {
    registerSoftInputChangedListener(activity.window, listener)
  }

  /**
   * Register soft input changed listener.
   *
   * @param window   The window.
   * @param listener The soft input changed listener.
   */
  fun registerSoftInputChangedListener(window: Window, listener: (t: Int) -> Unit) {
    val flags = window.attributes.flags
    if (flags and WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS != 0) {
      window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
    val contentView: FrameLayout = window.findViewById(android.R.id.content)
    val decorViewInvisibleHeightPre = intArrayOf(getDecorViewInvisibleHeight(window))
    val onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
      val height = getDecorViewInvisibleHeight(window)
      if (decorViewInvisibleHeightPre[0] != height) {
        listener.invoke(height)
        decorViewInvisibleHeightPre[0] = height
      }
    }
    contentView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    contentView.setTag(TAG_ON_GLOBAL_LAYOUT_LISTENER, onGlobalLayoutListener)
  }

  /**
   * Unregister soft input changed listener.
   *
   * @param window The window.
   */
  fun unregisterSoftInputChangedListener(window: Window) {
    val contentView = window.findViewById<View>(android.R.id.content) ?: return
    val tag = contentView.getTag(TAG_ON_GLOBAL_LAYOUT_LISTENER)
    if (tag is ViewTreeObserver.OnGlobalLayoutListener) {
      contentView.viewTreeObserver.removeOnGlobalLayoutListener(tag)
    }
  }

  private fun getContentViewInvisibleHeight(window: Window): Int {
    val contentView = window.findViewById<View>(android.R.id.content) ?: return 0
    val outRect = Rect()
    contentView.getWindowVisibleDisplayFrame(outRect)
    Log.d("KeyboardUtils", "getContentViewInvisibleHeight: "
        + (contentView.bottom - outRect.bottom))
    val delta = Math.abs(contentView.bottom - outRect.bottom)
    return if (delta <= statusBarHeight + navBarHeight) {
      0
    } else delta
  }

  /**
   * Fix the leaks of soft input.
   *
   * @param activity The activity.
   */
  fun fixSoftInputLeaks(activity: Activity) {
    fixSoftInputLeaks(activity.window)
  }

  /**
   * Fix the leaks of soft input.
   *
   * @param window The window.
   */
  fun fixSoftInputLeaks(window: Window) {
    val imm = Utils.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val leakViews = arrayOf("mLastSrvView", "mCurRootView", "mServedView", "mNextServedView")
    for (leakView in leakViews) {
      try {
        val leakViewField = InputMethodManager::class.java.getDeclaredField(leakView)
        if (!leakViewField.isAccessible) {
          leakViewField.isAccessible = true
        }
        val obj = leakViewField[imm] as? View ?: continue
        if (obj.rootView === window.decorView.rootView) {
          leakViewField[imm] = null
        }
      } catch (ignore: Throwable) { /**/
      }
    }
  }

  val statusBarHeight: Int
    get() {
      val resources: Resources = Utils.context.resources
      val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
      return resources.getDimensionPixelSize(resourceId)
    }

  /**
   * Return the navigation bar's height.
   *
   * @return the navigation bar's height
   */
  private val navBarHeight: Int
    get() {
      val res: Resources = Utils.context.resources
      val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
      return if (resourceId != 0) {
        res.getDimensionPixelSize(resourceId)
      } else {
        0
      }
    }

}