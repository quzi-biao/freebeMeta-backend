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
@ApiModel("交易参数")
public class TransactionParam extends BaseEntityParam {
	@ApiModelProperty("转出地址")
	private Long srcWalletId;

	@ApiModelProperty("转入地址")
	private Long dstWalletId;

	@ApiModelProperty("交易金额")
	private Double amount;

	@ApiModelProperty("货币")
	private Integer currency;

	@ApiModelProperty("交易类型")
	private Integer transactionType;

	@ApiModelProperty("交易备注")
	private String mark;

	@ApiModelProperty("关联的项目 ID")
	private Long projectId;
	
	@ApiModelProperty("交易 hash")
	private String transactionHash;
}
