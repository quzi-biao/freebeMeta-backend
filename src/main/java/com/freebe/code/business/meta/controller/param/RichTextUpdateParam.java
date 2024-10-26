package com.freebe.code.business.meta.controller.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 更新内容
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ApiModel("富文本参数")
public class RichTextUpdateParam {
	
	@ApiModelProperty("文档地址")
	private String docId;
	
	@ApiModelProperty("更新内容")
	private String updateContent;
}
