package com.freebe.code.business.website.template.controller.param;

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
@ApiModel("网页模版参数")
public class WebsiteTemplateEntityParam extends BaseEntityParam {
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


}
