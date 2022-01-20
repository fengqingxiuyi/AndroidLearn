package com.example.learn.ui.recyclerview

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.base.BaseActivity
import com.example.common.ui.empty.TipViewManager
import com.example.common.ui.empty.config.ITipViewConfig
import com.example.common.utils.viewBinding
import com.example.learn.databinding.SimpleRecyclerviewBinding

/**
 * 略
 *
 * @author: fqxyi
 */
class SimpleRecyclerViewActivity : BaseActivity() {

  private val binding: SimpleRecyclerviewBinding by viewBinding()
//  private var data = listOf<String>("1","1","1","1","1","1","1","1","1","1","1","1")
  private var data = listOf<String>()

  private val tipViewManager by lazy {
    val tipViewManager = TipViewManager(this)
    tipViewManager.build()
    tipViewManager
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
    initRecycler()
  }

  private fun initRecycler() {
    binding.rvSimple.layoutManager = LinearLayoutManager(this)
    binding.rvSimple.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val textView = TextView(applicationContext)
        return object : RecyclerView.ViewHolder(textView) {

        }
      }

      override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemView = holder.itemView as TextView
        itemView.text = data[position]
      }

      override fun getItemCount(): Int {
        return data.size
      }
    }
    if (data.isEmpty()) {
      tipViewManager.showTipView(ITipViewConfig.INoDataConfig.FAVORITED_ROOM_NO_DATA)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    tipViewManager.onDestroy()
  }
}