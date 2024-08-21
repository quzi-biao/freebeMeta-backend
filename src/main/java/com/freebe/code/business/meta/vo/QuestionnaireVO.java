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
@ApiModel("问卷")
public class QuestionnaireVO extends BaseVO {
	@ApiModelProperty("问卷所有者 ID")
	private Long ownerId;
	
	@ApiModelProperty("问卷所有者 ID")
	private UserVO owner;

	@ApiModelProperty("问卷标题")
	private String title;

	@ApiModelProperty("问卷背景图")
	private String picture;

	@ApiModelProperty("问卷说明")
	private String description;

	@ApiModelProperty("问卷截止日期")
	private Long deadLine;
	
	@ApiModelProperty("问卷问题")
	private List<QuestionVO> questions;

}
