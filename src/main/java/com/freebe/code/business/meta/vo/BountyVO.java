package com.freebe.code.business.meta.vo;

import java.util.List;

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
@ApiModel("项目任务")
public class BountyVO extends BaseVO {
	@ApiModelProperty("关联的项目 ID")
	private Long projectId;
	
	@ApiModelProperty("项目名称")
	private String projectName;

	@ApiModelProperty("任务所有者ID")
	private Long ownerId;

	@ApiModelProperty("任务所有者")
	private UserVO owner;

	@ApiModelProperty("任务标题")
	private String title;

	@ApiModelProperty("任务描述")
	private String description;

	@ApiModelProperty("任务状态")
	private Integer state;

	@ApiModelProperty("任务限时, 限时未完成任务重新进入可认领的状态，前一个人的工作被标记未超时，同时记录到档案")
	private Long limitTime;

	@ApiModelProperty("当前认领 ID")
	private BountyTakerVO take;

	@ApiModelProperty("认领等待时间，超过，则任务自动取消")
	private Long takerWaitTime;

	@ApiModelProperty("任务赏金")
	private Long reward;
	
	@ApiModelProperty("货币")
	private Integer currency;
	
	@ApiModelProperty("任务U赏金")
	private Double ureward;
	
	@ApiModelProperty("审核开始时间")
	private Long auditStartTime;
	
	@ApiModelProperty("审核时间")
	private Long auditTime;
	
	@ApiModelProperty("实际审核人")
	private UserVO auditor;
	
	@ApiModelProperty("悬赏优先级")
	private Integer priority;
	
	@ApiModelProperty("是否可认领")
	private Boolean canTake;
	
	@ApiModelProperty("审核员")
	private List<UserVO> auditors;
	
	@ApiModelProperty("审核奖励百分比")
	private Integer auditReward;
	
	@ApiModelProperty("前置任务列表")
	private List<BountyBaseVO> frontBounties;
	
	@ApiModelProperty("后置任务列表")
	private List<BountyBaseVO> nextBounties;
}
