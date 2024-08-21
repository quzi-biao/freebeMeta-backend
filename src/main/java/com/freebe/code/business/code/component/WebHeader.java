package com.freebe.code.business.code.component;

import java.util.Arrays;
import java.util.List;

import com.freebe.code.business.code.Component;
import com.freebe.code.business.code.ComponentBuilder;

/**
 * 网页端的 Header
 * @author zhengbiaoxie
 *
 */
public class WebHeader implements ComponentBuilder {


	@Override
	public List<Component> build() {
		Component component = new Component("div");
		
		component.getStyles()
			.add("width", "100%")
			.add("height", "72px")
			.add("position", "fixed")
			.add("top", "0")
			.add("left", "0")
			.add("line-height", "40px")
			.add("padding-left", "20px")
			.add("z-index", "99")
			.add("background-color", "white")
			.add("border-bottom", "solid 1px #ddd");
		
		component.getComponents().add(new ImageBuilder().build().get(0));
		Component container = new Component("div");
		container.getStyles()
			.add("margin-top", "72px")
			.add("width", "100%");
		
		return Arrays.asList(component, container);
	}

	@Override
	public String componentNames() {
		return "导航栏|顶部导航栏|顶部静态导航栏|顶部固定导航栏|顶部Header|静态Header|固定Header";
	}

}
