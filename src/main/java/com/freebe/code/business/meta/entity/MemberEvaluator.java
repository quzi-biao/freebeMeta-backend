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
	 * 小时价值，等于 悬赏总回报/悬赏总耗时*(8/24)，我们默认 8 小时工作制
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
	 * 总悬赏数
	 */
	private Long totalTakeBounty;
	
	/**
	 * 总超时悬赏数
	 */
	private Long timeoutBounty;
	
	/**
	 * 平均评分
	 */
	private Double avgScore;
	
	/**
	 * 总创建的悬赏数
	 */
	private Long totalOwnerBounty;
	
	/**
	 * 总发出的奖励
	 */
	private Long totalSendReward;
}
