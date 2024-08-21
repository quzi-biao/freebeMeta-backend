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
@ApiModel("问卷查询参数")
public class QuestionnaireQueryParam extends PageBean {
	@ApiModelProperty("问卷所有者 ID")
	private Long ownerId;

	@ApiModelProperty("问卷标题")
	private String title;

	@ApiModelProperty("问卷背景图")
	private String picture;

	@ApiModelProperty("问卷说明")
	private String description;

	@ApiModelProperty("问卷截止日期")
	private Long deadLine;

	@ApiModelProperty("开始时间")
	private Long createStartTime;

	@ApiModelProperty("结束时间")
	private Long createEndTime;

}
