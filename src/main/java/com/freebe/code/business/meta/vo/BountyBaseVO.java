package com.freebe.code.business.meta.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("悬赏的基础信息")
public class BountyBaseVO {
	@ApiModelProperty("悬赏ID")
	private Long id;
	
	@ApiModelProperty("悬赏名称")
	private String title;
	
	@ApiModelProperty("悬赏状态")
	private Integer state;
}
