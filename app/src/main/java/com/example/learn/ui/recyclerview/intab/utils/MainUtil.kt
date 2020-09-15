package com.example.learn.ui.recyclerview.intab.utils

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.learn.ui.recyclerview.intab.bean.DataBean
import com.example.learn.ui.recyclerview.intab.fragment.RecyclerInTabFragment

class MainUtil {

    companion object {

        fun getHorizontalList(): ArrayList<String> {
            val list = ArrayList<String>()
            for (i in 1..10) {
                list.add("水平数据$i")
            }
            return list
        }

        fun getVerticalList(): ArrayList<String> {
            val list = ArrayList<String>()
            for (i in 1..10) {
                list.add("垂直数据$i")
            }
            return list
        }

        fun getDataBean(): DataBean {
            val data = DataBean()
            val head = DataBean().HeadBean()
            head.text = "我是Header"
            data.head = head
            val pager = DataBean().PagerBean()
            val fragmentList = ArrayList<Fragment>()
            fragmentList.add(RecyclerInTabFragment())
            fragmentList.add(RecyclerInTabFragment())
            fragmentList.add(RecyclerInTabFragment())
            fragmentList.add(RecyclerInTabFragment())
            fragmentList.add(RecyclerInTabFragment())
            fragmentList.add(RecyclerInTabFragment())
            fragmentList.add(RecyclerInTabFragment())
            fragmentList.add(RecyclerInTabFragment())
            fragmentList.add(RecyclerInTabFragment())
            fragmentList.add(RecyclerInTabFragment())
            pager.list = fragmentList
            data.pager = pager
            return data
        }

        /**
         * 获取状态栏高度
         */
        fun getStatusBarHeight(context: Context?): Int {
            try {
                val resourceId = context?.resources?.getIdentifier("status_bar_height", "dimen", "android")?:0
                if (resourceId == 0) return 0
                return context?.resources?.getDimensionPixelSize(resourceId)?:0
            } catch (e: Exception) {
                return 0
            }
        }

        /**
         * 将dip或dp值转换为px值，保证尺寸不变
         */
        fun dip2px(context: Context?, dipValue: Float): Int {
            val density = context?.resources?.displayMetrics?.density?:0f
            return (dipValue * density + 0.5f).toInt()
        }

    }

}