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
@ApiModel("任务审核参数")
public class AdvantureTaskTakeAuditParam {
	@ApiModelProperty("领取ID")
	private Long takeId;
	
	@ApiModelProperty("描述")
	private String description;
	
	@ApiModelProperty("是否通过")
	private Boolean pass;
}
