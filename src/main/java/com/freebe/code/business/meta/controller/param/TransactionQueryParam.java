package com.freebe.code.business.meta.controller.param;

import java.util.List;

import com.freebe.code.common.PageBean;

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
@ApiModel("交易查询参数")
public class TransactionQueryParam extends PageBean {
	@ApiModelProperty("转出地址")
	private Long srcWalletId;

	@ApiModelProperty("转入地址")
	private Long dstWalletId;
	
	@ApiModelProperty("地址列表")
	private List<Long> walletIds;
	
	@ApiModelProperty("用户")
	private Long userId;
	
	@ApiModelProperty("用户名")
	private String userName;

	@ApiModelProperty("交易类型")
	private Integer transactionType;

	@ApiModelProperty("交易状态")
	private Integer state;

	@ApiModelProperty("关联的项目 ID")
	private Long projectId;

	@ApiModelProperty("开始时间")
	private Long createStartTime;

	@ApiModelProperty("结束时间")
	private Long createEndTime;

}
