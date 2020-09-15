package com.example.learn.ui.recyclerview.intab.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learn.R
import com.example.learn.ui.recyclerview.intab.adapter.RecyclerInTabOutAdapter
import com.example.learn.ui.recyclerview.intab.adapter.RecyclerInTabPagerAdapter
import com.example.learn.ui.recyclerview.intab.callback.RecyclerInTabCallback
import com.example.learn.ui.recyclerview.intab.utils.MainUtil
import kotlinx.android.synthetic.main.recycler_in_tab_activity.*

class RecyclerInTabActivity : FragmentActivity(), RecyclerInTabCallback {

    private val outAdapter : RecyclerInTabOutAdapter by lazy {
        RecyclerInTabOutAdapter(this, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_in_tab_activity)
        //
        initView()
    }

    private fun initView() {
        outRecyclerView.layoutManager = LinearLayoutManager(this)
        outAdapter.updateData(MainUtil.getDataBean())
        outRecyclerView.adapter = outAdapter
    }

    override fun initPagerAdapter() : RecyclerInTabPagerAdapter {
        return RecyclerInTabPagerAdapter(supportFragmentManager)
    }

    fun setIntercept(intercept: Boolean) {
        outRecyclerView.setIntercept(intercept)
    }

}