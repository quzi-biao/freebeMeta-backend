package com.freebe.code.business.meta.web3;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("签名校验参数")
public class VerifyParam {
	@ApiModelProperty("地址")
	private String address;
	
	@ApiModelProperty("签名")
	private String sign;
	
	@ApiModelProperty("钱包类型")
	private String walletType;
	
	private String r;
	
	private String s;
	
	private String v;
	
	@ApiModelProperty("消息")
	private String message;
}
