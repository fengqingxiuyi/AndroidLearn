package com.example.ui.list.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 无颜色分割线
 *
 * @author: shenbf
 */
class SimpleLinearItemDecoration(private val leftRight: Int, private val topBottom: Int) :
  RecyclerView.ItemDecoration() {

  override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    super.onDraw(c, parent, state)
  }

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
    val layoutManager = parent.layoutManager as LinearLayoutManager? ?: return
    //竖直方向的
    if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
      //最后一项需要 bottom
      if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
        outRect.bottom = topBottom
      }
      outRect.top = topBottom
      outRect.left = leftRight
      outRect.right = leftRight
    } else {
      //最后一项需要right
      if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
        outRect.right = leftRight
      }
      outRect.top = topBottom
      outRect.left = leftRight
      outRect.bottom = topBottom
    }
  }

}