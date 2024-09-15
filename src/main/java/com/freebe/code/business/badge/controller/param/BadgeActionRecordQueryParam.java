package com.freebe.code.business.badge.controller.param;

import com.freebe.code.common.PageBean;

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
@ApiModel("徽章操作记录查询参数")
public class BadgeActionRecordQueryParam extends PageBean {
	@ApiModelProperty("操作者")
	private Long operator;

	@ApiModelProperty("徽章ID")
	private Long badgeId;

	@ApiModelProperty("成员 ID，非成员，仅有 userId 不能获取徽章")
	private Long memberId;

	@ApiModelProperty("操作类型: 1 发放, 2 回收, 3 放弃")
	private Integer actionType;

	@ApiModelProperty("开始时间")
	private Long createStartTime;

	@ApiModelProperty("结束时间")
	private Long createEndTime;

}
