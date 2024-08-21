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
 * 问卷问题
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_question", 
	indexes = { @Index(columnList = "Id"), @Index(columnList = "questionnaireId")})
public class Question extends BaseEntity {
	
	/**
	 * 问卷ID
	 */
	private Long questionnaireId;
	
	/**
	 * 问题编号
	 */
	private Integer number;
	
	/**
	 * 问题
	 */
	@Column(columnDefinition = "TEXT")
	private String content;
	
	/**
	 * 问题说明
	 */
	@Column(columnDefinition = "TEXT")
	private String description;
	
	/**
	 * 问题类型
	 */
	private Integer questionType;
	
	/**
	 * 问题选项
	 */
	@Column(columnDefinition = "TEXT")
	private String questionSelect;
}
