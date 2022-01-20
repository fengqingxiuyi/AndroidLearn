package com.example.ui.tablayout

import android.animation.ArgbEvaluator
import android.graphics.Typeface
import android.util.Log
import android.util.TypedValue
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2

/**
 * 略
 *
 * @author: shenbf
 */
class SimpleTabTextChangeCallback(
  private var fontMaxPx: Float,
  private var fontMinPx: Float,
  private var colorNormal: Int,
  private var colorSelect: Int,
  private var viewNormalDefault: TextView,
  private var viewSelectDefault: TextView,
  private var onPageSelected: (callback: SimpleTabTextChangeCallback, position: Int) -> Unit
) : ViewPager2.OnPageChangeCallback() {
  
  private val TAG = "TabTextChangeCallback"
  private var argbEvaluator = ArgbEvaluator()
  private var lastPositionOffset = 0f
  private var fontDiff = fontMaxPx - fontMinPx
  private var byDrag = false //避免第一次进入页面出现的动画效果
  
  override fun onPageScrollStateChanged(state: Int) {
    when (state) {
      ViewPager2.SCROLL_STATE_IDLE -> { //停止滑动，保证字体大小无误
        byDrag = false
        if (viewNormalDefault.textSize > viewSelectDefault.textSize) {
          viewNormalDefault.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontMaxPx)
          viewSelectDefault.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontMinPx)
        } else {
          viewNormalDefault.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontMinPx)
          viewSelectDefault.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontMaxPx)
        }
      }
      ViewPager2.SCROLL_STATE_DRAGGING -> { //拖拽中
        byDrag = true
      }
      ViewPager2.SCROLL_STATE_SETTLING -> { //惯性滑动中

      }
    }
  }
  override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    if (!byDrag || positionOffset <= 0 || positionOffset > 1) return //过滤异常数据
    if (positionOffset > lastPositionOffset) { //从右往左滑动
      viewNormalDefault.setTextColor(argbEvaluator.evaluate(positionOffset, colorSelect, colorNormal) as Int)
      viewNormalDefault.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontMaxPx - fontDiff * positionOffset)
      viewSelectDefault.setTextColor(argbEvaluator.evaluate(positionOffset, colorNormal, colorSelect) as Int)
      viewSelectDefault.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontMinPx + fontDiff * positionOffset)
    } else { //从左往右滑动
      viewNormalDefault.setTextColor(argbEvaluator.evaluate(1 - positionOffset, colorNormal, colorSelect) as Int)
      viewNormalDefault.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontMinPx + fontDiff * (1 - positionOffset))
      viewSelectDefault.setTextColor(argbEvaluator.evaluate(1 - positionOffset, colorSelect, colorNormal) as Int)
      viewSelectDefault.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontMaxPx - fontDiff * (1 - positionOffset))
    }
    lastPositionOffset = positionOffset
    Log.i(TAG, "textSize = ${fontMaxPx - fontDiff * positionOffset}, positionOffset = $positionOffset, positionOffsetPixels = $positionOffsetPixels")
  }
  override fun onPageSelected(position: Int) {
    onPageSelected.invoke(this@SimpleTabTextChangeCallback, position)
  }

  fun onTabSelect() {
    setTabSelect(viewNormalDefault)
    setTabUnSelect(viewSelectDefault)
    //
    (viewNormalDefault.layoutParams as ConstraintLayout.LayoutParams).apply {
      baselineToBaseline = ConstraintLayout.LayoutParams.UNSET
      bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
    }
    (viewSelectDefault.layoutParams as ConstraintLayout.LayoutParams).apply {
      baselineToBaseline = viewNormalDefault.id
      bottomToBottom = ConstraintLayout.LayoutParams.UNSET
    }
  }

  fun onTabUnSelect() {
    setTabSelect(viewSelectDefault)
    setTabUnSelect(viewNormalDefault)
    //
    (viewNormalDefault.layoutParams as ConstraintLayout.LayoutParams).apply {
      baselineToBaseline = viewSelectDefault.id
      bottomToBottom = ConstraintLayout.LayoutParams.UNSET
    }
    (viewSelectDefault.layoutParams as ConstraintLayout.LayoutParams).apply {
      baselineToBaseline = ConstraintLayout.LayoutParams.UNSET
      bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
    }
  }

  private fun setTabSelect(textView: TextView) {
    textView.apply {
      setTextSize(TypedValue.COMPLEX_UNIT_PX, fontMaxPx)
      setTextColor(colorSelect)
      typeface = Typeface.defaultFromStyle(Typeface.BOLD)
    }
  }

  private fun setTabUnSelect(textView: TextView) {
    textView.apply {
      setTextSize(TypedValue.COMPLEX_UNIT_PX, fontMinPx)
      setTextColor(colorNormal)
      typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
    }
  }
}