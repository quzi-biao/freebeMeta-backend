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
@ApiModel("文件参数")
public class FreeFileParam extends BaseEntityParam {
	@ApiModelProperty("文件标题")
	private String title;

	@ApiModelProperty("文件描述")
	private String description;

	@ApiModelProperty("文件大小(B)")
	private Long fileSize;

	@ApiModelProperty("文件链接")
	private String url;

	@ApiModelProperty("文件类型")
	private String fileType;

	@ApiModelProperty("公开属性")
	private Integer publicType;

	@ApiModelProperty("文件所有者")
	private Long ownerId;

	@ApiModelProperty("文件定价")
	private String price;

	@ApiModelProperty("定价策略：次/不限次")
	private Integer priceCategory;

	@ApiModelProperty("文件类别")
	private Integer fileCategory;
}
