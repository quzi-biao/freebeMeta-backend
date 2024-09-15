package com.freebe.code.business.badge.vo;

import com.freebe.code.business.base.vo.BaseVO;
import com.freebe.code.business.base.vo.UserVO;
import com.freebe.code.business.meta.vo.MemberVO;

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
@ApiModel("徽章操作记录")
public class BadgeActionRecordVO extends BaseVO {
	@ApiModelProperty("操作者")
	private UserVO operator;

	@ApiModelProperty("徽章")
	private BadgeVO badge;

	@ApiModelProperty("成员 ID，非成员，仅有 userId 不能获取徽章")
	private MemberVO member;

	@ApiModelProperty("操作缘由")
	private String description;

	@ApiModelProperty("操作时间")
	private Long actionTime;

	@ApiModelProperty("操作类型: 1 发放, 2 回收, 3 放弃")
	private Integer actionType;

	@ApiModelProperty("交易 hash")
	private String transactionHash;
}
