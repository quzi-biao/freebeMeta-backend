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
@ApiModel("问卷问题参数")
public class RichTextParam extends BaseEntityParam {
	@ApiModelProperty("所有者 ID")
	private Long ownerId;

	@ApiModelProperty("所有者类型")
	private Integer ownerType;

	@ApiModelProperty("二进制内容")
	private byte[] byteContent;
	
	@ApiModelProperty("文本内容")
	private String textContent;

	@ApiModelProperty("内容类型")
	private Integer contentType;

	@ApiModelProperty("客户端 ID")
	private String clientId;
}
