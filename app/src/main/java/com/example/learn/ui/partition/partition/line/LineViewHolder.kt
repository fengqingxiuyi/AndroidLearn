package com.example.learn.ui.partition.partition.line

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.utils.device.DensityUtil

/**
 * 分割线区块
 */
class LineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val context: Context = itemView.context

    init {
        //分割线默认间距为10dp
        itemView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dp2px(context, 10f)
        )
        //分割线默认颜色为红色
        itemView.setBackgroundColor(Color.parseColor("#f6f6f6"))
    }

    /**
     * 自定义分割线样式
     */
    fun initData(height: Int?, color: Int = Color.parseColor("#f6f6f6")) {
        itemView.layoutParams.height = DensityUtil.dp2px(context, height?.toFloat() ?: 0f)
        itemView.setBackgroundColor(color)
    }
}