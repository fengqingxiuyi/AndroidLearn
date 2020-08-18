package com.example.learn.ui.viewswitcher

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.example.learn.R
import kotlinx.android.synthetic.main.activity_view_switcher_item.view.*

/**
 * @author fqxyi
 * @date 2018/3/23
 */
class ViewSwitcherItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.activity_view_switcher_item, this)
    }

    fun initData(viewSwitcherBean: ViewSwitcherBean?) {
        if (viewSwitcherBean == null) {
            return
        }
        itemTitle.text = viewSwitcherBean.title
        itemContent.text = viewSwitcherBean.content
    }
}