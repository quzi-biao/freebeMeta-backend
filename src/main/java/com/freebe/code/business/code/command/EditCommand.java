package com.freebe.code.business.code.command;

import com.freebe.code.business.code.ComponentBuilder;

import lombok.Data;

/**
 * 编辑指令
 * @author zhengbiaoxie
 *
 */
@Data
public class EditCommand {
	
	/**
	 * 动作类型，增删改查
	 */
	private ActionEnum action;
	
	/**
	 * 关联的组件类
	 */
	private Class<? extends ComponentBuilder> componentBuilder;
	
}
