package com.example.learn.ui.viewattribute

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.ColorInt
import com.example.learn.R
import com.example.log.LogUtil
import com.example.utils.device.DensityUtil.dp2px
import kotlinx.android.synthetic.main.view_attribute.view.*

/**
 * @author fqxyi
 * @date 2017/5/21
 * 属性的设置与使用
 */
class ViewAttribute @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_attribute, this)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        setPadding(
            dp2px(context, 20f), dp2px(context, 5f),
            dp2px(context, 20f), dp2px(context, 5f)
        )
        if (null != attrs) {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.ViewAttribute)
            //
            val text = typeArray.getString(R.styleable.ViewAttribute_view_attr_text)
            viewText.text = text
            //
            val color = typeArray.getColor(R.styleable.ViewAttribute_view_attr_text_color, Color.BLUE)
            viewText.setTextColor(color)
            //
            val rounded = typeArray.getBoolean(R.styleable.ViewAttribute_view_attr_round, false)
            if (rounded) {
                viewImage.setImageResource(R.mipmap.ic_launcher_round)
            } else {
                viewImage.setImageResource(R.mipmap.ic_launcher)
            }
            //
            typeArray.recycle()
        }
    }

    fun setViewText(text: String?) {
        viewText.text = text
    }

    fun setViewTextColor(@ColorInt color: Int) {
        try {
            viewText.setTextColor(color)
        } catch (e: Exception) {
            LogUtil.e(e)
            Toast.makeText(context, "color id is error", Toast.LENGTH_SHORT).show()
        }
    }

    fun setViewImageRounded(rounded: Boolean) {
        if (rounded) {
            viewImage.setImageResource(R.mipmap.ic_launcher_round)
        } else {
            viewImage.setImageResource(R.mipmap.ic_launcher)
        }
    }
}