package com.freebe.code.common.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.AttributeConverter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.freebe.code.util.CompressUtils;

public class JpaConverterJson implements AttributeConverter<Object, byte[]>{
	private static final String CLAZZ_KEY = "clazz";
	private static final String CONTENT_KEY = "cotent";
	private static final String KEY = "key";
	private static final String VALUE = "value";

	@Override
	public byte[] convertToDatabaseColumn(Object attribute) {
		if(null == attribute) {
			return null;
		}
		Object jo = toJSONObject(attribute);
		
		String value = JSONObject.toJSONString(jo);
		
		return CompressUtils.gzipCompress(value);
	}

	@Override
	public Object convertToEntityAttribute(byte[] dbData) {
		if(null == dbData) {
			return null;
		}
		try {
			byte[] jsonByte = CompressUtils.gzipUncompress(dbData);
			
			if(null == jsonByte) {
				return null;
			}
			String json = new String(jsonByte);
			Object ret = parseObject(json);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	private Object toJSONObject(Object attribute) {
		if(null == attribute) {
			return null;
		}
		JSONObject jo = new JSONObject();
		if(attribute instanceof Map) {
			jo.put(CLAZZ_KEY, Map.class);
			Map map = (Map)attribute;
			List<JSONObject> content = new ArrayList<>();
			if(map.size() > 0) {
				for(Object key : map.keySet()) {
					JSONObject entry = new JSONObject();
					entry.put(KEY, toJSONObject(key));
					entry.put(VALUE, toJSONObject(map.get(key)));
					content.add(entry);
				}
				jo.put(CONTENT_KEY, content);
			}else {
				return null;
			}
		}else {
			jo.put(CLAZZ_KEY, attribute.getClass());
			jo.put(CONTENT_KEY, attribute);
		}
		return jo;
	}
	
	private Object parseObject(String json) throws ClassNotFoundException {
		JSONObject jo = JSONObject.parseObject(json);
		if(null == jo) {
			return null;
		}
		
		String clazzName = jo.getString(CLAZZ_KEY);
		String content = jo.getString(CONTENT_KEY);
		
		Class<?> clazz = Class.forName(clazzName);
		if(clazz.equals(Map.class)) {
			return parseMap(jo);
		}else {
			if(String.class.equals(clazz)) {
				return content;
			}
			return JSONObject.parseObject(content, clazz);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object parseMap(JSONObject jo) throws ClassNotFoundException {
		String content = jo.getString(CONTENT_KEY);
		JSONArray values = JSONObject.parseArray(content);
		
		Map ret = new HashMap<>();
		for(int i = 0; i < values.size(); i++) {
			JSONObject entry = values.getJSONObject(i);
			
			Object key = parseObject(entry.getString(KEY));
			if(key == null) {
				continue;
			}
			Object value = parseObject(entry.getString(VALUE));
			
			ret.put(key, value);
		}
		
		return ret;
	}

}
