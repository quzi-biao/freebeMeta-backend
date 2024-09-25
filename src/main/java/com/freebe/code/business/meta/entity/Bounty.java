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
 * 项目悬赏
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_bounty", 
	indexes = { @Index(columnList = "Id"), @Index(columnList = "projectId")})
public class Bounty extends BaseEntity {
	/**
	 * 关联的项目 ID
	 */
	private Long projectId;
	
	/**
	 * 悬赏所有者
	 */
	private Long ownerId;
	
	/**
	 * 悬赏标题
	 */
	private String title;
	
	/**
	 * 悬赏描述
	 */
	@Column(columnDefinition = "TEXT")
	private String description;
	
	/**
	 * 悬赏状态
	 */
	private Integer state;
	
	/**
	 * 悬赏限时, 限时未完成悬赏重新进入可认领的状态，前一个人的工作被标记未超时，同时记录到档案
	 */
	private Long limitTime;
	
	/**
	 * 当前认领人 ID
	 */
	private Long takerId;
	
	/**
	 * 当前认领 ID
	 */
	private Long takeId;
	
	/**
	 * 认领等待时间，超过，则悬赏自动取消
	 */
	private Long takerWaitTime;
	
	/**
	 * 悬赏审核市场
	 */
	private Long auditTime;
	
	/**
	 * 审核开始时间
	 */
	private Long auditStartTime;
	
	/**
	 * 审核等待时长
	 */
	private Long auditWaitTime;
	
	/**
	 * 悬赏赏金
	 */
	private Long reward;
	
	/**
	 * U 奖励
	 */
	private String ureward;
	
	/**
	 * 悬赏优先级
	 */
	private Integer priority;
	
	/**
	 * 前置悬赏
	 */
	@Column(columnDefinition = "TEXT")
	private String frontBounties;
	
	/**
	 * 后续悬赏
	 */
	@Column(columnDefinition = "TEXT")
	private String nextBounties;
}
