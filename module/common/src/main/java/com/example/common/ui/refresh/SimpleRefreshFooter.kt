package com.example.common.ui.refresh

import android.content.Context
import android.util.AttributeSet
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

class SimpleRefreshFooter : SimpleRefreshLayout, RefreshFooter {
  constructor(context: Context) : super(context, null, 0)

  constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs, 0)

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {

  }

  override fun getView(): SimpleRefreshLayout {
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

  /**
   * todo 下拉刷新时调用finishRefresh会触发日志："NoMoreData is not supported.(不支持NoMoreData，请使用[ClassicsFooter]或者[自定义Footer并实现setNoMoreData方法且返回true]"
   */
  override fun setNoMoreData(noMoreData: Boolean): Boolean {
    return noMoreData
  }
}