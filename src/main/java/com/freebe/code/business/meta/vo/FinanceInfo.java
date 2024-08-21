package com.freebe.code.business.meta.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("社区资产信息")
public class FinanceInfo {
	
	@ApiModelProperty("社区总积分")
	private Double totalFreeBe;
	
	@ApiModelProperty("社区总发放积分")
	private Double totalDistrubuteFreeBe;
	
	@ApiModelProperty("社区资产总数")
	private Double usdtAmount;
	
}
