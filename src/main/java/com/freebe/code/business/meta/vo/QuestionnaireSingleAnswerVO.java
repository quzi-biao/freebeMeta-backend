package com.freebe.code.business.meta.vo;

import com.freebe.code.business.base.vo.BaseVO;

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
@ApiModel("问卷单题答案")
public class QuestionnaireSingleAnswerVO extends BaseVO {
	@ApiModelProperty("问卷 ID")
	private Long questionnaireId;

	@ApiModelProperty("问题 ID")
	private Long questionId;

	@ApiModelProperty("答题 ID")
	private Long answerId;

	@ApiModelProperty("成员 ID")
	private Long userId;

	@ApiModelProperty("问题答案")
	private String answer;


}
