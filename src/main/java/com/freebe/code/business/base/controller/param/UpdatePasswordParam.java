package com.freebe.code.business.base.controller.param;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author xiezhengbiao
 *
 */
@ApiModel("更新密码参数")
@Data
public class UpdatePasswordParam {
	
    @ApiModelProperty(value = "旧密码")
	private String oldPassword;

	@NotEmpty
    @ApiModelProperty(value = "新密码")
	private String newPassword;
}
