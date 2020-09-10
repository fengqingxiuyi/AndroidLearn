package com.example.utils.view;

import android.widget.TextView;

import java.util.Locale;

/**
 * TextView相关知识
 */
public class TextViewUtil {

    public static final int UNDERLINE_TEXT_FLAG = 0x08;   // 下划线
    public static final int STRIKE_THRU_TEXT_FLAG = 0x10; // 删除线
    public static final int FAKE_BOLD_TEXT_FLAG = 0x20;   // 字体加粗

    /**
     * 数据填充
     */
    public static void dataFill(TextView textView, String pattern, String name, int count) {
        textView.setText(String.format(Locale.getDefault(), pattern, name, count));
    }

    /**
     * 设置TextView样式
     */
    public static void setTextViewStyle(TextView textView, int flags) {
        textView.getPaint().setFlags(flags);
    }

}
