package com.example.common.ui.empty.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import com.example.common.databinding.CommonLayoutErrorEmptyViewBinding

/**
 * 网络请求 或者 服务器请求 异常显示的view
 *
 * @author: fqxyi
 */
class ErrorOrEmptyView  @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

  private var binding: CommonLayoutErrorEmptyViewBinding =
    CommonLayoutErrorEmptyViewBinding.inflate(LayoutInflater.from(context), this)

  private var onRefreshListener: OnRefreshListener? = null

  init {
    orientation = VERTICAL
    gravity = Gravity.CENTER_HORIZONTAL
    setOnClickListener { }
    binding.commonErrorPageBt.setOnClickListener {
      onRefreshListener?.onClickRefresh()
    }
  }

  fun showErrorView(errorText: String?) {
    binding.commonErrorPageTv.text = errorText
    binding.commonErrorPageHint.visibility = GONE
  }

  fun setImgSize(imgSize: Int) {
    val layoutParams = LayoutParams(imgSize, imgSize)
    layoutParams.gravity = Gravity.CENTER_HORIZONTAL
    binding.commonErrorPageImg.layoutParams = layoutParams
  }

  fun setImgSize(imgSize: Int, mTop: Int) {
    val layoutParams = LayoutParams(imgSize, imgSize)
    layoutParams.gravity = Gravity.CENTER_HORIZONTAL
    layoutParams.topMargin = mTop
    binding.commonErrorPageImg.layoutParams = layoutParams
  }

  fun showErrorView(errorText: String?, errorHintText: String?) {
    if (!TextUtils.isEmpty(errorText)) {
      binding.commonErrorPageTv.visibility = VISIBLE
      binding.commonErrorPageTv.text = errorText
    } else {
      binding.commonErrorPageTv.visibility = GONE
    }
    if (!TextUtils.isEmpty(errorHintText)) {
      binding.commonErrorPageHint.visibility = VISIBLE
      binding.commonErrorPageHint.text = errorHintText
    } else {
      binding.commonErrorPageHint.visibility = GONE
    }
  }

  fun initErrorImg(@DrawableRes resId: Int) {
    binding.commonErrorPageImg.setImageResource(resId)
  }

  fun setBtnVisible(visible: Int) {
    binding.commonErrorPageBt.visibility = visible
  }

  fun initBtnTxt(btnTxt: String?) {
    if (!TextUtils.isEmpty(btnTxt)) {
      binding.commonErrorPageBt.text = btnTxt
      binding.commonErrorPageBt.visibility = VISIBLE
    } else {
      binding.commonErrorPageBt.visibility = GONE
    }
  }

  fun initBackground(color: Int) {
    setBackgroundResource(color)
  }

  fun addBottomView(view: View?) {
    binding.bottomFram.addView(view)
  }

  fun clearBottomView() {
    binding.bottomFram.removeAllViews()
  }

  fun setOnRefreshListener(onRefreshListener: OnRefreshListener?) {
    this.onRefreshListener = onRefreshListener
  }

  interface OnRefreshListener {
    fun onClickRefresh()
  }
}