package com.freebe.code.business.meta.vo;

import com.freebe.code.business.base.vo.BaseVO;

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
@ApiModel("项目悬赏")
public class BountyVO extends BaseVO {
	@ApiModelProperty("关联的项目 ID")
	private Long projectId;
	
	@ApiModelProperty("项目名称")
	private String projectName;

	@ApiModelProperty("悬赏所有者")
	private Long ownerId;

	@ApiModelProperty("悬赏标题")
	private String title;

	@ApiModelProperty("悬赏描述")
	private String description;

	@ApiModelProperty("悬赏状态")
	private Integer state;

	@ApiModelProperty("悬赏限时, 限时未完成悬赏重新进入可认领的状态，前一个人的工作被标记未超时，同时记录到档案")
	private Long limitTime;

	@ApiModelProperty("当前认领 ID")
	private BountyTakerVO take;

	@ApiModelProperty("认领等待时间，超过，则悬赏自动取消")
	private Long takerWaitTime;

	@ApiModelProperty("悬赏赏金")
	private Long reward;
	
	@ApiModelProperty("审核开始时间")
	private Long auditStartTime;
}
