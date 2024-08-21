package com.freebe.code.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class IdUtils {
	
	public static List<Long> toIds(String ids) {
		if(StringUtils.isEmpty(ids)) {
			return null;
		}
		List<String> values = JSONObject.parseArray(ids, String.class);
		List<Long> ret = new ArrayList<>();
		for(String value : values) {
			if(StringUtils.isEmpty(value)) {
				continue;
			}
			if(!value.startsWith("_")) {
				ret.add(Long.parseLong(value));
				continue;
			}
			ret.add(Long.parseLong(value.substring(1, value.length() - 1)));
		}
		return ret;
	}
	
	public static String toStringId(Long id) {
		if(null == id) {
			return null;
		}
		return "_" + id + "_";
	}
	
	public static String toIdStr(List<Long> ids) {
		if(null == ids || ids.size() == 0) {
			return null;
		}
		List<String> values = new ArrayList<>();
		for(Long id : ids) {
			values.add(toStringId(id));
		}
		return JSONObject.toJSONString(values);
	}
}
