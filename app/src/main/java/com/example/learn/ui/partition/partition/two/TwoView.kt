package com.example.learn.ui.partition.partition.two

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.example.learn.R
import com.example.learn.ui.partition.IPartitionCallback
import kotlinx.android.synthetic.main.two_view.view.*

class TwoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        //
        View.inflate(context, R.layout.two_view, this)
        //
        orientation = VERTICAL
    }

    fun initData(dataBean: TwoBean.DataBean?, callback: IPartitionCallback?, position: Int) {
        if (dataBean == null) {
            return
        }
        twoViewText.text = dataBean.text
        twoViewText.setOnClickListener { callback?.clickTwo(position, dataBean.text) }
    }
}