package com.example.learn.ui.recyclerview.intab.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class RecyclerInTabPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val titleList = arrayOf<String>("Tab1", "Tab2", "Tab3", "Tab4", "Tab5", "Tab6", "Tab7", "Tab8", "Tab9", "Tab10")

    private val dataList: ArrayList<Fragment> by lazy { // 数据源
        ArrayList<Fragment>()
    }

    override fun getItem(position: Int): Fragment {
        return dataList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

    override fun getCount(): Int {
        return dataList.size
    }

    fun updateData(data: ArrayList<Fragment>?) {
        dataList.clear()
        dataList.addAll(data?:ArrayList<Fragment>())
        notifyDataSetChanged()
    }

}