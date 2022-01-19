package com.example.common.ui.empty.utils

import androidx.annotation.StringRes
import com.example.common.R
import com.example.common.global.AppGlobal
import com.example.common.ui.empty.bean.TipContentBean
import com.example.common.ui.empty.config.ITipViewConfig
import com.example.common.ui.empty.config.ITipViewConfig.*

/**
 * 略
 *
 * @author: shenbf
 */
object ErrorImageAndTxtUtils : ITipViewConfig, INetErrorConfig, INoDataConfig, ITipTypeConfig {
  @JvmStatic
  fun getTipContentBean(type: Int): TipContentBean? {
    // 网络提示
    var tipContentBean = getNetTipContentBean(type)
    if (null == tipContentBean) {
      // 没有数据提示
      tipContentBean = getNoDataTipContentBean(type)
    }
    if (null == tipContentBean) {
      // 状态提示
      tipContentBean = getTipTypeTipContentBean(type)
    }
    return tipContentBean
  }

  private fun getNetTipContentBean(type: Int): TipContentBean? {
    return when (type) {
      INetErrorConfig.NET_ERROR_CONFIG ->
        return TipContentBean(
          R.drawable.base_img_error_net,
          getString(R.string.common_error_net_title),
          getString(R.string.common_error_net_hint),
          getString(R.string.common_error_net_btn)
        )
      else -> null
    }
  }

  private fun getNoDataTipContentBean(type: Int): TipContentBean? {
    return when (type) {
      INoDataConfig.FAVORITED_ROOM_NO_DATA -> TipContentBean(
        R.drawable.base_img_nodata_room,
        "",
        getString(R.string.common_nodata_common_hint),
        ""
      )
      else -> null
    }
  }

  private fun getTipTypeTipContentBean(type: Int): TipContentBean? {
    when (type) {

    }
    return null
  }

  private fun getString(@StringRes resStringId: Int): String {
    try {

      return AppGlobal.appContext.getString(resStringId)
    } catch (e: Exception) {
    }
    return ""
  }
}