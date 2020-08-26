package com.example.learn.ui.partition.partition.one

import com.example.partition.IPartitionBean

/**
 * 一行一个item的区块的数据结构
 */
class OneBean : IPartitionBean {

    var lineHeight = 0

    var dataBean: List<DataBean>? = null

    class DataBean {
        var text: String? = null
    }

    override fun getPartitionType(): Int {
        return 1
    }
}