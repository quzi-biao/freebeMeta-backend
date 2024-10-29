package com.freebe.code.business.meta.controller.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("审核参数")
@Data
public class AuditParam {
	/**
	 * 审核说明
	 */
	private String evaluate;
	
	/**
	 * 是否通过
	 */
	private Boolean pass;
	
	/**
	 * 悬赏 ID
	 */
	private Long id;
}
