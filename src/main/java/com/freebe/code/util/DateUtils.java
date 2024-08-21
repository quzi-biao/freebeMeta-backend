package com.freebe.code.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.freebe.code.common.Constants;

public class DateUtils {
	// 1 天的毫秒数
	public static final long DAY_PERIOD = 24L * 3600L * 1000L;

	
    /**
     * 格式化时间
     * @param time
     * @return
     */
    public static String formatTime(Long time) {
    	if(null == time) {
    		return null;
    	}
    	return new SimpleDateFormat(Constants.COMMON_DATE_FORMAT).format(time);
    }
    
    
    /**
     * 格式化时间
     * @param time
     * @return
     */
    public static String formatKeyDate(Long time) {
    	if(null == time) {
    		return null;
    	}
    	return new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT2).format(time);
    }
    
    /**
     * 格式化时间
     * @param time
     * @return
     */
    public static String formatChartKey(Long time) {
    	if(null == time) {
    		return null;
    	}
    	return new SimpleDateFormat(Constants.CHARTKEY_DATE_FORMAT).format(time);
    }
    

	public static String formatChartTimeKey(Long time) {
		if(null == time) {
    		return null;
    	}
    	return new SimpleDateFormat(Constants.CHARTKEY_TIME_FORMAT).format(time);
	}

    /**
     * 
     * @param date
     * @return
     */
	public static String formatDate(Date date) {
		if(null == date) {
			return null;
		}
		return new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT).format(date);
	}
	
	/**
	 * 格式化时间
	 * @param time
	 * @return
	 */
	public static String formatDate(String time) {
		try {
			Long timestamp = Long.parseLong(time);
			
			return new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT).format(timestamp);
		} catch (NumberFormatException e) {
			return time;
		}
	}

	public static String formatDate(Long time) {
		if(null == time) {
			return null;
		}
		return new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT).format(time);
	}
	
	/**
	 * 获取本日的开始时间戳
	 * @param currTime
	 * @return
	 */
	public static long getDayStartTimestamp() {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		
		long time = c.getTimeInMillis();
		return time + 1000;
	}

	/**
	 * 获取本日的结束时间戳
	 * @param currTime
	 * @return
	 */
	public static long getDayEndTimestamp() {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		
		long time = c.getTimeInMillis();
		return time + 1000;
	}
	
	/**
	 * 获取本月的开始
	 * @return
	 */
	public static Long getThisMonthStart() {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		c.setTimeInMillis(System.currentTimeMillis());
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		
		long time = c.getTimeInMillis();
		return time;
	}

	/**
	 * 获取本月的结束
	 * @return
	 */
	public static Long getThisMonthEnd() {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		c.setTimeInMillis(System.currentTimeMillis());
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, 0);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		
		long time = c.getTimeInMillis();
		return time;
	}

	public static String getToday() {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		c.setTimeInMillis(System.currentTimeMillis());
		
		StringBuffer ret = new StringBuffer();
		ret.append(c.get(Calendar.YEAR));
		int month = c.get(Calendar.MONTH) + 1;
		if(month > 10) {
			ret.append(month);
		}else {
			ret.append('0').append(month);
		}
		ret.append(c.get(Calendar.DAY_OF_MONTH));
		
		return ret.toString();
	}

	/**
	 * 转成 1小时55分钟32秒
	 * @param keepTime
	 * @return
	 */
	public static String toHourStr(Long keepTime) {
		long tsecond = keepTime / 1000;
		long hour = tsecond / 3600;
		long minute = (tsecond % 3600) / 60;
		long second = (tsecond % 3600) % 60;
		StringBuffer buf = new StringBuffer();
		if(hour > 0) {
			buf.append(hour).append("小时");
		}
		if(minute > 0) {
			buf.append(minute).append("分钟");
		}
		if(second > 0) {
			buf.append(second).append("秒");
		}
		return buf.toString();
	}

}
