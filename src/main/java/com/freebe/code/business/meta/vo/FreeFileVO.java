package com.freebe.code.business.meta.vo;

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
@ApiModel("文件")
public class FreeFileVO extends BaseVO {
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
	
	@ApiModelProperty("文件所有者")
	private UserVO user;

	@ApiModelProperty("文件定价")
	private String price;

	@ApiModelProperty("定价策略：次/不限次")
	private Integer priceCategory;

	@ApiModelProperty("文件类别")
	private Integer fileCategory;
}
