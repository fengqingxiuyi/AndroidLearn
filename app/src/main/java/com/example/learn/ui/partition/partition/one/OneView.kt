package com.example.learn.ui.partition.partition.one

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.example.learn.R
import com.example.learn.ui.partition.IPartitionCallback
import kotlinx.android.synthetic.main.one_view.view.*

class OneView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        //
        View.inflate(context, R.layout.one_view, this)
        //
        orientation = VERTICAL
    }

    fun initData(dataBean: OneBean.DataBean?, callback: IPartitionCallback?, position: Int) {
        if (dataBean == null) {
            return
        }
        oneViewText.text = dataBean.text
        oneViewText.setOnClickListener { callback?.clickOne(position, dataBean.text) }
    }
}