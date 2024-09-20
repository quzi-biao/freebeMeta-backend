package com.freebe.code.business.graph;

import lombok.Data;

/**
 * 图节点信息
 * @author zhengbiaoxie
 *
 * @param <T>
 */
@Data
public class FreeNode<T, ID> {
	
	/**
	 * 节点数据
	 */
	private T node;
	
	/**
	 * 节点 ID
	 */
	private ID id;
}
