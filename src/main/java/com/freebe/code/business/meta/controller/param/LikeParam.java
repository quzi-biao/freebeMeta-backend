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
@ApiModel("喜爱参数")
public class LikeParam extends BaseEntityParam {
	@ApiModelProperty("用户 ID")
	private Long userId;

	@ApiModelProperty("类型 ID")
	private Long typeId;

	@ApiModelProperty("对象 ID")
	private Long entityId;
}
