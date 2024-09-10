package com.freebe.code.business.meta.controller.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("悬赏审核参数")
@Data
public class BountyAuditParam {
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
	private Long bountyId;
}
