package com.example.common.ui.empty

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.common.global.AppGlobal
import com.example.common.ui.empty.bean.TipContentBean
import com.example.common.ui.empty.config.ITipViewConfig
import com.example.common.ui.empty.widget.ErrorOrEmptyView
import com.example.utils.device.DensityUtil
import com.example.utils.device.DeviceUtil

/**
 * 遮挡页面的缺省页
 *
 * @author: fqxyi
 */
class TipViewManager : BaseTipViewManager {

  private var errorOrEmptyView: ErrorOrEmptyView? = null
  private var paddingStart = 0
  private var paddingTop = 0
  private var paddingEnd = 0
  private var paddingBottom = 0
  private var mGravity = -1
  private var sizeImage = 0
  private var mTopPadding = 0

  constructor(instance: Activity) : super(instance) {
    initTipParams()
  }

  constructor(instance: Fragment) : super(instance) {
    initTipParams()
  }

  constructor(instance: ViewGroup) : super(instance) {
    initTipParams()
  }

  override fun changeViewLayoutParams() {
    initTipView()
    errorOrEmptyView!!.layoutParams.let {
      when (it) {
        is ConstraintLayout.LayoutParams -> {
          it.topMargin = topHeight
        }
        is FrameLayout.LayoutParams -> {
          it.topMargin = topHeight
        }
        is RelativeLayout.LayoutParams -> {
          it.topMargin = topHeight
        }
      }
    }
  }

  /***
   * @paramsvalue IImgSizeConfig.TIP_VIEW_TOP_LAGER
   * @paramsvalue IImgSizeConfig.TIP_VIEW_TOP_SMALL
   * @paramsvalue IImgSizeConfig.TIP_VIEW_TOP_NULL
   */
  fun setTopModel(topPadding: Int) {
    var mTopPadding = topPadding
    if (mTopPadding > 0) {
      mTopPadding = (mTopPadding.toFloat() / 667f * DeviceUtil.getScreenHeight(AppGlobal.appContext)).toInt()
    }
    this.mTopPadding = mTopPadding
  }

  fun setGravity(gravity: Int) {
    this.mGravity = gravity
  }

  fun setPadding(start: Int, top: Int, end: Int, bottom: Int) {
    paddingStart = start
    paddingTop = top
    paddingEnd = end
    paddingBottom = bottom
  }

  private fun initTipParams() {
    sizeImage = DensityUtil.dp2px(AppGlobal.appContext, ITipViewConfig.IImgSizeConfig.IMG_BIG_SIZE.toFloat())
    setTopModel(ITipViewConfig.IImgSizeConfig.TIP_VIEW_TOP_LAGER)
  }

  fun setSizeImage(sizeImage: Int) {
    this.sizeImage = DensityUtil.dp2px(AppGlobal.appContext, sizeImage.toFloat())
  }

  private fun initTipView() {
    if (context == null) throw NullPointerException("ErrorOrEmptyView Context is Null")
    if (null == errorOrEmptyView) {
      errorOrEmptyView = ErrorOrEmptyView(context!!)
    }
    addContentView(errorOrEmptyView)
  }

  override fun showTipView(tipConfig: Int) {
    initTipView()
    initTipViewConfig(tipConfig)
    errorOrEmptyView?.visibility = View.VISIBLE
  }

  override fun showTipView(tipConfig: Int, tipContentBean: TipContentBean?) {
    initTipView()
    initTipViewConfig(tipConfig, tipContentBean)
    errorOrEmptyView?.visibility = View.VISIBLE
  }

  override fun hideTipView() {
    errorOrEmptyView?.visibility = View.GONE
  }

  val isShowing: Boolean
    get() = errorOrEmptyView?.visibility == View.VISIBLE

  private fun initTipViewConfig(
    tipConfig: Int,
    tipContentBean: TipContentBean? = getTipContentBean(tipConfig)
  ) {
    if (null == errorOrEmptyView || null == tipContentBean) {
      return
    }
    errorOrEmptyView!!.apply {
      initErrorImg(tipContentBean.imageRes)
      showErrorView(tipContentBean.text, tipContentBean.textHint)
      initBtnTxt(tipContentBean.btnContent)
      tag = tipConfig
      setImgSize(sizeImage, mTopPadding)
      setBackgroundColor(tipContentBean.backgroundColor ?: Color.WHITE)
      if (mGravity > 0) {
        gravity = mGravity
      }
      setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom)
      setOnRefreshListener(object : ErrorOrEmptyView.OnRefreshListener {
        override fun onClickRefresh() {
          onTipViewClickListener?.invoke(tipConfig)
        }
      })
    }
  }
}