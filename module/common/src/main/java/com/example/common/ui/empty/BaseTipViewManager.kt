package com.example.common.ui.empty

import android.app.Activity
import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.common.global.AppGlobal
import com.example.common.ui.empty.bean.TipContentBean
import com.example.common.ui.empty.utils.ErrorImageAndTxtUtils
import com.example.common.ui.empty.utils.ErrorViewUtils.addErrorLayout
import com.example.utils.device.DensityUtil
import com.example.utils.device.StatusBarUtil

/**
 * 略
 *
 * @author: shenbf
 */
abstract class BaseTipViewManager {

  private val tipContentBeanSparseArray by lazy { SparseArray<TipContentBean?>() }
  protected var instance: Any? = null
  protected var onTipViewClickListener: ((tipConfig: Int) -> Unit)? = null
  protected var topHeight = 0
  private var dp44 = DensityUtil.dp2px(AppGlobal.appContext, 44f)

  constructor(instance: Activity) {
    init(instance)
  }

  constructor(instance: Fragment) {
    init(instance)
  }

  /***
   * viewGroup 必须是FrameLayout 或者是 RelativeLayout 或者是 ConstraintLayout
   * @param instance
   */
  constructor(instance: ViewGroup) {
    init(instance)
  }

  fun build(topHeight: Int): BaseTipViewManager {
    this.topHeight = topHeight
    changeViewLayoutParams()
    return this
  }

  fun build(): BaseTipViewManager {
    topHeight = 0
    changeViewLayoutParams()
    return this
  }

  fun buildDefault(): BaseTipViewManager {
    if (null == context) {
      return this
    }
    topHeight = dp44
    changeViewLayoutParams()
    return this
  }

  fun buildHsionBar(): BaseTipViewManager {
    if (null == context) {
      return this
    }
    topHeight = dp44 + StatusBarUtil.getStatusBarHeight(AppGlobal.appContext)
    changeViewLayoutParams()
    return this
  }

  protected abstract fun changeViewLayoutParams()
  protected fun init(instance: Any?) {
    this.instance = instance
  }

  abstract fun showTipView(tipConfig: Int)

  abstract fun showTipView(tipConfig: Int, tipContentBean: TipContentBean? = null)

  abstract fun hideTipView()

  protected fun getTipContentBean(tipConfig: Int): TipContentBean? {
    var tipContentBean = tipContentBeanSparseArray[tipConfig, null]
    if (null == tipContentBean) {
      tipContentBean = ErrorImageAndTxtUtils.getTipContentBean(tipConfig)
      putTipContentBean(tipConfig, tipContentBean)
    }
    return tipContentBean
  }

  protected fun putTipContentBean(tipConfig: Int, tipContentBean: TipContentBean?) {
    tipContentBeanSparseArray.put(tipConfig, tipContentBean)
  }

  val context: Context?
    get() {
      if (null != instance) {
        if (instance is Activity) {
          return instance as Activity?
        }
        if (instance is Fragment) {
          return (instance as Fragment).context
        }
        if (instance is ViewGroup) {
          return (instance as ViewGroup).context
        }
      }
      return null
    }

  protected fun addContentView(view: View?) {
    if (null != instance) {
      if (instance is Activity) {
        addErrorLayout((instance as Activity?)!!, view, topHeight)
      }
      if (instance is Fragment) {
        addErrorLayout(instance as Fragment?, view, topHeight)
      }
      if (instance is ViewGroup) {
        addErrorLayout(instance as ViewGroup?, view, topHeight)
      }
    }
  }

  fun onDestroy() {
    tipContentBeanSparseArray.clear()
    instance = null
  }

  /***
   * 获取 相关类型
   * @param tipConfig
   * @return
   */
  fun buildTipContentBean(tipConfig: Int): TipContentBean? {
    return ErrorImageAndTxtUtils.getTipContentBean(tipConfig)
  }

  fun setTipViewClickListener(onTipViewClickListener: (tipConfig: Int) -> Unit) {
    this.onTipViewClickListener = onTipViewClickListener
  }

}