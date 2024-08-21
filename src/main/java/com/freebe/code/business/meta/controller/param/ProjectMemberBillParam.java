package com.freebe.code.business.meta.controller.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("结算参数")
public class ProjectMemberBillParam {
	@ApiModelProperty("项目ID")
	private Long projectId;
	
	@ApiModelProperty("成员ID")
	private Long memberId;
	
	@ApiModelProperty("结算金额")
	private Double amount;
	
	@ApiModelProperty("评价/结算说明")
	private String evalute;
}
