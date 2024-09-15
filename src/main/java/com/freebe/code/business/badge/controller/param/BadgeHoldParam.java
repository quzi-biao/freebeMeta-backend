package com.freebe.code.business.badge.controller.param;

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
@ApiModel("徽章持有记录参数")
public class BadgeHoldParam extends BaseEntityParam {
	@ApiModelProperty("徽章ID")
	private Long badgeId;

	@ApiModelProperty("成员 ID，非成员，仅有 userId 不能获取徽章")
	private Long memberId;

}
