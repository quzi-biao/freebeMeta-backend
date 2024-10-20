package com.freebe.code.business.meta.vo;

import java.util.List;

import com.freebe.code.business.base.vo.BaseVO;
import com.freebe.code.business.base.vo.UserVO;

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
@ApiModel("市场供应方")
public class MarketProvideVO extends BaseVO {
	@ApiModelProperty("供应所有者")
	private List<MarketProviderVO> providers;
	
	@ApiModelProperty("供应创建者")
	private UserVO creator;

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

	@ApiModelProperty("是否审核通过")
	private Boolean audit;

	@ApiModelProperty("联系方式")
	private String contact;
	
	@ApiModelProperty("是否已收藏")
	private Boolean collected;
}
