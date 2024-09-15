package com.freebe.code.business.badge.vo;

import com.freebe.code.business.base.vo.BaseVO;

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
@ApiModel("徽章")
public class BadgeVO extends BaseVO {
	@ApiModelProperty("徽章创建者")
	private Long createMemeber;

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

	@ApiModelProperty("持有数量")
	private Integer holderNumber;
}
