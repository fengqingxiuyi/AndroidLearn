package com.example.learn.ui.recyclerview

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.common.base.BaseActivity
import com.example.common.ui.empty.RVTipViewManager
import com.example.common.ui.empty.config.ITipViewConfig
import com.example.common.utils.viewBinding
import com.example.learn.R
import com.example.learn.databinding.ChadRecyclerviewBinding

/**
 * 略
 *
 * @author: shenbf
 */
class ChadRecyclerViewActivity : BaseActivity() {

  private val binding: ChadRecyclerviewBinding by viewBinding()
    private var data = listOf<String>("1","2","3","4","5","6","7","8","9")
//  private var data = listOf<String>()

  private val tipViewManager by lazy {
    val tipViewManager = RVTipViewManager(this)
    tipViewManager.build()
    tipViewManager
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
    initRecycler()
  }

  private fun initRecycler() {
    binding.rvChad.layoutManager = LinearLayoutManager(this)
    val adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.chad_recyclerview_item, data) {
      override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_text, item)
      }
    }
    adapter.emptyView = tipViewManager.createTipView()
    binding.rvChad.adapter = adapter
    //
    if (data.isEmpty()) {
      tipViewManager.showTipView(ITipViewConfig.INoDataConfig.FAVORITED_ROOM_NO_DATA)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    tipViewManager.onDestroy()
  }
}