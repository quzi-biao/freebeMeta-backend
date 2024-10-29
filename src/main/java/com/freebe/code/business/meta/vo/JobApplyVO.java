package com.freebe.code.business.meta.vo;

import com.freebe.code.business.base.vo.BaseVO;
import com.freebe.code.business.base.vo.UserVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 *
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ApiModel("岗位申请")
public class JobApplyVO extends BaseVO {
	@ApiModelProperty("申请者 ID")
	private Long ownerId;
	
	@ApiModelProperty("JOB ID")
	private Long jobId;
	
	@ApiModelProperty("申请者 ID")
	private UserVO owner;

	@ApiModelProperty("申请说明")
	private String applyInfo;

	@ApiModelProperty("申请时间")
	private Long applyTime;

	@ApiModelProperty("问卷回答 ID")
	private Long anwserId;

	@ApiModelProperty("答题提交时间")
	private Long answerTime;

	@ApiModelProperty("问卷答题点评")
	private String answerComment;

	@ApiModelProperty("答题审核时间")
	private Long anserAuditTime;

	@ApiModelProperty("申请状态(未开始，已答题，答题未通过，任务考察中，任务考察等审核，任务考察未通过，任务考察通过，用人单位审核中)")
	private Integer applyStatus;

	@ApiModelProperty("关联的任务卡片，任务达成的通过条件在任务卡片进行")
	private Long taskCardId;

	@ApiModelProperty("任务完成时间")
	private Long taskEndTime;

	@ApiModelProperty("任务点评")
	private String taskComment;

	@ApiModelProperty("任务审核时间")
	private Long taskAuditTime;

	@ApiModelProperty("审核人 ID")
	private Long reviewerId;
	
	@ApiModelProperty("审核人")
	private UserVO reviewer;

	@ApiModelProperty("审核时间")
	private Long reviewTime;

	@ApiModelProperty("审核点评")
	private String reviewComment;
}
