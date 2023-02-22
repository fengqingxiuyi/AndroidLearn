package com.example.ui.text

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.example.ui.R
import com.example.ui.utils.DimenUtil

/**
 * 通用的渐变带阴影的TextView
 *
 * @author shenBF
 */
class UnichatGradientWithShadowTextView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : UnichatTextView(context, attrs, defStyleAttr) {

  private var enable = true

  init {
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.UnichatGradientWithShadowTextView)
    enable = typedArray.getBoolean(R.styleable.UnichatGradientWithShadowTextView_enable_effect, enable)
    typedArray.recycle()
    //
    if (enable) {
      setGradientAndShadowThenNotify()
    }
  }

  fun setGradientAndShadowThenNotify() {
    setTopToBottomShadow(
      DimenUtil.dp2px(context, 1f).toFloat(),
      ContextCompat.getColor(context, R.color.dolphin_common_text_shadow_default),
      DimenUtil.dp2px(context, 1f).toFloat(),
    )
    setGradient(
      OrientationMode.VERTICAL,
      ContextCompat.getColor(context, R.color.dolphin_common_text_gradient_start_default),
      ContextCompat.getColor(context, R.color.dolphin_common_text_gradient_end_default),
      currentTextColor
    )
    notifyChanged()
  }

}