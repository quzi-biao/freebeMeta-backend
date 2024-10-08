package com.freebe.code.business.meta.controller.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("供应方申请说明")
@Data
public class MarketProvideApplyParam {
	
	@ApiModelProperty("服务ID")
	private Long provideId;
	
	@ApiModelProperty("经验说明")
	private String experience;
	
	@ApiModelProperty("能力证据")
	private String evidence;
	
	@ApiModelProperty("可提供服务的时间")
	private String serviceTime;
}
