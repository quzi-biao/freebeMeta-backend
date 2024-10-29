package com.freebe.code.business.meta.controller.param;

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
@ApiModel("喜爱查询参数")
public class LikeQueryParam extends PageBean {
	@ApiModelProperty("用户 ID")
	private Long userId;

	@ApiModelProperty("类型 ID")
	private Long typeId;

	@ApiModelProperty("对象 ID")
	private Long entityId;
}
