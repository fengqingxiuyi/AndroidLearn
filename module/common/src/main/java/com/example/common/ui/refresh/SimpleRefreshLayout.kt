package com.example.common.ui.refresh

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.common.R
import com.example.common.databinding.LayoutSimpleRefreshHeaderBinding
import com.example.utils.ext.dp
import com.example.utils.ext.string
import com.scwang.smart.refresh.layout.SmartRefreshLayout

open class SimpleRefreshLayout : FrameLayout {

  companion object {
    /**
     * @param fullScreen Boolean false 底部自动隐藏(内容不满一屏) true 底部跟随内容(内容超过一屏)
     */
    fun showNoMore(layout: SmartRefreshLayout, text: String = string(R.string.common_footer_no_more), fullScreen: Boolean = true) {
      layout.setEnableFooterFollowWhenNoMoreData(fullScreen)
      val footer = layout.refreshFooter as SimpleRefreshFooter
      footer.view.showNoMore(true, text)
    }

    /**
     * 下拉刷新的时候重置状态
     */
    fun resetNoMore(layout: SmartRefreshLayout) {
      layout.setNoMoreData(false) //没有更多数据了状态重置为loading状态
      layout.setEnableFooterFollowWhenNoMoreData(false) //底部自动隐藏(内容不满一屏)
      val footer = layout.refreshFooter as SimpleRefreshFooter
      footer.view.showNoMore(false)
    }
  }

  private var binding: LayoutSimpleRefreshHeaderBinding =
    LayoutSimpleRefreshHeaderBinding.inflate(LayoutInflater.from(context), this)

  constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs, 0) {
    init()
  }

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    init()
  }

  private fun init() {
    setBackgroundColor(Color.TRANSPARENT)
    setPadding(0, 23.dp, 0, 23.dp)
  }

  fun updatePadding(paddingTop: Int, paddingBottom: Int) {
    setPadding(0, paddingTop, 0, paddingBottom)
  }

  fun showNoMore(show: Boolean, text: String?) {
    showNoMore(show)
    setNoMore(text)
  }

  fun showNoMore(show: Boolean) {
    if (show) {
      binding.tvNoMore.visibility = View.VISIBLE
      binding.pBarRefresh.visibility = View.GONE
    } else {
      binding.tvNoMore.visibility = View.GONE
      binding.pBarRefresh.visibility = View.VISIBLE
    }
  }

  fun setNoMore(text: String?) {
    binding.tvNoMore.text = text
  }

}