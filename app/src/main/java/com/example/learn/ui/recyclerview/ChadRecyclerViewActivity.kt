package com.example.learn.ui.recyclerview

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.common.base.BaseActivity
import com.example.common.base.list.BaseListHelper
import com.example.common.base.list.IBaseListContract
import com.example.common.ui.empty.RVTipViewManager
import com.example.common.ui.empty.config.ITipViewConfig
import com.example.common.ui.loading.LoadingUtil
import com.example.common.ui.refresh.RVContentLessScreenGlobalListener
import com.example.common.ui.refresh.SimpleRefreshLayout
import com.example.common.utils.viewBinding
import com.example.learn.R
import com.example.learn.databinding.ChadRecyclerviewBinding

class ChadRecyclerViewActivity : BaseActivity(), IBaseListContract<List<String>> {

  private val binding: ChadRecyclerviewBinding by viewBinding()
    private var data = listOf<String>(
      "1","2","3","4","5","6","7","8","9",
      "1","2","3","4","5","6","7","8","9",
      "1","2","3","4","5","6","7","8","9",
      "1","2","3","4","5","6","7","8","9",
      "1","2","3","4","5","6","7","8","9",
      "1","2","3","4","5","6","7","8","9",
      "1","2","3","4","5","6","7","8","9",
      "1","2","3","4","5","6","7","8","9",
    )
//  private var data = listOf<String>()
  private lateinit var adapter: BaseQuickAdapter<String, BaseViewHolder>

  //特殊处理
  private val rvContentLessScreenGlobalListener by lazy {
    RVContentLessScreenGlobalListener(
      binding.rvChad, binding.smartRefreshLayout, true
    ) {
      request()
    }
  }

  private val tipViewManager by lazy {
    val tipViewManager = RVTipViewManager(this)
    tipViewManager.build()
    tipViewManager
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
    request(true)
    initRefresh()
    initRecycler()
  }

  private fun initRefresh() {
    binding.smartRefreshLayout.setOnRefreshListener {
      refresh()
    }
    binding.smartRefreshLayout.setOnLoadMoreListener {
      //请求数据
      request()
    }
  }

  private fun refresh() {
    //重置状态
    rvContentLessScreenGlobalListener.add()
    SimpleRefreshLayout.resetNoMore(binding.smartRefreshLayout)
    //页码重置

    //请求数据`
    request()
  }

  private fun request(firstInPage: Boolean = false) {
//    if (firstInPage) LoadingUtil.showLoading(this)
    //这里发起请求
  }

  private fun initRecycler() {
    binding.rvChad.layoutManager = LinearLayoutManager(this)
    adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.chad_recyclerview_item, data) {
      override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_text, item)
      }
    }
    adapter.emptyView = tipViewManager.createTipView()
    binding.rvChad.adapter = adapter
    //内容不满一屏时自动请求下一页数据的监听器&自动收回没有更多内容的底部
    rvContentLessScreenGlobalListener.add()
    //
    if (data.isEmpty()) {
      tipViewManager.showTipView(ITipViewConfig.INoDataConfig.FAVORITED_ROOM_NO_DATA)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    tipViewManager.onDestroy()
  }

  override fun refresh(data: List<String>) {
    LoadingUtil.hideLoading(this)
    binding.smartRefreshLayout.finishRefresh()
    binding.smartRefreshLayout.setEnableLoadMore(!data.isNullOrEmpty())
  }

  override fun showErrEmptyView() {
    BaseListHelper.showErrEmptyView(binding.smartRefreshLayout, tipViewManager) {
      LoadingUtil.showLoading(this)
      refresh()
    }
  }

  override fun loadMore(data: List<String>) {
    if (data.isNullOrEmpty()) { //数据已全部加载完毕
      rvContentLessScreenGlobalListener.remove()
      SimpleRefreshLayout.showNoMore(
        binding.smartRefreshLayout,
        fullScreen = rvContentLessScreenGlobalListener.lastPosition < adapter.itemCount - 1
      )
      binding.smartRefreshLayout.finishLoadMoreWithNoMoreData()
    } else { //本页数据加载完毕
      binding.smartRefreshLayout.finishLoadMore()
    }
    data?.let {
      adapter.addData(data)
    }
  }

  override fun failure(code: Int) {
    binding.smartRefreshLayout.finishRefresh(false)
    binding.smartRefreshLayout.finishLoadMore(false)
  }
}