package com.freebe.code.util;

import java.util.HashMap;
import java.util.Map;

import com.freebe.code.common.CustomException;

import cn.hutool.core.lang.UUID;

/**
 * 编码工具
 * @author xiezhengbiao
 *
 */
public class CodeUtils {
	public static final Map<Class<?>, String> PREFFIXES = new HashMap<>();
	
	/**
	 * 年份的后两位，今年所在的天数加上 120
	 * @param count 
	 * @param preffix
	 * @return
	 * @throws CustomException 
	 */
	public synchronized static String generateCode(Class<?> clazz) throws CustomException {
		return UUID.randomUUID().toString().replace("-", "");
	}

}
