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
@ApiModel("评论查询参数")
public class CommentQueryParam extends InteractionEntityQueryParam {
	@ApiModelProperty("评论者")
	private Long ownerId;

	@ApiModelProperty("归属的内容 ID")
	private Long contentId;

	@ApiModelProperty("评论的回复对象")
	private Long parentId;

	@ApiModelProperty("回复对象的所有者")
	private Long parentOwnerId;

	@ApiModelProperty("内容 key，关联到 contentData")
	private String contentKey;

	@ApiModelProperty("开始时间")
	private Long createStartTime;

	@ApiModelProperty("结束时间")
	private Long createEndTime;

}
