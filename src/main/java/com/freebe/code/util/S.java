package com.freebe.code.util;

public class S {
	
	/**
	 * 字符串拼接
	 * @param ss
	 * @return
	 */
	public static String c(Object... ss) {
		if(null == ss || ss.length == 0) {
			return null;
		}
		
		StringBuffer buf = new StringBuffer();
		for(Object s : ss) {
			buf.append(String.valueOf(s));
		}
		
		return buf.toString();
	}
}
