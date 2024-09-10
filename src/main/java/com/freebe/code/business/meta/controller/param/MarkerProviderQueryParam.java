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
@ApiModel("市场供应方查询参数")
public class MarkerProviderQueryParam extends PageBean {
	@ApiModelProperty("供应所有者")
	private Long ownerId;

	@ApiModelProperty("最高价格")
	private Long maxPrice;

	@ApiModelProperty("最低价格")
	private Long minPrice;

	@ApiModelProperty("标签")
	private String tags;

	@ApiModelProperty("是否审核通过")
	private Boolean audit;
}
