package com.freebe.code.business.code.command;

import java.util.HashMap;
import java.util.Map;

import com.freebe.code.business.code.ComponentBuilder;
import com.freebe.code.business.code.component.AiCodeBar;
import com.freebe.code.business.code.component.AvatorBuilder;
import com.freebe.code.business.code.component.CardBuilder;
import com.freebe.code.business.code.component.ImageBuilder;
import com.freebe.code.business.code.component.MenuBuilder;
import com.freebe.code.business.code.component.WebHeader;

/**
 * 匹配动作
 * @author zhengbiaoxie
 *
 */
public class ActionMatcher {
	private static final String ADD = "添加|新增|创建|增加|补上|加|添|创造|新添|Add|create|put";
	
	private static Map<String, ActionEnum> actionMap = new HashMap<>();
	
	private static Map<String, Class<? extends ComponentBuilder>> builderClazz = new HashMap<>();
	
	public static EditCommand match(String input) {
		EditCommand command = new EditCommand();
		
		for(String key : actionMap.keySet()) {
			if(input.indexOf(key) >= 0) {
				ActionEnum action = actionMap.get(key);
				command.setAction(action);
				command.setComponentBuilder(matchBuilder(input));
				
				return command;
			}
		}
		command.setAction(ActionEnum.CSS);
		return command;
	}
	
	/**
	 * 匹配构建器
	 * @param input
	 * @return
	 */
	private static Class<? extends ComponentBuilder> matchBuilder(String input) {
		for(String key : builderClazz.keySet()) {
			if(input.indexOf(key) >= 0) {
				return builderClazz.get(key);
			}
		}
		return null;
	}

	private static void addBuilderClazz(ComponentBuilder builder) {
		String names = builder.componentNames();
		String[] words = names.split("\\|");
		for(String word : words) {
			builderClazz.put(word, builder.getClass());
		}
	}
	
	static {
		String[] words = ADD.split("\\|");
		for(String word : words) {
			actionMap.put(word, ActionEnum.ADD);
		}
		
		addBuilderClazz(new WebHeader());
		addBuilderClazz(new MenuBuilder());
		addBuilderClazz(new ImageBuilder());
		addBuilderClazz(new CardBuilder());
		addBuilderClazz(new AvatorBuilder());
		addBuilderClazz(new AiCodeBar());
		System.out.println(builderClazz);
	}
}
