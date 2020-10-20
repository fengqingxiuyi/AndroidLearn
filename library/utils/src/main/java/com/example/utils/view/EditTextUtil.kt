package com.example.utils.view

import android.text.SpannableString
import android.text.Spanned
import android.text.SpannedString
import android.text.style.AbsoluteSizeSpan
import android.widget.EditText

/**
 * EditText相关知识
 */
class EditTextUtil {
    /**
     * 已在布局中设置好Hint值
     * @param editText
     * @param fontSize
     */
    private fun setHintByXML(editText: EditText, fontSize: Int) {
        val hint = editText.hint
        // 新建一个可以添加属性的文本对象
        val ss = SpannableString(hint)
        // 新建一个属性对象,设置文字的大小
        val ass = AbsoluteSizeSpan(fontSize, true)
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // 设置hint
        editText.hint = SpannedString(ss) // 一定要进行转换,否则属性会消失
    }

    /**
     * 未在布局中设置好Hint值
     * @param editText
     * @param hint
     * @param fontSize
     */
    private fun setHintByParam(
        editText: EditText,
        hint: String,
        fontSize: Int
    ) {
        // 新建一个可以添加属性的文本对象
        val ss = SpannableString(hint)
        // 新建一个属性对象,设置文字的大小
        val ass = AbsoluteSizeSpan(fontSize, true)
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // 设置hint
        editText.hint = SpannedString(ss) // 一定要进行转换,否则属性会消失
    }
}