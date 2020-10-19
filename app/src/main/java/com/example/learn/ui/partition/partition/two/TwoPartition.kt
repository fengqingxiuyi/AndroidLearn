package com.example.learn.ui.partition.partition.two

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learn.ui.partition.IPartitionCallback
import com.example.learn.ui.partition.partition.line.LineViewHolder
import com.example.partition.BasePartition
import com.example.partition.ISpanSize
import com.example.partition.ItemViewTypeBean

/**
 * 分区类型2
 */
class TwoPartition(context: Context, private val callback: IPartitionCallback?) :
    BasePartition<TwoBean>(context) {

    override fun getSpanSize(position: Int): Int {
        return if (position == mItemList.size - 1) {
            ISpanSize.ONE
        } else ISpanSize.ONE_TWO
    }

    override fun getItemViewTypeBean(position: Int): ItemViewTypeBean {
        val bean = ItemViewTypeBean()
        if (position == mItemList.size - 1) {
            bean.itemViewType = 0
            bean.repeat = false
        } else {
            bean.itemViewType = partitionType
            bean.repeat = true
        }
        return bean
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            LineViewHolder(View(context))
        } else TwoViewHolder(TwoView(context))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is TwoViewHolder) {
            viewHolder.twoView.initData(data?.dataBean?.get(position), callback, position)
        }
        if (viewHolder is LineViewHolder) {
            viewHolder.initData(data?.lineHeight, Color.BLUE)
        }
    }

    override fun getItemSize() {
        mItemList.add(position)
        mItemList.add(position)
        mItemList.add(position)
    }

}