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
@ApiModel("思维导图查询参数")
public class MindMapQueryParam extends PageBean {
	@ApiModelProperty("所有者")
	private Long ownerId;

	@ApiModelProperty("归属的项目")
	private Long projectId;

	@ApiModelProperty("导图内容 json 格式")
	private String content;

	@ApiModelProperty("开始时间")
	private Long createStartTime;

	@ApiModelProperty("结束时间")
	private Long createEndTime;

}
