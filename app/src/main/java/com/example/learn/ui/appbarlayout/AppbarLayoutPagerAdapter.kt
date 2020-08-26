package com.example.learn.ui.appbarlayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

class AppbarLayoutPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var list = ArrayList<Fragment>()

    init {
        list.add(AppbarLayoutFragment())
        list.add(AppbarLayoutFragment())
        list.add(AppbarLayoutFragment())
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }


}