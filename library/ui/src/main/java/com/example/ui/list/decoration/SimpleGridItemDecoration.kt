package com.example.ui.list.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 无颜色分割线，item的左、右间距为1倍间距，上为两倍间距，下无间距
 *
 * @author: shenbf
 */
class SimpleGridItemDecoration(private val leftRight: Int, private val topBottom: Int) :
  RecyclerView.ItemDecoration() {

  override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    super.onDraw(c, parent, state)
  }

  override fun getItemOffsets(
    outRect: Rect,
    view: View,
    parent: RecyclerView,
    state: RecyclerView.State
  ) {
    val layoutManager = parent.layoutManager as GridLayoutManager? ?: return
    val lp = view.layoutParams as GridLayoutManager.LayoutParams
    val childPosition = parent.getChildAdapterPosition(view)
    val spanCount = layoutManager.spanCount
    if (layoutManager.orientation == GridLayoutManager.VERTICAL) {
//      //判断是否在第一排
//      if (layoutManager.spanSizeLookup.getSpanGroupIndex(
//          childPosition,
//          spanCount
//        ) == 0
//      ) { //第一排的需要上面
//        outRect.top = topBottom
//      }
//      outRect.bottom = topBottom
      outRect.top = topBottom * 2 //item
      //这里忽略和合并项的问题，只考虑占满和单一的问题
      if (lp.spanSize == spanCount) { //占满
        outRect.left = leftRight
        outRect.right = leftRight
      } else {
        outRect.left = ((spanCount - lp.spanIndex).toFloat() / spanCount * leftRight).toInt()
        outRect.right = (leftRight.toFloat() * (spanCount + 1) / spanCount - outRect.left).toInt()
      }
    } else {
      if (layoutManager.spanSizeLookup.getSpanGroupIndex(
          childPosition,
          spanCount
        ) == 0
      ) { //第一排的需要left
        outRect.left = leftRight
      }
      outRect.right = leftRight
      //这里忽略和合并项的问题，只考虑占满和单一的问题
      if (lp.spanSize == spanCount) { //占满
        outRect.top = topBottom
        outRect.bottom = topBottom
      } else {
        outRect.top = ((spanCount - lp.spanIndex).toFloat() / spanCount * topBottom).toInt()
        outRect.bottom = (topBottom.toFloat() * (spanCount + 1) / spanCount - outRect.top).toInt()
      }
    }
  }

}