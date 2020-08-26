package com.example.learn.ui.appbarlayout

import android.os.Bundle
import com.example.common.BaseActivity
import com.example.learn.R
import kotlinx.android.synthetic.main.appbar_layout_activity.*

class AppbarLayoutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.appbar_layout_activity)
        homePager.adapter = AppbarLayoutPagerAdapter(supportFragmentManager)
    }

}