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
@ApiModel("项目成员")
public class ProjectMemberVO extends BaseVO {
	@ApiModelProperty("项目 ID")
	private Long projectId;

	@ApiModelProperty("成员 ID")
	private Long memberId;
	
	@ApiModelProperty("成员")
	private MemberVO member;

	@ApiModelProperty("加入时间")
	private Long joinTime;

	@ApiModelProperty("退出时间")
	private Long dimissionTime;

	@ApiModelProperty("承担的角色")
	private String role;

	@ApiModelProperty("成员状态")
	private Integer projectMemberState;

	@ApiModelProperty("任务完成状态")
	private Integer workState;

	@ApiModelProperty("成员结算状态")
	private Integer billState;

	@ApiModelProperty("预期支付金额")
	private Double preAmount;

	@ApiModelProperty("实际支付金额")
	private Double realAmount;

	@ApiModelProperty("工作评价")
	private String evaluation;
	
	@ApiModelProperty("退出原因")
	private String dismissionReson;
	
	@ApiModelProperty("完成时间")
	private Long doneTime;
	
	@ApiModelProperty("结算时间")
	private Long billTime;
	
	@ApiModelProperty("结算时间")
	private Long billEndTime;
	
	@ApiModelProperty("结算交易ID")
	private TransactionVO transaction;
}
