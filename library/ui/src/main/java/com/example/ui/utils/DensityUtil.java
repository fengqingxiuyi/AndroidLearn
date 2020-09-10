package com.example.ui.utils;

import android.content.Context;

/**
 * @author fqxyi
 * @date 2018/3/18
 * 尺寸大小转换工具类：dp与px、sp与px
 */
public class DensityUtil {

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}