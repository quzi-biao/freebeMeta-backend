package com.freebe.code.business.base.controller.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户参数")
public class UserParam {
    @ApiModelProperty(value = "用户ID，如果设置则更新用户")
    private Long id;
    
    @ApiModelProperty(value = "手机号")
    private String phone;
    
    @ApiModelProperty("验证码")
	private String verifyCode;
}
