package com.freebe.code.business.base.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseVO {
	@ApiModelProperty("ID")
	private Long id;
	
	@ApiModelProperty("编码")
	private String code;
	
	@ApiModelProperty("名称")
	private String name;
	
	@ApiModelProperty("创建时间")
	private Long createTime;
	
	@ApiModelProperty("是否使用中")
	private Boolean inUse;
}
