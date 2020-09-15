package com.example.learn.ui.recyclerview.intab.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.Nullable
import com.example.learn.R
import kotlinx.android.synthetic.main.recycler_in_tab_vertical.view.*

class RecyclerInTabVertical: LinearLayout {

    constructor(mContext: Context) : super(mContext)

    constructor(mContext: Context, mAttributeSet: AttributeSet) : super(mContext, mAttributeSet)

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        val itemView: View = View.inflate(context, R.layout.recycler_in_tab_vertical, this)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        itemView.layoutParams = layoutParams
        orientation = VERTICAL
    }

    fun initData(data: String) {
        verticalText.text = data
    }

}