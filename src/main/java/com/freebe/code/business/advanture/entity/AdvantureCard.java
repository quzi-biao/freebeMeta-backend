package com.freebe.code.business.advanture.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 冒险卡片
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_advanture_card", 
	indexes = { @Index(columnList = "Id")})
public class AdvantureCard extends BaseEntity {
	/**
	 * 卡片持有者 ID
	 */
	private Long userId;
	
	/**
	 * 冒险开始时间
	 */
	private Long startTime;
	
	/**
	 * 冒险结束时间
	 */
	private Long endTime;
	
	/**
	 * 经验值
	 */
	private Long experience;
	
	/**
	 * 任务类型
	 */
	private Integer taskTypeId;
}
