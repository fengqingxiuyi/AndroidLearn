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

/**
 * 嵌套在Recyclerview中的缺省页
 *
 * @author: shenbf
 */
open class RVTipViewManager : BaseTipViewManager {

  private var errorOrEmptyView: ErrorOrEmptyView? = null
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

  private fun initTipParams() {
    sizeImage = DensityUtil.dp2px(AppGlobal.appContext, ITipViewConfig.IImgSizeConfig.IMG_BIG_SIZE.toFloat())
    setTopModel(ITipViewConfig.IImgSizeConfig.TIP_VIEW_TOP_LAGER)
  }

  fun createTipView() : ErrorOrEmptyView {
    if (context == null) throw NullPointerException("ErrorOrEmptyView Context is Null")
    if (null == errorOrEmptyView) {
      errorOrEmptyView = ErrorOrEmptyView(context!!)
    }
    errorOrEmptyView!!.visibility = View.GONE
    return errorOrEmptyView!!
  }

  override fun changeViewLayoutParams() {
    createTipView()
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

  override fun showTipView(tipConfig: Int) {
    createTipView()
    initTipViewConfig(tipConfig)
    errorOrEmptyView?.visibility = View.VISIBLE
  }

  override fun showTipView(tipConfig: Int, tipContentBean: TipContentBean?) {
    createTipView()
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
      setBackgroundColor(tipContentBean.backgroundColor ?: Color.TRANSPARENT)
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

  fun setGravity(gravity: Int) {
    this.mGravity = gravity
  }

  fun setSizeImage(sizeImage: Int) {
    this.sizeImage = DensityUtil.dp2px(AppGlobal.appContext, sizeImage.toFloat())
  }

  /***
   * @paramsvalue IImgSizeConfig.TIP_VIEW_TOP_LAGER
   * @paramsvalue IImgSizeConfig.TIP_VIEW_TOP_SMALL
   * @paramsvalue IImgSizeConfig.TIP_VIEW_TOP_NULL
   */
  fun setTopModel(topPadding: Int) {
    this.mTopPadding = DensityUtil.dp2px(AppGlobal.appContext, topPadding.toFloat())
  }

}