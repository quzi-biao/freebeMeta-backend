package com.freebe.code.business.meta.vo;

import java.util.List;

import com.freebe.code.business.base.vo.BaseVO;
import com.freebe.code.business.base.vo.UserVO;

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
@ApiModel("问卷回答")
public class QuestionnaireAnswerVO extends BaseVO {
	@ApiModelProperty("问卷 ID")
	private Long questionnaireId;
	
	@ApiModelProperty("问卷 ID")
	private QuestionnaireVO questionnaire;
	
	@ApiModelProperty("答题人 ID")
	private Long userId;

	@ApiModelProperty("是否公开")
	private Boolean isPublic;

	@ApiModelProperty("答题人")
	private UserVO user;
	
	@ApiModelProperty("答案")
	List<QuestionnaireSingleAnswerVO> answers;
}
