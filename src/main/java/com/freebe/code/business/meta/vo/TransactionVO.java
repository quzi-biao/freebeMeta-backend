package com.freebe.code.business.meta.vo;

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
@ApiModel("交易")
public class TransactionVO extends BaseVO {
	@ApiModelProperty("转出地址")
	private WalletVO srcWallet;

	@ApiModelProperty("转入地址")
	private WalletVO dstWallet;

	@ApiModelProperty("交易金额")
	private Double amount;

	@ApiModelProperty("货币")
	private Integer currency;

	@ApiModelProperty("交易确认时间(上链时间)")
	private Long confirmTime;

	@ApiModelProperty("交易 hash")
	private String hash;

	@ApiModelProperty("交易类型")
	private Integer transactionType;

	@ApiModelProperty("交易状态")
	private Integer state;

	@ApiModelProperty("交易备注")
	private String mark;

	@ApiModelProperty("关联的项目 ID")
	private Long projectId;
	
	@ApiModelProperty("关联的项目")
	private String projectName;

	@ApiModelProperty("公示开始时间")
	private Long publicStartTime;
	
	@ApiModelProperty("失败原因")
	private String failedReason;
}
