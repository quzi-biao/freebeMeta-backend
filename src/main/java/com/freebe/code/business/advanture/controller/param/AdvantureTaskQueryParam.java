package com.freebe.code.business.advanture.controller.param;

import com.freebe.code.common.PageBean;

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
@ApiModel("冒险卡片查询参数")
public class AdvantureTaskQueryParam extends PageBean {
	@ApiModelProperty("任务状态，0 表示正常，1 表示关闭")
	private Integer status;

	@ApiModelProperty("任务等级")
	private Integer taskLevel;
}
