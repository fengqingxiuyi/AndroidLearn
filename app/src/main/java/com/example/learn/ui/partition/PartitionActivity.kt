package com.example.learn.ui.partition

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.common.BaseActivity
import com.example.learn.R
import com.example.learn.ui.partition.partition.one.OneBean
import com.example.learn.ui.partition.partition.two.TwoBean
import com.example.learn.ui.partition.utils.Util
import com.example.partition.IPartitionBean
import com.example.partition.ISpanSize
import com.example.ui.toast.ToastUtil
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*

class PartitionActivity : BaseActivity(), IPartitionCallback {

    //adapter
    private val partitionAdapter by lazy {
        PartitionAdapter(applicationContext, this)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initRecyclerView()
        initAdapter()
    }

    private fun initRecyclerView() {
        //set layout manager
        mainRecyclerView.layoutManager = GridLayoutManager(applicationContext, ISpanSize.ONE)
    }

    private fun initAdapter() {
        //create adapter
        partitionAdapter.setData(Util.getData())
        //set adapter
        mainRecyclerView.adapter = partitionAdapter
    }

    fun testUpdate(view: View?) {
//        partitionAdapter.resetData(null)

//        ToastUtil.toast("partition = " + partitionAdapter.getPartition(1))

//        val oneBean = OneBean()
//        val dataBeanList: MutableList<OneBean.DataBean> = ArrayList()
//        val dataBean = OneBean.DataBean()
//        dataBean.text = "新增的分区1-数据"
//        dataBeanList.add(dataBean)
//        oneBean.dataBean = dataBeanList
//        oneBean.lineHeight = 50
//        partitionAdapter.updatePartition(oneBean, 2)

//        partitionAdapter.removePartition(1)

//        val oneBean = OneBean()
//        val dataBeanList: MutableList<OneBean.DataBean> = ArrayList()
//        val dataBean = OneBean.DataBean()
//        dataBean.text = "新增的分区1-数据"
//        dataBeanList.add(dataBean)
//        oneBean.dataBean = dataBeanList
//        oneBean.lineHeight = 50
//        partitionAdapter.addPartition(oneBean, 1)

//        ToastUtil.toast("partition = " + partitionAdapter.getPartitionList(1, 2))

//        val list: MutableList<IPartitionBean> = ArrayList()
//        val oneBean = OneBean()
//        val dataBeanList: MutableList<OneBean.DataBean> = ArrayList()
//        val dataBean = OneBean.DataBean()
//        dataBean.text = "更新的分区1-数据"
//        dataBeanList.add(dataBean)
//        oneBean.dataBean = dataBeanList
//        oneBean.lineHeight = 50
//        list.add(oneBean)
//        val twoBean = TwoBean()
//        val dataBeanList1: MutableList<TwoBean.DataBean> = ArrayList()
//        val dataBean1 = TwoBean.DataBean()
//        dataBean1.text = "新增的分区2数据-1"
//        dataBeanList1.add(dataBean1)
//        val dataBean2 = TwoBean.DataBean()
//        dataBean2.text = "新增的分区2数据-2"
//        dataBeanList1.add(dataBean2)
//        twoBean.dataBean = dataBeanList1
//        twoBean.lineHeight = 1
//        list.add(twoBean)
//        partitionAdapter.updatePartitionList(list, 1, 3)

//        partitionAdapter.removePartitionList(1, 3)

        val list: MutableList<IPartitionBean> = ArrayList()
        val oneBean = OneBean()
        val dataBeanList: MutableList<OneBean.DataBean> = ArrayList()
        val dataBean = OneBean.DataBean()
        dataBean.text = "新增的分区1-数据"
        dataBeanList.add(dataBean)
        oneBean.dataBean = dataBeanList
        oneBean.lineHeight = 50
        list.add(oneBean)
        val twoBean = TwoBean()
        val dataBeanList1: MutableList<TwoBean.DataBean> = ArrayList()
        val dataBean1 = TwoBean.DataBean()
        dataBean1.text = "新增的分区2数据-1"
        dataBeanList1.add(dataBean1)
        val dataBean2 = TwoBean.DataBean()
        dataBean2.text = "新增的分区2数据-2"
        dataBeanList1.add(dataBean2)
        twoBean.dataBean = dataBeanList1
        twoBean.lineHeight = 1
        list.add(twoBean)
        partitionAdapter.addPartitionList(list, 1)
    }

    override fun clickOne(position: Int, text: String?) {
        ToastUtil.toast("position = $position , text = $text")
    }

    override fun clickTwo(position: Int, text: String?) {
        ToastUtil.toast("position = $position , text = $text")
    }

    override fun clickThree(position: Int, text: String?) {
        ToastUtil.toast("position = $position , text = $text")
    }
}