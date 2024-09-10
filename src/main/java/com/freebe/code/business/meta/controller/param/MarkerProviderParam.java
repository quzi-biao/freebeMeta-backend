package com.freebe.code.business.meta.controller.param;

import java.util.List;

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
@ApiModel("市场供应方参数")
public class MarkerProviderParam extends BaseEntityParam {
	@ApiModelProperty("供应所有者")
	private Long ownerId;

	@ApiModelProperty("供应内容")
	private String title;

	@ApiModelProperty("宣传展示图")
	private String picture;

	@ApiModelProperty("描述")
	private String description;

	@ApiModelProperty("最高价格")
	private Long maxPrice;

	@ApiModelProperty("最低价格")
	private Long minPrice;

	@ApiModelProperty("价格描述")
	private String priceDescription;

	@ApiModelProperty("标签")
	private List<String> tags;

	@ApiModelProperty("联系方式")
	private String contact;
}
