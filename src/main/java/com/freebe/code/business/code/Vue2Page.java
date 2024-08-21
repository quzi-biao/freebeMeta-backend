package com.freebe.code.business.code;

import lombok.Data;

/**
 * 一个可视的页面由多个 View 节点构成
 * @author zhengbiaoxie
 *
 */
@Data
public class Vue2Page {
	/**
	 * 页面节点
	 */
	private Component template;
	
	/**
	 * 
	 */
	private Component script;
	
	/**
	 * 
	 */
	private Component style;
	
	/**
	 * 页面类型
	 */
	private String pageType;
}
