package com.example.learn.ui.recyclerview.intab.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learn.R
import com.example.learn.ui.recyclerview.intab.activity.RecyclerInTabActivity
import com.example.learn.ui.recyclerview.intab.adapter.RecyclerInTabInAdapter
import com.example.learn.ui.recyclerview.intab.utils.MainUtil
import com.example.learn.ui.recyclerview.intab.view.InRecyclerView
import kotlinx.android.synthetic.main.recycler_in_tab_fragment.*

class RecyclerInTabFragment : Fragment(), InRecyclerView.ParentInterceptListener, RecyclerInTabInAdapter.ItemClickListener {

    private val inAdapter : RecyclerInTabInAdapter by lazy {
        RecyclerInTabInAdapter(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.recycler_in_tab_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    private fun init() {
        inRecyclerView.layoutManager = LinearLayoutManager(context)
        inRecyclerView.setTabTop(MainUtil.dip2px(context, 44f) + MainUtil.getStatusBarHeight(context))
        inRecyclerView.setParentInterceptListener(this)
        inAdapter.setItemClickListener(this)
        inAdapter.updateData(MainUtil.getHorizontalList(), MainUtil.getVerticalList())
        inRecyclerView.adapter = inAdapter
    }

    override fun intercept(intercept: Boolean) {
        if (activity is RecyclerInTabActivity) {
            (activity as RecyclerInTabActivity).setIntercept(intercept)
        }
    }

    override fun itemClick(position: Int, itemData: String) {
        Toast.makeText(context, "点击了位置: $position ,数据: $itemData", Toast.LENGTH_SHORT).show()
    }

}