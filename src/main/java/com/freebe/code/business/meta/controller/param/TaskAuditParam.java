package com.freebe.code.business.meta.controller.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("任务审核参数")
@Data
public class TaskAuditParam {
	/**
	 * 审核说明
	 */
	private String evaluate;
	
	/**
	 * 是否通过
	 */
	private Boolean pass;
	
	/**
	 * 任务 ID
	 */
	private Long taskId;
}
