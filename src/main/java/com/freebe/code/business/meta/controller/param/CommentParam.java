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
@ApiModel("评论参数")
public class CommentParam extends InteractionEntityParam {
	@ApiModelProperty("评论者")
	private Long ownerId;

	@ApiModelProperty("归属的内容 ID")
	private Long contentId;

	@ApiModelProperty("评论的回复对象")
	private Long parentId;

	@ApiModelProperty("回复对象的所有者")
	private Long parentOwnerId;

	@ApiModelProperty("内容 key，关联到 contentData")
	private String content;


}
