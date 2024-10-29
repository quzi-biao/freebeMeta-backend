package com.freebe.code.business.meta.vo;


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
@ApiModel("评论")
public class CommentVO extends InteractionEntityVO {
	@ApiModelProperty("评论者")
	private Long ownerId;
	
	@ApiModelProperty("评论者")
	private UserVO owner;

	@ApiModelProperty("归属的内容 ID")
	private Long contentId;

	@ApiModelProperty("评论的回复对象")
	private Long parentId;

	@ApiModelProperty("回复对象的所有者")
	private Long parentOwnerId;
	
	@ApiModelProperty("评论者")
	private UserVO parentOwner;

	@ApiModelProperty("评论内容")
	private String content;
}
