package com.example.common.base.list

import com.example.common.ui.empty.RVTipViewManager
import com.example.common.ui.empty.config.ITipViewConfig
import com.example.utils.network.NetworkUtils
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * 略
 */
object BaseListHelper {

  fun showErrEmptyView(srl: SmartRefreshLayout?, tipViewManager: RVTipViewManager, refresh: () -> Unit) {
    srl?.setEnableLoadMore(false)
    if (!NetworkUtils.isConnected()) {
      tipViewManager.showTipView(ITipViewConfig.INetErrorConfig.NET_ERROR_CONFIG)
      tipViewManager.setTipViewClickListener {
        tipViewManager.hideTipView()
        refresh()
      }
    }
  }

}