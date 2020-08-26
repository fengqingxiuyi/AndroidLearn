package com.example.learn.ui.partition.utils

import com.example.learn.ui.partition.partition.one.OneBean
import com.example.learn.ui.partition.partition.two.TwoBean
import com.example.partition.IPartitionBean
import java.util.*

/**
 * 工具类
 */
object Util {

    /**
     * 创建假数据
     */
    fun getData(): List<IPartitionBean> {
        val list: MutableList<IPartitionBean> = ArrayList()
        for (i in 0..19) {
            if (i % 2 == 0) {
                // add one
                val oneBean = OneBean()
                val dataBeanList: MutableList<OneBean.DataBean> = ArrayList()
                val dataBean = OneBean.DataBean()
                dataBean.text = "分区1数据$i-1"
                dataBeanList.add(dataBean)
                oneBean.dataBean = dataBeanList
                oneBean.lineHeight = 10
                list.add(oneBean)
            } else {
                // add two
                val twoBean = TwoBean()
                val dataBeanList: MutableList<TwoBean.DataBean> = ArrayList()
                val dataBean1 = TwoBean.DataBean()
                dataBean1.text = "分区2数据$i-1"
                dataBeanList.add(dataBean1)
                val dataBean2 = TwoBean.DataBean()
                dataBean2.text = "分区2数据$i-2"
                dataBeanList.add(dataBean2)
                twoBean.dataBean = dataBeanList
                twoBean.lineHeight = 20
                list.add(twoBean)
            }
        }
        return list
    }
}