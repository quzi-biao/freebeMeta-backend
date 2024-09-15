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
@ApiModel("徽章持有记录")
public class BadgeHoldVO extends BaseVO {
	@ApiModelProperty("徽章")
	private BadgeVO badge;

	@ApiModelProperty("成员 ID，非成员，仅有 userId 不能获取徽章")
	private Long memberId;

	@ApiModelProperty("是否持有中")
	private Boolean inHold;


}
