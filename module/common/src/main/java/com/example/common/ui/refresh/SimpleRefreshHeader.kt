package com.example.common.ui.refresh

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

class SimpleRefreshHeader : SimpleRefreshLayout, RefreshHeader {

  constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
  }

  override fun getView(): View {
    return this
  }

  override fun getSpinnerStyle(): SpinnerStyle {
    return SpinnerStyle.Translate
  }

  override fun setPrimaryColors(vararg colors: Int) {
  }

  override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
  }

  override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {
  }

  override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
  }

  override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
  }

  override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
    return 1000
  }

  override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
  }

  override fun isSupportHorizontalDrag(): Boolean {
    return false
  }
}