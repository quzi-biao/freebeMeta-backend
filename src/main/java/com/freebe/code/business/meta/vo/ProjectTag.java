package com.freebe.code.business.meta.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("项目标签")
public class ProjectTag {
	
	@ApiModelProperty("标签名称")
	private String name;
	
	@ApiModelProperty("标签颜色")
	private String color;
}
