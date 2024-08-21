package com.freebe.code.util;

public class DoubleUtils {
	public static Double sub(Double curr, Double front) {
		if(null != curr && null != front) {
			double ret = curr - front;
			if(ret < 0) {
				return 0D;
			}
			return ret;
		}
		if(front == null && curr != null) {
			return curr;
		}
		return 0D;
	}
	
	public static Double toDouble(Object input) {
		if(null == input) {
			return 0D;
		}
		return Double.parseDouble(input.toString());
	}

	public static String format(Double input, double div) {
		if(input == null || input < 0) {
			return "0";
		}
		return String.format("%.1f", input / div);
	}
	
	public static String format3(Double input, double div) {
		if(input == null) {
			return "0";
		}
		return String.format("%.3f", input / div);
	}
	
	public static String format4(Double input, double div) {
		if(input == null) {
			return "0";
		}
		return String.format("%.4f", input / div);
	}
}
