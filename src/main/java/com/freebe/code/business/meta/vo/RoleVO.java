package com.freebe.code.business.meta.vo;

import java.util.List;

import com.freebe.code.business.base.vo.BaseVO;
import com.freebe.code.business.base.vo.UserVO;

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
@ApiModel("角色")
public class RoleVO extends BaseVO {
	@ApiModelProperty("关联的合约地址")
	private String contractAddress;

	@ApiModelProperty("角色图标")
	private String icon;
	
	@ApiModelProperty("角色图片")
	private String picture;
	
	@ApiModelProperty("角色描述")
	private String description;
	
	@ApiModelProperty("角色回报")
	private String reward;
	
	@ApiModelProperty("角色编码")
	private String roleCode;
	
	@ApiModelProperty("数量")
	private Integer number;
	
	@ApiModelProperty("角色持有者")
	private List<UserVO> roleKeeper;
}
