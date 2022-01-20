package com.example.common.ui.refresh

import android.view.ViewTreeObserver
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * 功能：
 * ① 当RecyclerView列表内容不满一屏时自动请求下一页数据的监听器
 * ② 使用SmartRefreshLayout如果内容不满一屏则自动收回没有更多内容的底部
 *
 * @author: fqxyi
 */
class RVContentLessScreenGlobalListener(
  private val recyclerView: RecyclerView,
  private val smartRefreshLayout: SmartRefreshLayout,
  private val hasEmptyView: Boolean = true,
  private val request: (() -> Unit)?
) : ViewTreeObserver.OnGlobalLayoutListener {

  //过滤 之后的重复调用的情况
  var lastPosition: Int = -1

  override fun onGlobalLayout() {
    val layoutManager = recyclerView.layoutManager
    if (layoutManager !is LinearLayoutManager) return
    val adapter = recyclerView.adapter ?: return
    //
    val last = layoutManager.findLastCompletelyVisibleItemPosition()
    //过滤 之后的重复调用的情况、数据未加载完成时last为-1的情况
    if (lastPosition == last || last == -1) return
    //过滤 设置了缺省页之后last为0的情况
    if (hasEmptyView && last == 0) return
    //
    if (last < adapter.itemCount - 1) {
      remove()
    } else { //数据未满一页
      //如果内容不满一屏则自动收回没有更多内容的底部
      smartRefreshLayout.setEnableFooterFollowWhenNoMoreData(false)
      //请求下一页
      request?.invoke()
    }
    //
    lastPosition = last
  }

  fun add() {
    recyclerView.viewTreeObserver.addOnGlobalLayoutListener(this)
  }

  fun remove() {
    recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
  }
}