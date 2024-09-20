package com.freebe.code.business.graph;

import java.util.List;

import lombok.Data;

/**
 * 图数据
 * @author zhengbiaoxie
 *
 * @param <T>
 * @param <ID>
 */
@Data
public class FreeGraph<T, ID> {
	/**
	 * 节点数据
	 */
	private List<FreeNode<T, ID>> nodes;
	
	/**
	 * 关系列表
	 */
	private List<FreeRelation<ID>> relations;
}
