package com.freebe.code.business.base.entity;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 可交互的对象
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class InteractionEntity extends BaseEntity {
	/**
	 * 喜欢的数量
	 */
	private Long favorite;
	
	/**
	 * 收藏的数量
	 */
	private Long collect;
	
	/**
	 * 分享
	 */
	private Long share;
	
	/**
	 * 评论
	 */
	private Long comment;
}
