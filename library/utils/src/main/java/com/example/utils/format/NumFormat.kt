package com.example.utils.format

import android.text.TextUtils
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * 数字格式化工具类
 */
class NumFormat private constructor() {
    /**
     * 四舍五入
     * @param pattern
     * @param value
     * @return
     */
    fun formatCeiling(pattern: String?, value: Float): String {
        val valueStr = value.toString()
        return if (TextUtils.isEmpty(pattern)) valueStr else try {
            decimalFormat.applyPattern(pattern)
            decimalFormat.roundingMode = RoundingMode.CEILING
            val bd = BigDecimal(value.toString())
            decimalFormat.format(bd)
        } catch (e: IllegalArgumentException) {
            valueStr
        }
    }

    /**
     * 非四舍五入
     * @param pattern
     * @param value
     * @return
     */
    fun formatFloor(pattern: String?, value: Float): String {
        val valueStr = value.toString()
        return if (TextUtils.isEmpty(pattern)) valueStr else try {
            decimalFormat.applyPattern(pattern)
            decimalFormat.roundingMode = RoundingMode.FLOOR
            val bd = BigDecimal(value.toString())
            decimalFormat.format(bd)
        } catch (e: IllegalArgumentException) {
            valueStr
        }
    }

    companion object {
        @Volatile
        private var instance: NumFormat? = null

        // 保证单一实例
        @Volatile
        private lateinit var decimalFormat: DecimalFormat
        @JvmStatic
        fun get(): NumFormat? {
            if (instance == null) {
                synchronized(NumFormat::class.java) {
                    if (instance == null) {
                        instance = NumFormat()
                    }
                }
            }
            return instance
        }
    }

    init {
        decimalFormat = DecimalFormat()
    }
}