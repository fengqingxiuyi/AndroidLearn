package com.example.banner.listener

import android.view.View

/**
 * @author fqxyi
 * @date 2017/9/18
 */
interface PointChangeListener {
    fun getInfo(pointView: View?, position: Int, pointSize: Int)
}