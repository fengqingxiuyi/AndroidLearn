package com.example.learn.ui.adapter

import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.learn.R
import kotlinx.android.synthetic.main.list_common_adapter.*

/**
 * @author fqxyi
 * @date 2017/5/19
 */
class ListCommonAdapterActivity : BaseActivity() {

    var data = arrayOf(
        "a", "b", "c",
        "d", "e", "f",
        "g", "h", "i"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_common_adapter)
        // Array to List
        val list = mutableListOf<String>()
        list.addAll(data)
        list.addAll(data)
        listView.adapter = ListCommonAdapter(this, list)
    }
}