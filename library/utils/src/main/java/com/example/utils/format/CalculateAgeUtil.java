package com.example.utils.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 计算年龄工具类
 *
 * 完整的年龄格式为：xx岁xx个月xx天
 */
public class CalculateAgeUtil {

	private static Calendar calS = Calendar.getInstance();

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
	public static String calculate(Date startDate,Date endDate){
		calS.setTime(startDate);
		int startY = calS.get(Calendar.YEAR);
		int startM = calS.get(Calendar.MONTH);
		int startD = calS.get(Calendar.DATE);
		int startDayOfMonth = calS.getActualMaximum(Calendar.DAY_OF_MONTH);

		calS.setTime(endDate);
		int endY = calS.get(Calendar.YEAR);
		int endM = calS.get(Calendar.MONTH);
		int endD = calS.get(Calendar.DATE);
		
		StringBuilder sBuilder = new StringBuilder();
		if (endDate.compareTo(startDate) < 0) {
			//结束日期小于开始日期，不合法
			return sBuilder.append("").toString();   
		}
		
		int rY = endY - startY;
		int rM = endM - startM;
		int rD = endD - startD;
		
		// 当天	
		if(rY == 0 && rM == 0 && rD == 0){
			return sBuilder.append(1).append("天").toString();
		}
		
		// 本年
		if(rY == 0){
			if(rD > 0){
				if(rM > 0){
					sBuilder.append(rM).append("个月");
				}
				return sBuilder 
						.append(rD + 1).append("天")
						.toString();
			}else if(rD == 0){
				return sBuilder
						.append(rM).append("个月")
						.toString();
			}else{
				if((rM - 1) > 0){
					sBuilder.append((rM - 1)).append("个月");
				}
				return sBuilder
						.append((startDayOfMonth-startD)+endD  + 1).append("天")
						.toString();
			}
		}
		
		// 超过一年
		if(rD > 0){
			// 天数大于0
			if(rM > 0){
				// 月份大于0
				sBuilder.append(rY).append("岁")
					    .append(rM).append("个月");
			}else if(rM == 0){
				// 月份等于0
				sBuilder.append(rY).append("岁");
			}else{
				// 月份小于0
				if((rY - 1) > 0){
					sBuilder.append((rY - 1)).append("岁");
				}
				sBuilder.append((12-startM)+endM).append("个月");
			}
			return sBuilder 
					.append(rD).append("天")
					.toString();
		}else if(rD == 0){
			// 天数等于0
			if(rM > 0){
				// 月份大于0
				sBuilder.append(rY).append("岁")
					    .append(rM).append("个月");
			}else if(rM == 0){
				// 月份等于0
				sBuilder.append(rY).append("岁");
			}else{
				// 月份小于0
				if((rY - 1) > 0){
					sBuilder.append((rY - 1)).append("岁");
				}
				sBuilder.append((12-startM)+endM).append("个月");
			}
			return sBuilder.toString();
		}else{
			// 天数小于0
			if((rM - 1) > 0){
				// 月份大于0
				sBuilder.append(rY).append("岁")
				        .append((rM - 1)).append("个月");
			}else if((rM - 1) == 0){
				// 月份等于0
				sBuilder.append(rY).append("岁");
			}else{
				// 月份小于0
				if((rY - 1) > 0){
					sBuilder.append((rY - 1)).append("岁");
				}
				sBuilder.append(((12-startM)+endM)-1).append("个月");
			}
			return sBuilder
					.append((startDayOfMonth-startD)+endD).append("天")
					.toString();
		}
	}

	public static Date getDate(String time) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 小写的mm表示的是分钟
			return sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}
}
