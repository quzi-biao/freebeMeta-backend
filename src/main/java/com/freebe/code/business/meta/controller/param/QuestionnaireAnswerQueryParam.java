package com.freebe.code.business.meta.controller.param;

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
@ApiModel("问卷回答查询参数")
public class QuestionnaireAnswerQueryParam extends PageBean {
	@ApiModelProperty("问卷 ID")
	private Long questionnaireId;

	@ApiModelProperty("答题人 ID")
	private Long userId;

	@ApiModelProperty("开始时间")
	private Long createStartTime;

	@ApiModelProperty("结束时间")
	private Long createEndTime;
	
	@ApiModelProperty("是否公开")
	private Boolean isPublic;

}
