package com.freebe.code.business.meta.controller.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("提现参数")
@Data
public class WithdrawParam {
	@ApiModelProperty("提现金额")
	public Integer amount;
}
