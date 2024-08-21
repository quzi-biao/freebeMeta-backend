package com.freebe.code.business.code;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * 样式
 * @author zhengbiaoxie
 *
 */
@Data
public class Styles {
	
	/**
	 * 样式列表
	 */
	private Map<String, String> values;
	
	/**
	 * 如果关联的 css 类
	 */
	private String cssClassName;
	
	/**
	 * 创建
	 * @return
	 */
	public static Styles create() {
		Styles styles = new Styles();
		styles.setValues(new HashMap<>());
		return styles;
	}
	
	/**
	 * 添加属性
	 * @param key
	 * @param value
	 * @return
	 */
	public Styles add(String key, String value) {
		this.values.put(key, value);
		return this;
	}
}
