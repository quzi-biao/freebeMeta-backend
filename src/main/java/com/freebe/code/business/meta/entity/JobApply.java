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
 * 岗位申请
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Table(name="t_job_apply", 
	indexes = { @Index(columnList = "Id") })
public class JobApply extends BaseEntity {
	/**
	 * 申请者 ID
	 */
	private Long ownerId;
	
	/**
	 * 工作 ID
	 */
	private Long jobId;
	
	/**
	 * 申请说明
	 */
	@Column(columnDefinition = "TEXT")
	private String applyInfo;
	
	/**
	 * 申请时间
	 */
	private Long applyTime;
	
	/**
	 * 问卷回答 ID
	 */
	private Long anwserId;
	
	/**
	 * 答题提交时间
	 */
	private Long answerTime;;
	
	/**
	 * 问卷答题点评
	 */
	@Column(columnDefinition = "TEXT")
	private String answerComment;
	
	/**
	 * 答题审核时间
	 */
	private Long anserAuditTime;
	
	/**
	 * 申请状态(未开始，已答题，答题未通过，任务考察中，任务考察等审核，任务考察未通过，任务考察通过，用人单位审核中)
	 */
	private Integer applyStatus;
	
	/**
	 * 关联的任务卡片，任务达成的通过条件在任务卡片进行
	 */
	private Long taskCardId;
	
	/**
	 * 任务完成时间
	 */
	private Long taskEndTime;
	
	/**
	 * 任务点评
	 */
	@Column(columnDefinition = "TEXT")
	private String taskComment;
	
	/**
	 * 任务审核时间
	 */
	private Long taskAuditTime;
	
	/**
	 * 审核人 ID
	 */
	private Long reviewerId;
	
	/**
	 * 审核时间
	 */
	private Long reviewTime;
	
	/**
	 * 审核点评
	 */
	@Column(columnDefinition = "TEXT")
	private String reviewComment;
}
