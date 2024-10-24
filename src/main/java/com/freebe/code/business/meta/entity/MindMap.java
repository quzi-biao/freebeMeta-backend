package com.freebe.code.business.meta.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 思维导图
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_mind_map", 
	indexes = { @Index(columnList = "Id")})
public class MindMap extends BaseEntity {
	
	/**
	 * 所有者
	 */
	private Long ownerId;
	
	/**
	 * 归属的项目
	 */
	private Long projectId;
	
	/**
	 * 导图内容 json 格式
	 */
	@Column(columnDefinition = "TEXT")
	private String content;
}
