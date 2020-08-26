package com.example.learn.ui.partition.partition

import android.content.Context
import com.example.learn.ui.partition.IPartitionCallback
import com.example.learn.ui.partition.partition.one.OnePartition
import com.example.learn.ui.partition.partition.two.TwoPartition
import com.example.partition.BasePartition
import com.example.partition.IPartitionBean

/**
 * 区块创建类
 */
object PartitionFactory {
    /**
     * 创建分区
     */
    fun createPartition(
        context: Context,
        bean: IPartitionBean,
        callback: IPartitionCallback?
    ): BasePartition<*>? {
        return when (bean.getPartitionType()) {
            1 -> OnePartition(context, callback)
            2 -> TwoPartition(context, callback)
            else -> null
        }
    }
}