package com.example.learn.ui.recyclerview.intab.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.Nullable
import com.example.learn.R
import com.example.learn.ui.recyclerview.intab.adapter.RecyclerInTabPagerAdapter
import com.example.learn.ui.recyclerview.intab.bean.DataBean
import com.example.learn.ui.recyclerview.intab.callback.RecyclerInTabCallback
import kotlinx.android.synthetic.main.recycler_in_tab_pager.view.*

class RecyclerInTabPager: LinearLayout {

    private var outAdapter : RecyclerInTabPagerAdapter? = null

    constructor(mContext: Context) : super(mContext)

    constructor(mContext: Context, mAttributeSet: AttributeSet) : super(mContext, mAttributeSet)

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        val itemView: View = View.inflate(context, R.layout.recycler_in_tab_pager, this)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        itemView.layoutParams = layoutParams
        orientation = VERTICAL
        //
        tabLayout.setupWithViewPager(viewPager) // 关联tabLayout和viewPager
    }

    fun initData(pager: DataBean.PagerBean?) {
        outAdapter?.updateData(pager?.list)
    }

    fun setRecyclerInTabCallback(callback: RecyclerInTabCallback?) {
        outAdapter = callback?.initPagerAdapter()
        viewPager.adapter = outAdapter
    }

}