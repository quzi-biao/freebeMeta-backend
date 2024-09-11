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
@ApiModel("冒险卡片参数")
public class AdvantureTaskParam extends BaseEntityParam {
	@ApiModelProperty("任务标题")
	private String title;

	@ApiModelProperty("任务说明")
	private String description;

	@ApiModelProperty("奖励经验值")
	private Long experience;
	
	@ApiModelProperty("任务等级")
	private Integer taskLevel;
}
