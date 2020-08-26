package com.example.learn.ui.partition

import android.content.Context
import com.example.learn.ui.partition.partition.PartitionFactory
import com.example.partition.BasePartition
import com.example.partition.BasePartitionAdapter
import com.example.partition.IPartitionBean

class PartitionAdapter(
    private val context: Context, //上下文
    private val callback: IPartitionCallback //回调
) : BasePartitionAdapter() {

    override fun createPartition(bean: IPartitionBean): BasePartition<*>? {
        return PartitionFactory.createPartition(context, bean, callback)
    }

}