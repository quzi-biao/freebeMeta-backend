package com.freebe.code.util;

public class AbstractUtils {
	private static int LENGTH = 200;
	
	public static String getAbstract(String html) {
		if(null == html || html.length() == 0) {
			return html;
		}
		String text = html.replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "\n\t");
		if(text.length() < LENGTH) {
			return text;
		}
		return text.substring(0, LENGTH);
	}
}
