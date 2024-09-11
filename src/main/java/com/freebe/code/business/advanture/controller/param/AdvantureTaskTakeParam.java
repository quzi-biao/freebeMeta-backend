package com.freebe.code.business.advanture.controller.param;

import com.freebe.code.business.base.controller.param.BaseEntityParam;

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
@ApiModel("任务领取参数")
public class AdvantureTaskTakeParam extends BaseEntityParam {
	@ApiModelProperty("领取的任务 ID")
	private Long taskId;
}
