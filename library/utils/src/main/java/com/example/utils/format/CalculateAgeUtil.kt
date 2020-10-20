package com.example.utils.format

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 计算年龄工具类
 *
 * 完整的年龄格式为：xx岁xx个月xx天
 */
object CalculateAgeUtil {
    private val calS = Calendar.getInstance()

    /**
     * 计算年龄核心类
     *
     * 字符串时间格式 yyyy-MM-dd
     *
     * 注意：开始时间 < 结束时间
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
	@JvmStatic
	fun calculate(startDate: Date?, endDate: Date): String {
        calS.time = startDate
        val startY = calS[Calendar.YEAR]
        val startM = calS[Calendar.MONTH]
        val startD = calS[Calendar.DATE]
        val startDayOfMonth =
            calS.getActualMaximum(Calendar.DAY_OF_MONTH)
        calS.time = endDate
        val endY = calS[Calendar.YEAR]
        val endM = calS[Calendar.MONTH]
        val endD = calS[Calendar.DATE]
        val sBuilder = StringBuilder()
        if (endDate.compareTo(startDate) < 0) {
            //结束日期小于开始日期，不合法
            return sBuilder.append("").toString()
        }
        val rY = endY - startY
        val rM = endM - startM
        val rD = endD - startD

        // 当天	
        if (rY == 0 && rM == 0 && rD == 0) {
            return sBuilder.append(1).append("天").toString()
        }

        // 本年
        if (rY == 0) {
            return if (rD > 0) {
                if (rM > 0) {
                    sBuilder.append(rM).append("个月")
                }
                sBuilder
                    .append(rD + 1).append("天")
                    .toString()
            } else if (rD == 0) {
                sBuilder
                    .append(rM).append("个月")
                    .toString()
            } else {
                if (rM - 1 > 0) {
                    sBuilder.append(rM - 1).append("个月")
                }
                sBuilder
                    .append(startDayOfMonth - startD + endD + 1).append("天")
                    .toString()
            }
        }

        // 超过一年
        return if (rD > 0) {
            // 天数大于0
            if (rM > 0) {
                // 月份大于0
                sBuilder.append(rY).append("岁")
                    .append(rM).append("个月")
            } else if (rM == 0) {
                // 月份等于0
                sBuilder.append(rY).append("岁")
            } else {
                // 月份小于0
                if (rY - 1 > 0) {
                    sBuilder.append(rY - 1).append("岁")
                }
                sBuilder.append(12 - startM + endM).append("个月")
            }
            sBuilder
                .append(rD).append("天")
                .toString()
        } else if (rD == 0) {
            // 天数等于0
            if (rM > 0) {
                // 月份大于0
                sBuilder.append(rY).append("岁")
                    .append(rM).append("个月")
            } else if (rM == 0) {
                // 月份等于0
                sBuilder.append(rY).append("岁")
            } else {
                // 月份小于0
                if (rY - 1 > 0) {
                    sBuilder.append(rY - 1).append("岁")
                }
                sBuilder.append(12 - startM + endM).append("个月")
            }
            sBuilder.toString()
        } else {
            // 天数小于0
            if (rM - 1 > 0) {
                // 月份大于0
                sBuilder.append(rY).append("岁")
                    .append(rM - 1).append("个月")
            } else if (rM - 1 == 0) {
                // 月份等于0
                sBuilder.append(rY).append("岁")
            } else {
                // 月份小于0
                if (rY - 1 > 0) {
                    sBuilder.append(rY - 1).append("岁")
                }
                sBuilder.append(12 - startM + endM - 1).append("个月")
            }
            sBuilder
                .append(startDayOfMonth - startD + endD).append("天")
                .toString()
        }
    }

    @JvmStatic
	fun getDate(time: String): Date {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA) // 小写的mm表示的是分钟
            sdf.parse(time)!!
        } catch (e: ParseException) {
            e.printStackTrace()
            Date()
        }
    }
}