package com.example.learn.ui.recyclerview

import android.content.Intent
import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.learn.R
import com.example.learn.ui.recyclerview.intab.activity.RecyclerInTabActivity
import kotlinx.android.synthetic.main.activity_recyclerview.*

class RecyclerViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        //
        recyclerInTabBtn.setOnClickListener {
            startActivity(Intent(this, RecyclerInTabActivity::class.java))
        }
    }
}