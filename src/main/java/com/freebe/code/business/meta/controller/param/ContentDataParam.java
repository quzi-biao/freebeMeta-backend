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
@ApiModel("内容数据，做为各个对象富文本等内容的存储参数")
public class ContentDataParam extends BaseEntityParam {
	@ApiModelProperty("内容的文本")
	private String content;

	@ApiModelProperty("内容的二进制数据（如果是压缩保存）")
	private byte[] binary;
	
	@ApiModelProperty("内容类型")
	private Integer contentType;
}
