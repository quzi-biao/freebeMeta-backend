package com.freebe.code.business.meta.vo;

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
@ApiModel("钱包")
public class WalletVO extends BaseVO {
	@ApiModelProperty("成员 ID")
	private Long userId;
	
	@ApiModelProperty("成员")
	private UserVO user;

	@ApiModelProperty("钱包地址，一般和 member 中的 address 一致")
	private String address;

	@ApiModelProperty("积分余额")
	private Double freeBe;

	@ApiModelProperty("U 的余额")
	private Double usdt;

	@ApiModelProperty("人民币余额")
	private Double cny;
}
