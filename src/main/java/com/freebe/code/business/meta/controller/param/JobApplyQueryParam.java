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
@ApiModel("岗位申请查询参数")
public class JobApplyQueryParam extends PageBean {
	@ApiModelProperty("申请者 ID")
	private Long ownerId;

	@ApiModelProperty("申请的岗位 ID")
	private Long jobId;
}
