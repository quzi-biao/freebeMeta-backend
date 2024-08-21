package com.freebe.code.business.code;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 组件
 * @author zhengbiaoxie
 *
 */
@Data
public class Component {
	/**
	 * 样式
	 */
	private Styles styles = Styles.create();
	
	/**
	 * 子组件
	 */
	private List<Component> components = new ArrayList<>();
	
	/**
	 * 属性
	 */
	private List<ViewAttribute<?>> attributes = new ArrayList<>();
	
	/**
	 * 标签
	 */
	private String elementTag;
	
	/**
	 * 内容
	 */
	private String content;
	
	public Component(String elementTag) {
		this.elementTag = elementTag;
	}
	
	public Component(String elementTag, String content) {
		this(elementTag);
		this.content = content;
	}
}
