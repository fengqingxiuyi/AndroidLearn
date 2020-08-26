package com.example.learn.ui.partition.partition.two

import com.example.partition.IPartitionBean

/**
 * 一行两个item的区块的数据结构
 */
class TwoBean : IPartitionBean {

    var lineHeight = 0

    var dataBean: List<DataBean>? = null

    class DataBean {
        var text: String? = null
    }

    override fun getPartitionType(): Int {
        return 2
    }
}