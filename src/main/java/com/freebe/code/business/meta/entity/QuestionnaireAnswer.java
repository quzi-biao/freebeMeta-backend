package com.freebe.code.business.meta.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 问卷回答
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_answer", 
	indexes = { @Index(columnList = "Id"), @Index(columnList = "questionnaireId"), @Index(columnList = "userId")})
public class QuestionnaireAnswer extends BaseEntity {
	/**
	 * 问卷 ID
	 */
	private Long questionnaireId;
	
	/**
	 * 答题人 ID
	 */
	private Long userId;
	
	/**
	 * 是否公开
	 */
	private Boolean isPublic;
}
