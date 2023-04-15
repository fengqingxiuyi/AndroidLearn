package com.example.learn.uiutils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * @author shenBF
 */
public class UIUtils {

    private Context context;

    //标准值 初始化时序可以在ContentProvider初始化时
    private static final float STANDARD_WIDTH = 1080;
    private static final float STANDARD_HEIGHT = 1920;
    //实际值
    private static float displayMetricsWidth;
    private static float displayMetricsHeight;

    //单例
    private static UIUtils instance;

    public static UIUtils getInstance(Context context) {
        if (instance == null) {
            instance = new UIUtils(context);
        }
        return instance;
    }

    private UIUtils(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (displayMetricsWidth == 0f || displayMetricsHeight == 0f) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            int systemBarHeight = getSystemBarHeight(context);
            if (displayMetrics.widthPixels > displayMetrics.heightPixels) { //横屏
                displayMetricsWidth = displayMetrics.heightPixels;
                displayMetricsHeight = displayMetrics.widthPixels - systemBarHeight;
            } else { //竖屏
                displayMetricsWidth = displayMetrics.widthPixels;
                displayMetricsHeight = displayMetrics.heightPixels - systemBarHeight;
            }
        }
    }

    private static final String DIMEN_CLASS = "com.android.internal.R$dimen";

    private int getSystemBarHeight(Context context) {
        return getValue(context, DIMEN_CLASS, "system_bar_height", 48);
    }

    private int getValue(Context context, String dimenClass, String system_bar_height, int defaultSystemBarHeight) {
        try {
            Class<?> clazz = Class.forName(dimenClass);
            Object object = clazz.newInstance();
            Field field = clazz.getField(system_bar_height);
            int id = Integer.parseInt(field.get(object).toString());
            return context.getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultSystemBarHeight;
    }

    public float getHorValue() {
        return ((float) displayMetricsWidth) / STANDARD_WIDTH;
    }

    public float getVerValue() {
        return ((float) displayMetricsHeight) / (STANDARD_HEIGHT - getSystemBarHeight(context));
    }

}
