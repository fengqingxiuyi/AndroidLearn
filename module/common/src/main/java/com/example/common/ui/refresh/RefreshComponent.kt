package com.example.common.ui.refresh

import com.scwang.smart.refresh.layout.SmartRefreshLayout

object RefreshComponent {
  fun init() {
    SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
      SimpleRefreshHeader(context)
    }

    SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
      SimpleRefreshFooter(context)
    }
  }
}