package com.example.learn.ui.recyclerview.intab.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learn.ui.recyclerview.intab.bean.DataBean
import com.example.learn.ui.recyclerview.intab.callback.RecyclerInTabCallback
import com.example.learn.ui.recyclerview.intab.view.RecyclerInTabHead
import com.example.learn.ui.recyclerview.intab.view.RecyclerInTabPager

class RecyclerInTabOutAdapter(private val context: Context, private val callback: RecyclerInTabCallback?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HEAD = 0
    private val PAGER = 1

    private var data: DataBean? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == HEAD) {
            return RecyclerInTabHeadHolder(RecyclerInTabHead(context))
        } else {
            val pager = RecyclerInTabPager(context)
            pager.setRecyclerInTabCallback(callback)
            return RecyclerInTabPagerHolder(pager)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecyclerInTabHeadHolder) {
            holder.item.initData(data?.head)
            holder.item.setOnClickListener {

            }
        }
        if (holder is RecyclerInTabPagerHolder) {
            holder.item.initData(data?.pager)
        }
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1) {
            return PAGER
        }
        return HEAD
    }

    fun updateData(data: DataBean?) {
        if (data == null) return
        this.data = data
        notifyDataSetChanged()
    }

    class RecyclerInTabHeadHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item = itemView as RecyclerInTabHead
    }

    class RecyclerInTabPagerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item = itemView as RecyclerInTabPager
    }

}
