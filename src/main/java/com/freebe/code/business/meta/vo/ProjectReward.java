package com.freebe.code.business.meta.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("项目积分奖励详情")
public class ProjectReward {
	
	@ApiModelProperty("总积分")
	private Double totalAmount;
	
	@ApiModelProperty("奖励详情")
	private List<RewardItem> rewards;
	
	@Data
	public static class RewardItem {
		private WalletVO wallet;
		
		private Double amount;
	}
}
