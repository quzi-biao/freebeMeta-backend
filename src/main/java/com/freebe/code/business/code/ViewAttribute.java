package com.freebe.code.business.code;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 节点属性
 * @author zhengbiaoxie
 *
 * @param <T>
 */
@Data
@AllArgsConstructor
public class ViewAttribute<T> {
	private String name;
	
	private T value;
}
