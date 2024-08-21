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
@ApiModel("角色参数")
public class RoleParam extends BaseEntityParam {
	@ApiModelProperty("关联的合约地址")
	private String contractAddress;

	@ApiModelProperty("角色图标")
	private String icon;

	@ApiModelProperty("角色描述")
	private String description;
	
	@ApiModelProperty("角色回报")
	private String reward;
	
	@ApiModelProperty("角色图片")
	private String picture;
	
	@ApiModelProperty("角色编码")
	private String roleCode;
}
