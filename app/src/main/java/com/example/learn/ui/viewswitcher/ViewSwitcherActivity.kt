package com.example.learn.ui.viewswitcher

import android.os.Bundle
import android.os.Handler
import com.example.common.base.BaseActivity
import com.example.learn.R
import kotlinx.android.synthetic.main.activity_view_switcher.*
import java.util.*

/**
 * @author fqxyi
 * @date 2020/8/18
 * ViewSwitcher使用示例
 */
class ViewSwitcherActivity : BaseActivity() {

    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_switcher)
        testViewSwitcher()
    }

    private fun testViewSwitcher() {
        viewSwitcher.setFactory { ViewSwitcherItemView(this@ViewSwitcherActivity) }
        viewSwitcher.setInAnimation(this, R.anim.view_switcher_slide_in)
        viewSwitcher.setOutAnimation(this, R.anim.view_switcher_slide_out)
        val list = ArrayList<ViewSwitcherBean>()
        for (i in 0..999) {
            list.add(ViewSwitcherBean("title $i", "content $i"))
        }
        val itemView = viewSwitcher.nextView as ViewSwitcherItemView
        itemView.initData(list[index++ % list.size])
        viewSwitcher.showNext()
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                val innerItemView = viewSwitcher.nextView as ViewSwitcherItemView
                innerItemView.initData(list[index++ % list.size])
                if (index >= list.size) {
                    index = 0
                }
                viewSwitcher.showNext()
                handler.postDelayed(this, 2000)
            }
        }, 2000)
    }
}