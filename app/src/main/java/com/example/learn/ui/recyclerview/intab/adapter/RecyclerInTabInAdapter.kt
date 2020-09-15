package com.example.learn.ui.recyclerview.intab.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learn.ui.recyclerview.intab.view.RecyclerInTabHorizontal
import com.example.learn.ui.recyclerview.intab.view.RecyclerInTabVertical

class RecyclerInTabInAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VERTICAL = 0
    private val HORIZONTAL = 1

    private val horizontalList: ArrayList<String> by lazy { // 水平数据源
        ArrayList<String>()
    }

    private val verticalList: ArrayList<String> by lazy { // 垂直数据源
        ArrayList<String>()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == HORIZONTAL) {
            return RecyclerInTabHorizontalHolder(RecyclerInTabHorizontal(context))
        } else {
            return RecyclerInTabVerticalHolder(RecyclerInTabVertical(context))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecyclerInTabHorizontalHolder) {
            val itemData = getItem(position)
            holder.item.initData(itemData)
            holder.item.setOnClickListener {
                listener?.itemClick(position, itemData)
            }
        }
        if (holder is RecyclerInTabVerticalHolder) {
            val itemData = getItem(position)
            holder.item.initData(itemData)
            holder.item.setOnClickListener {
                listener?.itemClick(position - horizontalList.size, itemData)
            }
        }
    }

    override fun getItemCount(): Int {
        return horizontalList.size + verticalList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position < horizontalList.size) {
            return HORIZONTAL
        }
        return VERTICAL
    }

    private fun getItem(position: Int): String {
        if (position < horizontalList.size) {
            return horizontalList[position]
        }
        return verticalList[position - horizontalList.size]
    }

    fun updateData(horizontal: ArrayList<String>?, vertical: ArrayList<String>?) {
        horizontalList.clear()
        if (horizontal != null) {
            horizontalList.addAll(horizontal)
        }
        verticalList.clear()
        if (vertical != null) {
            verticalList.addAll(vertical)
        }
        notifyDataSetChanged()
    }

    class RecyclerInTabHorizontalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item = itemView as RecyclerInTabHorizontal
    }

    class RecyclerInTabVerticalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item = itemView as RecyclerInTabVertical
    }

    interface ItemClickListener {
        fun itemClick(position: Int, itemData: String)
    }

    private var listener: ItemClickListener? = null

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

}
