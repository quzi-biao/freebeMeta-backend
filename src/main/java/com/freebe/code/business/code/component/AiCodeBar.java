package com.freebe.code.business.code.component;

import java.util.Arrays;
import java.util.List;

import com.freebe.code.business.code.Component;
import com.freebe.code.business.code.ComponentBuilder;

public class AiCodeBar implements ComponentBuilder {

	@Override
	public List<Component> build() {
		Component component = new Component("AiCode");
		return Arrays.asList(component);
	}

	@Override
	public String componentNames() {
		return "AI代码";
	}

}
