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
 * 问卷单题答案
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_single_answer", 
	indexes = { @Index(columnList = "Id"), @Index(columnList = "questionnaireId"), @Index(columnList = "userId")})
public class QuestionnaireSingleAnswer extends BaseEntity {
	/**
	 * 问卷 ID
	 */
	private Long questionnaireId;
	
	/**
	 * 问题 ID
	 */
	private Long questionId;
	
	/**
	 * 答题 ID
	 */
	private Long answerId;
	
	/**
	 * 成员 ID
	 */
	private Long userId;
	
	/**
	 * 问题答案
	 */
	@Column(columnDefinition = "TEXT")
	private String answer;
}
