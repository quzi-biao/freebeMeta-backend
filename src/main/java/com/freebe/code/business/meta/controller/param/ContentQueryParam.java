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
@ApiModel("内容查询参数")
public class ContentQueryParam extends InteractionEntityQueryParam {
	@ApiModelProperty("发布者 ID")
	private Long ownerId;

	@ApiModelProperty("本文做为另一文章的回应")
	private Long repplyContent;

	@ApiModelProperty("项目 ID")
	private Long projectId;

	@ApiModelProperty("内容类型")
	private Integer contentType;

	@ApiModelProperty("内容分类")
	private Integer category;

	@ApiModelProperty("内容 key，关联到 contentData")
	private String contentKey;

	@ApiModelProperty("内容发布状态（审核中，已发布）")
	private Integer status;
}
