package com.freebe.code.business.code.component;

import java.util.Arrays;
import java.util.List;

import com.freebe.code.business.code.Component;
import com.freebe.code.business.code.ComponentBuilder;

/**
 * 头像
 * @author zhengbiaoxie
 *
 */
public class AvatorBuilder implements ComponentBuilder {

	@Override
	public List<Component> build() {
		Component component = new Component("div");
		
		component.getStyles()
			.add("width", "60px")
			.add("height", "60px")
			.add("text-align", "center")
			.add("border-radius", "30px")
			.add("background-color", "white");
		
		Component avator = new ImageBuilder().build().get(0);
		avator.getStyles()
			.add("width", "100%")
			.add("height", "100%");
		
		component.getComponents().add(avator);
		
		return Arrays.asList(component);
	}

	@Override
	public String componentNames() {
		return "头像|Avator";
	}

}
