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
@ApiModel("钱包参数")
public class WalletParam extends BaseEntityParam {
	@ApiModelProperty("成员 ID")
	private Long userId;

	@ApiModelProperty("钱包地址，一般和 member 中的 address 一致")
	private String address;
}
