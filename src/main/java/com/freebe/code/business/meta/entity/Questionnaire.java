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
 * 问卷
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_questionnaire", 
	indexes = { @Index(columnList = "Id")})
public class Questionnaire extends BaseEntity {
	/**
	 * 问卷所有者 ID
	 */
	private Long ownerId;
	
	/**
	 * 问卷标题
	 */
	@Column(columnDefinition = "TEXT")
	private String title;
	
	/**
	 * 问卷背景图
	 */
	@Column(columnDefinition = "TEXT")
	private String picture;
	
	/**
	 * 问卷说明
	 */
	@Column(columnDefinition = "TEXT")
	private String description;
	
	/**
	 * 问卷截止日期
	 */
	private Long deadLine;
}
