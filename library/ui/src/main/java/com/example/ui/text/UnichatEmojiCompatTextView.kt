package com.example.ui.text

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.example.ui.R


/**
 * 保证emoji不被截断
 *
 * @author shenBF
 */
open class UnichatEmojiCompatTextView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null
) : UnichatTextView(context, attrs) {

  private val TAG = "UnichatEmojiCompatTextView"
  //自定义属性
  private var adapterRecyclerView = false
  //
  private var originLayoutParamsWidth = 0
  private var originText: CharSequence? = null
  private var originBufferType: BufferType? = null
  //
  private var adjustText = ""

  init {
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.UnichatEmojiCompatTextView)
    adapterRecyclerView = typedArray.getBoolean(R.styleable.UnichatEmojiCompatTextView_adapter_recyclerview, adapterRecyclerView)
    typedArray.recycle()
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    if (adapterRecyclerView) {
      setText(adjustText(originText.toString()))
    } else if (changed && !isMarqueeView()) {
      setText(adjustText(originText.toString()))
    }
  }

  override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
    super.onWindowFocusChanged(hasWindowFocus)
//    logD(TAG, "hasWindowFocus = $hasWindowFocus")
    if (!isMarqueeView() || adapterRecyclerView) return
    if (hasWindowFocus) {
      setText(originText.toString())
    } else {
      setText(adjustText(originText.toString()))
    }
  }

  private fun adjustText(text: String?): String? {
    //过滤空字符串
    if (text.isNullOrEmpty()) return text
    //过滤宽度为0的情况
    val viewWidth = width
    if (viewWidth == 0) return text
    //固定宽度
    if (adapterRecyclerView) {
      layoutParams?.width = viewWidth
    }
    //过滤长度不够的字符串
    val paddingStart = paddingStart
    val paddingEnd = paddingEnd
    if (paint.measureText(text) + paddingStart + paddingEnd <= viewWidth) return text
    //获取适应宽度之后的文案
    adjustText = getAdjustText(text, ELLIPSIS)
    return adjustText
  }

  override fun setText(text: CharSequence?, type: BufferType?) {
    //重置固定的宽度
    if (adapterRecyclerView && (layoutParams?.width ?: 0) > 0 && originLayoutParamsWidth != 0) {
      layoutParams?.width = originLayoutParamsWidth
    }
    originText = text
    originBufferType = type
    super.setText(text, type)
    //强制执行onLayout方法
    requestLayout()
  }

  private fun setText(text: String?) {
    super.setText(text, originBufferType)
  }

  private fun isMarqueeView(): Boolean {
    return ellipsize == TextUtils.TruncateAt.MARQUEE
  }

  override fun getText(): CharSequence {
    return originText ?: ""
  }

  override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
    super.setLayoutParams(params)
    originLayoutParamsWidth = params?.width ?: 0
  }

}