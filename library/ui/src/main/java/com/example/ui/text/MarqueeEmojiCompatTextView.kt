package com.example.ui.text

import android.content.Context
import android.util.AttributeSet
import com.example.ui.utils.isRtlLayout

class MarqueeEmojiCompatTextView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null,
) : UnichatEmojiCompatTextView(context, attrs) {

  private var layoutDirect = LAYOUT_DIRECTION_RTL
  private var textDirect = TEXT_DIRECTION_RTL

  override fun isFocused(): Boolean {
    return true
  }

  override fun getLayoutDirection(): Int {
    return layoutDirect
  }

  override fun getTextDirection(): Int {
    return textDirect
  }

  override fun setText(text: CharSequence?, type: BufferType?) {
    if (isRtlLayout(context)) {
      layoutDirect = LAYOUT_DIRECTION_RTL
      textDirect = TEXT_DIRECTION_RTL
    } else {
      layoutDirect = LAYOUT_DIRECTION_LTR
      textDirect = TEXT_DIRECTION_LTR
    }
    super.setText(text, type)
  }
}