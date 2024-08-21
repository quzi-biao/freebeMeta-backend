package com.freebe.code.business.meta.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("技能")
@Data
public class Skill {
	
	@ApiModelProperty("技能名称")
	private String name;
	
	@ApiModelProperty("标签颜色")
	private String color;
}
