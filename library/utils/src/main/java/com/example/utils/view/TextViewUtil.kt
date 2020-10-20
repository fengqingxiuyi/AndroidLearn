package com.example.utils.view

import android.widget.TextView
import java.util.*

/**
 * TextView相关知识
 */
object TextViewUtil {

    const val UNDERLINE_TEXT_FLAG = 0x08 // 下划线
    const val STRIKE_THRU_TEXT_FLAG = 0x10 // 删除线
    const val FAKE_BOLD_TEXT_FLAG = 0x20 // 字体加粗

    /**
     * 数据填充
     */
    @JvmStatic
    fun dataFill(
        textView: TextView,
        pattern: String,
        name: String?,
        count: Int
    ) {
        textView.text = String.format(Locale.getDefault(), pattern, name, count)
    }

    /**
     * 设置TextView样式
     */
    fun setTextViewStyle(textView: TextView, flags: Int) {
        textView.paint.flags = flags
    }
}