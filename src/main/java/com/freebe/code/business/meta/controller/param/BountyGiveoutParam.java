package com.freebe.code.business.meta.controller.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("悬赏审核参数")
@Data
public class BountyGiveoutParam {
	/**
	 * 审核说明
	 */
	private String reason;
	
	/**
	 * 悬赏 ID
	 */
	private Long takeId;
}
