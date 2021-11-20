package com.example.learn.ui.recyclerview

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.base.BaseActivity
import com.example.learn.R
import kotlinx.android.synthetic.main.activity_recyclerview.*

class RecyclerViewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)
        //
        recyclerSimple.layoutManager = LinearLayoutManager(this)
        val adapter = RecyclerSimpleAdapter(this)
        recyclerInTabBtn.setOnClickListener {
//            startActivity(Intent(this, RecyclerInTabActivity::class.java))
            adapter.updateData()
        }
        recyclerSimple.adapter = adapter
    }
}

class RecyclerSimpleAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var size = 50
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val textView = TextView(context)
        textView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200)
        return RecyclerSimpleHolder(textView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val textView = holder.itemView as TextView
        textView.text = "pos $position"
    }

    override fun getItemCount(): Int {
        return size
    }

    fun updateData() {
        size = 51
        notifyItemInserted(8)
    }

}

class RecyclerSimpleHolder(itemView: View) : RecyclerView.ViewHolder(itemView)