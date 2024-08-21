package com.freebe.code.business.code.component;

import java.util.Arrays;
import java.util.List;

import com.freebe.code.business.code.Component;
import com.freebe.code.business.code.ComponentBuilder;

/**
 * 卡片构建
 * @author zhengbiaoxie
 *
 */
public class CardBuilder implements ComponentBuilder {

	@Override
	public List<Component> build() {
		Component component = new Component("div");
		
		component.getStyles()
			.add("width", "300px")
			.add("height", "200px")
			.add("padding", "8px")
			.add("background-color", "white")
			.add("border", "solid 1px #ddd");
		
		
		return Arrays.asList(component);
	}

	@Override
	public String componentNames() {
		return "卡片|块|卡|信息卡";
	}

}
