package com.freebe.code.business.website.template.vo;

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
@ApiModel("网页模版")
public class WebsiteTemplateEntityVO extends BaseVO {
	@ApiModelProperty("模版描述")
	private String description;

	@ApiModelProperty("模版所有者")
	private Long owner;

	@ApiModelProperty("demo 链接")
	private String demoUrl;

	@ApiModelProperty("模版图片")
	private String picture;

	@ApiModelProperty("定价")
	private Long price;

	@ApiModelProperty("模版资源路径")
	private String templateUrl;

	@ApiModelProperty("模版内容")
	private String content;

	@ApiModelProperty("是否已收藏")
	private Boolean collected;
}
