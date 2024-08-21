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
@ApiModel("文件查询参数")
public class FreeFileQueryParam extends PageBean {
	@ApiModelProperty("文件标题")
	private String title;

	@ApiModelProperty("文件描述")
	private String description;

	@ApiModelProperty("文件大小(B)")
	private Long fileSize;

	@ApiModelProperty("文件链接")
	private String url;

	@ApiModelProperty("文件类型")
	private Integer fileType;

	@ApiModelProperty("公开属性")
	private Integer publicType;

	@ApiModelProperty("文件所有者")
	private Long ownerId;

	@ApiModelProperty("文件定价")
	private String price;

	@ApiModelProperty("定价策略：次/不限次")
	private Integer priceCategory;

	@ApiModelProperty("开始时间")
	private Long createStartTime;

	@ApiModelProperty("结束时间")
	private Long createEndTime;

}
