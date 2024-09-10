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
@ApiModel("项目悬赏参数")
public class BountyParam extends BaseEntityParam {
	@ApiModelProperty("关联的项目 ID")
	private Long projectId;

	@ApiModelProperty("悬赏标题")
	private String title;

	@ApiModelProperty("悬赏描述")
	private String description;

	@ApiModelProperty("悬赏限时, 限时未完成悬赏重新进入可认领的状态，前一个人的工作被标记未超时，同时记录到档案")
	private Long limitTime;

	@ApiModelProperty("认领等待时间，超过，则悬赏自动取消")
	private Long takerWaitTime;

	@ApiModelProperty("悬赏赏金")
	private Long reward;

	@ApiModelProperty("审核等待时间")
	private Long auditWaitTime;
}
