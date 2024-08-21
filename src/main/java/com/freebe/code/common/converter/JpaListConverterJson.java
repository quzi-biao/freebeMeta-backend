package com.freebe.code.common.converter;

import java.util.List;

import javax.persistence.AttributeConverter;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 数组变换
 * @author xiezhengbiao
 *
 */
public class JpaListConverterJson implements AttributeConverter<List<?>, String> {
	private static final String CLAZZ_KEY = "clazz";
	private static final String CONTENT_KEY = "cotent";
	
	@Override
	public String convertToDatabaseColumn(List<?> attribute) {
		if(null == attribute || attribute.size() == 0) {
			return null;
		}
		JSONObject jo = new JSONObject();
		jo.put(CLAZZ_KEY, attribute.get(0).getClass());
		jo.put(CONTENT_KEY, JSONObject.toJSONString(attribute));
		
		String value = JSONObject.toJSONString(jo);
		
		return value;
	}

	@Override
	public List<?> convertToEntityAttribute(String dbData) {
		if(StringUtils.isEmpty(dbData)) {
			return null;
		}
		try {
			JSONObject jo = JSONObject.parseObject(dbData);
			
			String clazzName = jo.getString(CLAZZ_KEY);
			String content = jo.getString(CONTENT_KEY);
			
			return JSONObject.parseArray(content, Class.forName(clazzName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
