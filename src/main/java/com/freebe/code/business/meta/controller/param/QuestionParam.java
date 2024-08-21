package com.freebe.code.business.meta.controller.param;

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
@ApiModel("问卷问题参数")
public class QuestionParam extends BaseEntityParam {
	@ApiModelProperty("问卷ID")
	private Long questionnaireId;

	@ApiModelProperty("问题编号")
	private Integer number;

	@ApiModelProperty("问题")
	private String content;

	@ApiModelProperty("问题说明")
	private String description;

	@ApiModelProperty("问题类型")
	private Integer questionType;

	@ApiModelProperty("问题选项")
	private String questionSelect;


}
