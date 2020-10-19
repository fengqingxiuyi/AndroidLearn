package com.example.partition

import android.util.SparseIntArray
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * 适配器基类
 */
abstract class BasePartitionAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * 分区集合，根据数据源list创建，大小和数据源一样；
     * 目的是为了减少内存占用，不创建相同数据的重复分区；
     * partitionPosition是partitionList的index。
     */
    private val partitionList by lazy {
        ArrayList<BasePartition<*>>()
    }

    /**
     * 缓存viewType和partitionPosition的关系，key为viewType，value为partitionPosition；
     * 目的是在onCreateViewHolder方法中能够获取到partitionPosition。
     */
    private val viewTypePositionArr by lazy {
        SparseIntArray()
    }

    /**
     * 集合的大小为getItemCount的返回值；
     * totalItemList的大小由业务类决定；
     * position是totalItemList的index。
     */
    private val totalItemList by lazy {
        ArrayList<Int>()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val partitionPosition = totalItemList[position]
                    verifyPartitionPosition(partitionPosition)
                    val partition = partitionList[partitionPosition]
                    return partition.getSpanSize(getPartitionInnerPosition(position, partition))
                }
            }
        }
    }

    /**
     * 分区是否可复用由repeat决定；
     * 默认规则（只是一个说法并没有在代码中限制）：相同分区可复用，同一分区内新增的Header或Footer不可复用。
     * 举个例子：
     * 0 1      2 3      4 5      6 7      参数position
     * 1 0      2 0      3 0      1 0      业务类定义的viewType
     * 1 1+100  2 3+100  3 5+100  1 7+100  最终生成的viewType
     */
    override fun getItemViewType(position: Int): Int {
        val partitionPosition = totalItemList[position]
        verifyPartitionPosition(partitionPosition)
        val partition = partitionList[partitionPosition]
        val bean = partition.getItemViewTypeBean(getPartitionInnerPosition(position, partition))
        if (bean.itemViewType < 0) {
            throw RuntimeException("BasePartitionAdapter getItemViewType ItemViewTypeBean.itemViewType < 0")
        }
        val key: Int
        key = if (bean.repeat) {
            bean.itemViewType
        } else {
            bean.itemViewType + partitionPosition * 1000
        }
        viewTypePositionArr.put(key, partitionPosition)
        return key
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        /**
         * 此处逻辑与getItemViewType方法相对应
         */
        return if (viewType >= 1000) {
            val partitionPosition = viewTypePositionArr[viewType]
            partitionList[partitionPosition]
                .onCreateViewHolder(viewGroup, viewType - partitionPosition * 1000)
        } else {
            partitionList[viewTypePositionArr[viewType]]
                .onCreateViewHolder(viewGroup, viewType)
        }
    }

    override fun onBindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val partitionPosition = totalItemList[position]
        verifyPartitionPosition(partitionPosition)
        val partition = partitionList[partitionPosition]
        partition.onBindViewHolder(viewHolder, getPartitionInnerPosition(position, partition))
    }

    /**
     * getItemCount的值由业务类决定
     */
    override fun getItemCount(): Int {
        return totalItemList.size
    }

    /**
     * 创建分区集合
     */
    fun setData(list: List<IPartitionBean>?) {
        //数据初始化
        partitionList.clear()
        viewTypePositionArr.clear()
        totalItemList.clear()
        if (list == null) {
            return
        }
        //创建数据
        val size = list.size
        for (i in 0 until size) {
            val bean = list[i]
            val partition = createPartition(bean) ?: continue
            //设置分区数据
            partition.startCount = totalItemList.size
            partition.position = i
            partition.setItemList() //setItemList必须在setPosition之后执行
            partition.setData(bean)
            partition.partitionType = bean.getPartitionType()
            partitionList.add(partition)
            //计算totalItemList
            totalItemList.addAll(partition.getItemList())
        }
    }

    /**
     * 重置分区集合
     */
    fun resetData(list: List<IPartitionBean>?) {
        setData(list)
        notifyDataSetChanged()
    }

    /**
     * 获取指定位置的分区，可能为null
     *
     * @param partitionPosition 通过BasePartition子类回调获取
     * @return 返回指定位置的分区
     */
    fun getPartition(partitionPosition: Int): BasePartition<*> {
        verifyPartitionPosition(partitionPosition)
        return partitionList[partitionPosition]
    }

    /**
     * 更新指定位置的分区
     *
     * @param bean              新分区数据源
     * @param partitionPosition 通过BasePartition子类回调获取
     */
    fun updatePartition(bean: IPartitionBean?, partitionPosition: Int) {
        if (bean == null) {
            throw RuntimeException("BasePartitionAdapter updatePartition bean == null")
        }
        verifyPartitionPosition(partitionPosition)
        //添加新分区到partitionList中
        val newPartition = createPartition(bean)
            ?: throw RuntimeException("BasePartitionAdapter updatePartition newPartition == null")
        newPartition.setData(bean)
        newPartition.partitionType = bean.getPartitionType()
        partitionList[partitionPosition] = newPartition
        updatePartitionParam()
    }

    /**
     * 移除指定位置的分区
     *
     * @param partitionPosition 通过BasePartition子类回调获取
     */
    fun removePartition(partitionPosition: Int) {
        verifyPartitionPosition(partitionPosition)
        //删除partitionList中数据
        partitionList.removeAt(partitionPosition)
        updatePartitionParam()
    }

    /**
     * 在指定位置添加新分区
     *
     * @param bean              新分区数据源
     * @param partitionPosition 通过BasePartition子类回调获取
     */
    fun addPartition(bean: IPartitionBean?, partitionPosition: Int) {
        if (bean == null) {
            throw RuntimeException("BasePartitionAdapter addPartition bean == null")
        }
        verifyPartitionPosition(partitionPosition)
        //添加新分区到partitionList中
        val newPartition = createPartition(bean)
            ?: throw RuntimeException("BasePartitionAdapter addPartition newPartition == null")
        newPartition.setData(bean)
        newPartition.partitionType = bean.getPartitionType()
        partitionList.add(partitionPosition, newPartition)
        updatePartitionParam()
    }

    /**
     * 获取指定范围内的分区list，可能为null
     *
     * @param startPartitionPosition 通过BasePartition子类回调获取
     * @param endPartitionPosition   通过BasePartition子类回调获取
     * @return 返回指定位置的分区
     */
    fun getPartitionList(
        startPartitionPosition: Int,
        endPartitionPosition: Int
    ): List<BasePartition<*>> {
        verifyPartitionPosition(startPartitionPosition, endPartitionPosition)
        return partitionList.subList(startPartitionPosition, endPartitionPosition)
    }

    /**
     * 将指定返回内的分区更改为新分区list
     *
     * @param list                   新分区数据源
     * @param startPartitionPosition 通过BasePartition子类回调获取
     * @param endPartitionPosition   通过BasePartition子类回调获取
     */
    fun updatePartitionList(
        list: List<IPartitionBean>?,
        startPartitionPosition: Int,
        endPartitionPosition: Int
    ) {
        if (list.isNullOrEmpty()) {
            throw RuntimeException("BasePartitionAdapter updatePartitionList list == null || list.size() == 0")
        }
        verifyPartitionPosition(startPartitionPosition, endPartitionPosition)
        //移除指定范围的分区
        for (i in startPartitionPosition until endPartitionPosition) {
            partitionList.removeAt(startPartitionPosition)
        }
        //添加分区列表到partitionList中
        for (i in list.indices) {
            val bean = list[i]
            val newPartition = createPartition(bean) ?: continue
            newPartition.setData(bean)
            newPartition.partitionType = bean.getPartitionType()
            partitionList.add(startPartitionPosition + i, newPartition)
        }
        updatePartitionParam()
    }

    /**
     * 移除指定范围的分区
     *
     * @param startPartitionPosition 通过BasePartition子类回调获取
     * @param endPartitionPosition   通过BasePartition子类回调获取
     */
    fun removePartitionList(
        startPartitionPosition: Int,
        endPartitionPosition: Int
    ) {
        verifyPartitionPosition(startPartitionPosition, endPartitionPosition)
        //移除指定范围的分区
        for (i in startPartitionPosition until endPartitionPosition) {
            partitionList.removeAt(startPartitionPosition)
        }
        updatePartitionParam()
    }

    /**
     * 在指定位置添加分区list
     *
     * @param list              新分区数据源
     * @param partitionPosition 通过BasePartition子类回调获取
     */
    fun addPartitionList(
        list: List<IPartitionBean>?,
        partitionPosition: Int
    ) {
        if (list.isNullOrEmpty()) {
            throw RuntimeException("BasePartitionAdapter addPartitionList list == null || list.size() == 0")
        }
        verifyPartitionPosition(partitionPosition)
        //添加分区列表到partitionList中
        for (i in list.indices) {
            val bean = list[i]
            val newPartition = createPartition(bean) ?: continue
            newPartition.setData(bean)
            newPartition.partitionType = bean.getPartitionType()
            partitionList.add(partitionPosition + i, newPartition)
        }
        updatePartitionParam()
    }

    /**
     * 更新集合数据，保证位置的正确性
     */
    private fun updatePartitionParam() {
        //clear
        totalItemList.clear()
        val size = partitionList.size
        for (i in 0 until size) {
            val partition = partitionList[i]
            //设置分区数据
            partition.startCount = totalItemList.size
            partition.position = i
            partition.setItemList() //setItemList必须在setPosition之后执行
            //计算totalItemList
            totalItemList.addAll(partition.getItemList())
        }
        notifyDataSetChanged()
    }

    /**
     * 使BasePartition子类得到正确的position，作用在传参上
     */
    private fun getPartitionInnerPosition(
        position: Int,
        partition: BasePartition<*>
    ): Int {
        /**
         * 减去partition的startCount是为了使partition得到自己的position，
         * 因为参数position是totalItemList的index。
         */
        return position - partition.startCount
    }

    /**
     * 验证partitionPosition是否合法
     */
    private fun verifyPartitionPosition(partitionPosition: Int) {
        if (partitionPosition < 0 || partitionPosition >= partitionList.size) {
            throw RuntimeException("BasePartitionAdapter verifyPartitionPosition partitionPosition is illegal")
        }
    }

    /**
     * 验证partitionPosition是否合法
     */
    private fun verifyPartitionPosition(
        startPartitionPosition: Int,
        endPartitionPosition: Int
    ) {
        if (startPartitionPosition < 0 || startPartitionPosition >= partitionList.size) {
            throw RuntimeException("BasePartitionAdapter verifyPartitionPosition startPartitionPosition is illegal")
        }
        if (endPartitionPosition < 0 || endPartitionPosition >= partitionList.size) {
            throw RuntimeException("BasePartitionAdapter verifyPartitionPosition endPartitionPosition is illegal")
        }
        if (startPartitionPosition > endPartitionPosition) {
            throw RuntimeException("BasePartitionAdapter verifyPartitionPosition startPartitionPosition > endPartitionPosition")
        }
    }

    /**
     * 创建分区
     *
     * @return 可能为null
     */
    abstract fun createPartition(bean: IPartitionBean): BasePartition<*>?
}