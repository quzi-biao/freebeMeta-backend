package com.freebe.code.util;

public class ObjectUtils {
	/**
	 * 空转 null
	 * @param <T>
	 * @param obj
	 * @return
	 */
	public static <T> T empty2Null(T obj) {
		if(null == obj) {
			return null;
		}
		if(obj.toString().length() == 0) {
			return null;
		}
		if(obj instanceof Number) {
			if(((Number) obj).longValue() == 0) {
				return null;
			}
		}
		return obj;
	}
	
	/**
	 * null 转空
	 * @param <T>
	 * @param obj
	 * @return
	 */
	public static <T> String str(T obj) {
		if(null == obj) {
			return "";
		}
		return obj.toString();
	}
	
	/**
	 * 获取布尔值
	 * @param bool
	 * @param defaultValue
	 * @return
	 */
	public static boolean getBool(Boolean bool, boolean defaultValue) {
		if(null == bool) {
			return defaultValue;
		}
		return bool.booleanValue();
	}
}
