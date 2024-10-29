package com.freebe.code.business.meta.controller.param;


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
@ApiModel("内容参数")
public class ContentParam extends InteractionEntityParam {
	@ApiModelProperty("本文做为另一文章的回应")
	private Long repplyContent;

	@ApiModelProperty("项目 ID")
	private Long projectId;

	@ApiModelProperty("内容类型")
	private Integer contentType;

	@ApiModelProperty("内容分类")
	private Integer category;
	
	@ApiModelProperty("内容Key")
	private String cotentKey;
	
	@ApiModelProperty("内容")
	private String content;

	@ApiModelProperty("内容标题")
	private String title;

	@ApiModelProperty("内容封面")
	private String picture;

	@ApiModelProperty("内容摘要")
	private String contentAbstract;

}
