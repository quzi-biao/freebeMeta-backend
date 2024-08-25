package com.freebe.code.business.meta.controller.param;

import com.freebe.code.business.base.controller.param.BaseEntityParam;

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
@ApiModel("项目任务参数")
public class TaskParam extends BaseEntityParam {
	@ApiModelProperty("关联的项目 ID")
	private Long projectId;

	@ApiModelProperty("任务标题")
	private String title;

	@ApiModelProperty("任务描述")
	private String description;

	@ApiModelProperty("任务限时, 限时未完成任务重新进入可认领的状态，前一个人的工作被标记未超时，同时记录到档案")
	private Long limitTime;

	@ApiModelProperty("认领等待时间，超过，则任务自动取消")
	private Long takerWaitTime;

	@ApiModelProperty("任务赏金")
	private Long reward;

	@ApiModelProperty("审核等待时间")
	private Long auditWaitTime;
}
