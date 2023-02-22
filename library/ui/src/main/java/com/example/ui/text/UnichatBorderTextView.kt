package com.example.ui.text

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.example.ui.R
import com.example.ui.utils.DimenUtil

/**
 * 通用的带边框的TextView
 *
 * @author shenBF
 */
class UnichatBorderTextView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : UnichatTextView(context, attrs, defStyleAttr) {

  private var borderColor = ContextCompat.getColor(context, R.color.dolphin_common_text_border_stroke)
  private var borderWidth = DimenUtil.dp2pxFloat(context, 1.5f)

  init {
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.UnichatBorderTextView)
    borderColor = typedArray.getColor(R.styleable.UnichatBorderTextView_border_color, borderColor)
    borderWidth =
      typedArray.getDimension(R.styleable.UnichatBorderTextView_border_width, borderWidth)
    typedArray.recycle()
    //
    setStrokeThenNotify(
      borderColor,
      borderWidth
    )
  }

}