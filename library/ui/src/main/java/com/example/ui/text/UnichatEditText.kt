package com.example.ui.text

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 * 统一字体
 *
 * @author shenBF
 */
open class UnichatEditText @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

  init {
    typeface = Typeface.createFromAsset(context.assets, "fonts/unichat_default.ttf")
  }

}