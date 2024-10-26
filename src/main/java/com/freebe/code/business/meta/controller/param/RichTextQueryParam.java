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
@ApiModel("问卷问题查询参数")
public class RichTextQueryParam extends PageBean {
	@ApiModelProperty("所有者 ID")
	private Long ownerId;

	@ApiModelProperty("所有者类型")
	private Integer ownerType;

	@ApiModelProperty("内容类型")
	private Integer contentType;

	@ApiModelProperty("开始时间")
	private Long createStartTime;

	@ApiModelProperty("结束时间")
	private Long createEndTime;

}
