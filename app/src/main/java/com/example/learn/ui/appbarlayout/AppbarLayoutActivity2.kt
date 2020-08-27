package com.example.learn.ui.appbarlayout

import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.learn.R
import kotlinx.android.synthetic.main.appbar_layout_activity2.*

class AppbarLayoutActivity2 : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.appbar_layout_activity2)
        homePager.adapter = AppbarLayoutPagerAdapter(supportFragmentManager)
    }

}