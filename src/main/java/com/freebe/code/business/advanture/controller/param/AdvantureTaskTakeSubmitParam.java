package com.freebe.code.business.advanture.controller.param;

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
@ApiModel("任务提交参数")
public class AdvantureTaskTakeSubmitParam {
	@ApiModelProperty("领取ID")
	private Long takeId;
	
	@ApiModelProperty("描述")
	private String description;
}
