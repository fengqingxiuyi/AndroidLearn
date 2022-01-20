package com.example.common.ui.empty.config

/**
 * 略
 *
 * @author: fqxyi
 */
interface ITipViewConfig {
  interface IImgSizeConfig {
    companion object {
      /*** 大的尺寸  */
      const val IMG_BIG_SIZE = 180

      /*** 中的尺寸  */
      const val IMG_MIDDLE_SIZE = 144

      /*** 小的尺寸  */
      const val IMG_SMALL_SIZE = 80

      /*** 距离上面距离大的  */
      const val TIP_VIEW_TOP_LAGER = 165

      /*** 距离上面距离小的尺寸  */
      const val TIP_VIEW_TOP_SMALL = 30

      /*** 没有距离  */
      const val TIP_VIEW_TOP_NULL = 0
    }
  }

  /***
   * 网络类型 错误
   */
  interface INetErrorConfig {
    companion object {
      /*** 网络错误  */
      const val NET_ERROR_CONFIG = 1001
    }
  }

  /***
   * 自定义扩展类型
   */
  interface ICustomerTipConfig {
    companion object {
      const val CUSTOMER_ONE = 90001
      const val CUSTOMER_TWO = 90002
      const val CUSTOMER_THREE = 90003
      const val CUSTOMER_FOUR = 90004
      const val CUSTOMER_FIVE = 90005
      const val CUSTOMER_SIX = 90006
      const val CUSTOMER_SEVEN = 90007
      const val CUSTOMER_EIGHT = 90008
      const val CUSTOMER_NINE = 90009
    }
  }

  /**
   * 无数据提示
   */
  interface INoDataConfig {
    companion object {
      /*** 你没有收藏任何房间 */
      const val FAVORITED_ROOM_NO_DATA = 3001
    }
  }

  /**
   * 状态提示
   */
  interface ITipTypeConfig {
    companion object {

    }
  }
}