package com.example.learn.ui.appbarlayout

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class AppbarLayoutRecyclerAdapter(var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: MutableList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(TextView(context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder2 = holder as ViewHolder
        holder2.textView.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData() {
        list.clear()
        list.add("111111111")
        list.add("22222222")
        list.add("33333333")
        list.add("4444444444")
        list.add("55555555")
        list.add("66666666")
        list.add("77777777")
        list.add("88888888")
        list.add("111111111")
        list.add("22222222")
        list.add("33333333")
        list.add("4444444444")
        list.add("55555555")
        list.add("66666666")
        list.add("77777777")
        list.add("88888888")
        list.add("111111111")
        list.add("22222222")
        list.add("33333333")
        list.add("4444444444")
        list.add("55555555")
        list.add("66666666")
        list.add("77777777")
        list.add("88888888")
        list.add("111111111")
        list.add("22222222")
        list.add("33333333")
        list.add("4444444444")
        list.add("55555555")
        list.add("66666666")
        list.add("77777777")
        list.add("88888888")
        list.add("111111111")
        list.add("22222222")
        list.add("33333333")
        list.add("4444444444")
        list.add("55555555")
        list.add("66666666")
        list.add("77777777")
        list.add("88888888")
        list.add("111111111")
        list.add("22222222")
        list.add("33333333")
        list.add("4444444444")
        list.add("55555555")
        list.add("66666666")
        list.add("77777777")
        list.add("88888888")
        list.add("111111111")
        list.add("22222222")
        list.add("33333333")
        list.add("4444444444")
        list.add("55555555")
        list.add("66666666")
        list.add("77777777")
        list.add("88888888")
        notifyDataSetChanged()
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView = itemView as TextView
    }

}