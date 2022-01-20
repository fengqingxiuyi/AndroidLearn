package com.example.utils.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.AnimationDrawable
import android.util.TypedValue
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.utils.device.ScreenUtils
import com.example.utils.other.Utils

fun View.setVisible(visible: Boolean) {
    if (visible) visible() else gone()
}

fun View.visible() {
    if (!isVisible()) {
        visibility = View.VISIBLE
    }
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    if (!isGone()) {
        visibility = View.GONE
    }
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun View.isInvisible(): Boolean {
    return visibility == View.INVISIBLE
}

fun View.isGone(): Boolean {
    return visibility == View.GONE
}

/**
 * Visible if condition met
 */
inline fun View.visibleIf(block: () -> Boolean) {
    if (visibility != View.VISIBLE && block()) {
        visible()
    }
}


/**
 * Visible if condition met else gone
 */

inline fun View.visibleIfElseGone(block: () -> Boolean) {
    if (block()) {
        visible()
    } else {
        gone()
    }
}

/**
 * Invisible if condition met
 */

inline fun View.invisibleIf(block: () -> Boolean) {
    if (visibility != View.INVISIBLE && block()) {
        invisible()
    }
}

/**
 * Invisible if condition met
 */

inline fun View.invisibleIfElseVisible(block: () -> Boolean) {
    if (block()) {
        invisible()
    } else {
        visible()
    }
}


/**
 * Gone if condition met
 */
inline fun View.goneIf(block: () -> Boolean) {
    if (visibility != View.GONE && block()) {
        gone()
    }
}


/**
 * Gone if condition met
 */
inline fun View.goneIfElseVisible(block: () -> Boolean) {
    if (block()) {
        if (visibility != View.GONE) {
            gone()
        }
    } else {
        visible()
    }
}
var Int.dp: Int
    get() {
        val metrics = Utils.context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
    }

    set(_) {}


var Float.dp: Float
    get() {
        val metrics = Utils.context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, metrics)
    }
    set(_) {}

var Int.sp: Int
    get() {
        val metrics = Utils.context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), metrics).toInt()
    }
    set(_) {}


var Float.sp: Float
    get() {
        val metrics = Utils.context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, metrics)
    }
    set(_) {}

fun View.color(resourceId: Int): Int {
    return ContextCompat.getColor(context, resourceId)
}

/**
 * Shows the soft input for the vindow.
 */
fun View.showSoftInput() {
    context.getSystemService(Context.INPUT_METHOD_SERVICE).let { it as InputMethodManager }.showSoftInput(this, 0)
}

/**
 * Hides the soft input for the vindow.
 */
fun View.hideSoftInput() {
    context.getSystemService(Context.INPUT_METHOD_SERVICE).let { it as InputMethodManager }
        .hideSoftInputFromWindow(this.applicationWindowToken, 0)
}

/**
 * 扩大点击区域
 */
fun View.expandClickArea(expandSize: Int) {
    expandClickArea(expandSize, expandSize, expandSize, expandSize)
}

/**
 * 扩大点击区域
 */
fun View.expandClickArea(
    expandSizeTop: Int=0,
    expandSizeLeft: Int=0,
    expandSizeRight: Int=0,
    expandSizeBottom: Int=0
) {
    val parentView = parent as? View ?: return
    parentView.post {
        val rect = Rect()
        getHitRect(rect)
        rect.top -= expandSizeTop
        rect.bottom += expandSizeBottom
        rect.left -= expandSizeLeft
        rect.right += expandSizeRight
        parentView.touchDelegate = TouchDelegate(rect, this)
    }
}

fun View.resetFocus() {
    clearFocus()
    isFocusableInTouchMode = false
    isFocusable = false
    isFocusableInTouchMode = true
    isFocusable = true
}

fun View?.cancelFocus() {
    if (this != null) {
        val viewParent = parent
        if (viewParent is ViewGroup) {
            viewParent.isFocusable = true
            viewParent.isFocusableInTouchMode = true
            viewParent.requestFocus()
        }
    }
}

@SuppressLint("ClickableViewAccessibility")
inline fun View.setOnTouchDownListener(crossinline onSingleTap: (v: View, event: MotionEvent) -> Boolean) {
    setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> onSingleTap(v, event)
            else -> false
        }
    }
}

/**
 * Hides the soft input for the vindow.
 */
fun Activity.translationLogin(parent:ViewGroup, softHeight:Int, button:View) {
    if (softHeight > ScreenUtils.screenHeight - button.bottom) {
        parent.translationY = (ScreenUtils.screenHeight - button.bottom - softHeight -button.height).toFloat()
    }else{
        parent.translationY = 0f
    }
}

fun ImageView.startAnim(animId: Int) {
    setImageResource(animId)
    val animDrawable = drawable as AnimationDrawable
    animDrawable.start()
}