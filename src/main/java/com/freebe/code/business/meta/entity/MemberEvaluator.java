package com.freebe.code.business.meta.entity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.freebe.code.business.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 成员能力评估
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_member_evaluator", indexes = { @Index(columnList = "Id")})
public class MemberEvaluator extends BaseEntity {
	/**
	 * 小时价值，等于 任务总回报/任务总耗时*(8/24)，我们默认 8 小时工作制
	 */
	private Long hourValue;
	
	/**
	 * 总回报
	 */
	private Long totalReward;
	
	/**
	 * 总时间
	 */
	private Long totalHours;
	
	/**
	 * 总任务数
	 */
	private Long totalTakeTask;
	
	/**
	 * 总超时任务数
	 */
	private Long timeoutTask;
	
	/**
	 * 平均评分
	 */
	private Double avgScore;
	
	/**
	 * 总创建的任务数
	 */
	private Long totalOwnerTask;
	
	/**
	 * 总发出的奖励
	 */
	private Long totalSendReward;
}
