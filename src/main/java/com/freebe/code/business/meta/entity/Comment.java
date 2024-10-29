package com.freebe.code.business.meta.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.InteractionEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 评论
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_comment", 
	indexes = { @Index(columnList = "Id") })
public class Comment extends InteractionEntity {
	/**
	 * 评论者
	 */
	private Long ownerId;
	
	/**
	 * 归属的内容 ID
	 */
	private Long contentId;
	
	/**
	 * 评论的回复对象
	 */
	private Long parentId;
	
	/**
	 * 回复对象的所有者
	 */
	private Long parentOwnerId;
	
	/**
	 * 内容 key，关联到 contentData
	 */
	private String contentKey;
}
