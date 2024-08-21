package com.freebe.code.business.meta.controller.param;

import com.freebe.code.common.KeyWordsQueryParam;

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
@ApiModel("项目查询参数")
public class ProjectQueryParam extends KeyWordsQueryParam {
	@ApiModelProperty("项目所有者（项目主理人）")
	private Long ownerId;

	@ApiModelProperty("项目状态")
	private Integer state;

	@ApiModelProperty("项目结算状态")
	private Integer billState;
}
