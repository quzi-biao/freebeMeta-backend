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
@Table(name="t_advanture_task", 
	indexes = { @Index(columnList = "Id")})
public class AdvantureTask extends BaseEntity {
	/**
	 * 任务标题
	 */
	private String title;
	
	/**
	 * 任务说明
	 */
	private String description;
	
	/**
	 * 奖励经验值
	 */
	private Long experience;
	
	/**
	 * 任务状态，0 表示正常，1 表示关闭
	 */
	private Integer status;
	
	/**
	 * 任务创建者
	 */
	private Long creator;
	
	/**
	 * 任务等级
	 */
	private Integer taskLevel;
}
