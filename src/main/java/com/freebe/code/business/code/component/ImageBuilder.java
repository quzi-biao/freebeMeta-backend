package com.freebe.code.business.code.component;

import java.util.Arrays;
import java.util.List;

import com.freebe.code.business.code.Component;
import com.freebe.code.business.code.ComponentBuilder;
import com.freebe.code.business.code.ViewAttribute;

/**
 * 图片
 * @author zhengbiaoxie
 *
 */
public class ImageBuilder implements ComponentBuilder {
	
	@Override
	public List<Component> build() {
		Component component = new Component("img");
		
		component.getStyles()
			.add("object-fit", "cover")
			.add("height", "64px");
		
		component.getAttributes().add(new ViewAttribute<String>("src", "https://water-dev-waterai.oss-cn-hangzhou.aliyuncs.com/log.png"));
		
		return Arrays.asList(component);
	}

	@Override
	public String componentNames() {
		return "图|图片|图像|Picture|Photo|图画|插画";
	}
}
