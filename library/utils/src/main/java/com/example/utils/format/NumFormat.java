package com.example.utils.format;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 数字格式化工具类
 */
public class NumFormat {

	private volatile static NumFormat instance;

	// 保证单一实例
	private volatile static DecimalFormat decimalFormat;

	private NumFormat() {
		decimalFormat = new DecimalFormat();
	}

	public static NumFormat get() {
		if (instance == null) {
			synchronized (NumFormat.class) {
				if (instance == null) {
					instance = new NumFormat();
				}
			}
		}
		return instance;
	}

    /**
     * 四舍五入
     * @param pattern
     * @param value
     * @return
     */
    public String formatCeiling(String pattern, float value) {
        String valueStr = String.valueOf(value);
        if (TextUtils.isEmpty(pattern)) return valueStr;
        try {
            decimalFormat.applyPattern(pattern);
            decimalFormat.setRoundingMode(RoundingMode.CEILING);
            BigDecimal bd = new BigDecimal(String.valueOf(value));
            return decimalFormat.format(bd);
        } catch (IllegalArgumentException e) {
            return valueStr;
        }
    }

    /**
     * 非四舍五入
     * @param pattern
     * @param value
     * @return
     */
    public String formatFloor(String pattern, float value) {
        String valueStr = String.valueOf(value);
        if (TextUtils.isEmpty(pattern)) return valueStr;
        try {
            decimalFormat.applyPattern(pattern);
            decimalFormat.setRoundingMode(RoundingMode.FLOOR);
            BigDecimal bd = new BigDecimal(String.valueOf(value));
            return decimalFormat.format(bd);
        } catch (IllegalArgumentException e) {
            return valueStr;
        }
    }

}
