package com.freebe.code.business.base.controller.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author xiezhengbiao
 *
 */
@ApiModel("登录参数")
@Data
public class LoginParam {
	@ApiModelProperty(value = "freebeId")
    private String freeBeId;
	
	@ApiModelProperty(value = "账号ID(邮箱/手机号)")
	private String accountId;
	
    @ApiModelProperty(value = "密码")
    private String password;  
    
    @ApiModelProperty(value = "验证码")
    private String smsCode;
}
