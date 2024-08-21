package com.freebe.code.business.code;

import java.util.List;

/**
 * 预置的组件库
 * @author zhengbiaoxie
 *
 */
public interface ComponentBuilder {
	
	/**
	 * 
	 * @return
	 */
	List<Component> build();
	
	/**
	 * 组件名称，用于文本理解
	 * @return
	 */
	String componentNames();
}
