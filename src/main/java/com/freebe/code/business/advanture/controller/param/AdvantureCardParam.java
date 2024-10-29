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
public class AdvantureCardParam extends BaseEntityParam {
	@ApiModelProperty("任务类型")
	private Integer taskTypeId;
}
