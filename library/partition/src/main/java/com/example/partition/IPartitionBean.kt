package com.example.partition

/**
 * 每个分区的数据结构必须要实现IPartitionBean，否则分区组件无法得知具体的类型
 */
interface IPartitionBean {
    fun getPartitionType(): Int
}