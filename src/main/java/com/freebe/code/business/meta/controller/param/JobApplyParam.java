package com.freebe.code.business.meta.controller.param;

import com.freebe.code.business.base.controller.param.BaseEntityParam;

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
@ApiModel("岗位申请参数")
public class JobApplyParam extends BaseEntityParam {
	@ApiModelProperty("申请说明")
	private String applyInfo;
	
	@ApiModelProperty("申请的岗位 ID")
	private Long jobId;
}
