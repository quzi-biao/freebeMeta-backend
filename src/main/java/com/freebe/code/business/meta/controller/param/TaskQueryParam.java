package com.freebe.code.business.meta.controller.param;

import java.util.List;

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
@ApiModel("项目任务查询参数")
public class TaskQueryParam extends PageBean {
	@ApiModelProperty("关联的项目 ID")
	private Long projectId;

	@ApiModelProperty("任务所有者")
	private Long ownerId;

	@ApiModelProperty("任务状态")
	private List<Integer> state;

	@ApiModelProperty("当前认领 ID")
	private Long takerId;

	@ApiModelProperty("开始时间")
	private Long createStartTime;

	@ApiModelProperty("结束时间")
	private Long createEndTime;

}
