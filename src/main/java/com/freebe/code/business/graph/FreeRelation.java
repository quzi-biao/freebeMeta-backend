package com.freebe.code.business.graph;

import lombok.Data;

/**
 * 图关系
 * @author zhengbiaoxie
 *
 * @param <ID>
 */
@Data
public class FreeRelation<ID> {
	/**
	 * 源节点
	 */
	private ID srcNode;
	
	/**
	 * 目标节点
	 */
	private ID dstNode;
}
