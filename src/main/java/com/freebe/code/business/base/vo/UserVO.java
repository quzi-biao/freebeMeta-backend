package com.freebe.code.business.base.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author xiezhengbiao
 *
 */
@Data
@ApiModel("用户数据")
public class UserVO {
	
	@ApiModelProperty("用户ID")
	private Long id;

	@ApiModelProperty("用户名称")
	private String name;
	
	@ApiModelProperty("地址")
	private String address;
	
	@ApiModelProperty("头像")
	private String avatar;
	
	@ApiModelProperty("头像")
	private String avator;
	
	@ApiModelProperty("用户类型")
	private Integer userType;
	
	@ApiModelProperty("freeBeId")
	private String freeBeId;
	
	@ApiModelProperty("freeBe积分")
	private Double freeBe;
	
	@ApiModelProperty("freeBe积分")
	private Double cny;
	
	@ApiModelProperty("最后一次登录时间")
	private Long lastLogin;
	
	@ApiModelProperty("贡献分")
	private Long contribution;
	
	@ApiModelProperty("用户类型")
	private String accessToken;
}
