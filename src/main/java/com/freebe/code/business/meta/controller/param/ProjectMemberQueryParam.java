package com.freebe.code.business.meta.controller.param;

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
@ApiModel("项目成员查询参数")
public class ProjectMemberQueryParam extends PageBean {
	@ApiModelProperty("项目 ID")
	private Long projectId;

	@ApiModelProperty("成员 ID")
	private Long memberId;

	@ApiModelProperty("承担的角色")
	private String role;

	@ApiModelProperty("开始时间")
	private Long createStartTime;

	@ApiModelProperty("结束时间")
	private Long createEndTime;

}
