package com.example.partition

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * 分区基类
 */
abstract class BasePartition<T : IPartitionBean>(//上下文
    protected var context: Context
) {

    /**
     * 获取区块数据源
     */
    //分区数据源
    var data: T? = null
        private set

    //同一分区内viewHolder的个数
    protected var mItemList = ArrayList<Int>()

    /**
     * 使用包作用域，防止业务类调用
     */
    /**
     * 使用包作用域，防止业务类调用
     */
    //分区开始位置
    var startCount = 0

    /**
     * 使用包作用域，防止业务类调用
     */
    //分区类型
    var partitionType = 0

    /**
     * 使用包作用域，防止业务类调用
     */
    //分区在partitionList中的位置
    var position = 0

    /**
     * 为区块设置数据源
     * @param data 值可能为null
     */
    fun setData(data: Any?) {
        this.data = data as T?
    }

    /**
     * 使用包作用域，防止业务类调用
     */
    fun getItemList(): List<Int> {
        return mItemList
    }

    /**
     * 使用包作用域，防止业务类调用
     */
    fun setItemList() {
        mItemList.clear()
        getItemSize()
    }

    abstract fun getSpanSize(position: Int): Int
    abstract fun getItemViewTypeBean(position: Int): ItemViewTypeBean
    abstract fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder

    abstract fun onBindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        position: Int
    )

    abstract fun getItemSize()

}