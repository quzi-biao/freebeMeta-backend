package com.freebe.code.business.badge.controller.param;

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
@ApiModel("徽章参数")
public class BadgeParam extends BaseEntityParam {
	@ApiModelProperty("图标")
	private String icon;

	@ApiModelProperty("展示图")
	private String picture;

	@ApiModelProperty("描述")
	private String description;

	@ApiModelProperty("获取条件")
	private String getCondition;

	@ApiModelProperty("自动获取的认定器")
	private Long autoGetIdentity;

	@ApiModelProperty("徽章合约地址")
	private String contractAddress;
}
