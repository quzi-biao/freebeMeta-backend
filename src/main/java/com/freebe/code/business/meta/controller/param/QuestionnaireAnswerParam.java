package com.freebe.code.business.meta.controller.param;

import java.util.List;

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
@ApiModel("问卷回答参数")
public class QuestionnaireAnswerParam extends BaseEntityParam {
	@ApiModelProperty("问卷 ID")
	private Long questionnaireId;

	@ApiModelProperty("答题人 ID")
	private Long userId;

	@ApiModelProperty("是否公开")
	private Boolean isPublic;
	
	@ApiModelProperty("问题答案")
	private List<QuestionnaireSingleAnswerParam> answers;
}
