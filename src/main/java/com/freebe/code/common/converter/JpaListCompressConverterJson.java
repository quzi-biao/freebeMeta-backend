package com.freebe.code.common.converter;

import java.util.List;

import javax.persistence.AttributeConverter;

import com.alibaba.fastjson.JSONObject;
import com.freebe.code.util.CompressUtils;

/**
 * 数组变换
 * @author xiezhengbiao
 *
 */
public class JpaListCompressConverterJson implements AttributeConverter<List<?>, byte[]> {
	private static final String CLAZZ_KEY = "clazz";
	private static final String CONTENT_KEY = "cotent";
	
	@Override
	public byte[] convertToDatabaseColumn(List<?> attribute) {
		if(null == attribute || attribute.size() == 0) {
			return null;
		}
		JSONObject jo = new JSONObject();
		jo.put(CLAZZ_KEY, attribute.get(0).getClass());
		jo.put(CONTENT_KEY, JSONObject.toJSONString(attribute));
		
		String value = JSONObject.toJSONString(jo);
		
		return CompressUtils.gzipCompress(value);
	}

	@Override
	public List<?> convertToEntityAttribute(byte[] dbData) {
		if(null == dbData) {
			return null;
		}
		try {
			byte[] jsonByte = CompressUtils.gzipUncompress(dbData);
			
			if(null == jsonByte) {
				return null;
			}
			String json = new String(jsonByte);
			
			JSONObject jo = JSONObject.parseObject(json);
			
			String clazzName = jo.getString(CLAZZ_KEY);
			String content = jo.getString(CONTENT_KEY);
			return JSONObject.parseArray(content, Class.forName(clazzName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
