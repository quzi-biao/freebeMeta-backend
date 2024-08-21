package com.freebe.code.business.base.controller.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseEntityParam {
	@ApiModelProperty("ID")
	private Long id;
	
	@ApiModelProperty("名称")
	private String name;
}
