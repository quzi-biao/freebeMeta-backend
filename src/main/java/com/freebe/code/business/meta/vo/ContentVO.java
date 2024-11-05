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
@ApiModel("内容")
public class ContentVO extends InteractionEntityVO {
	@ApiModelProperty("发布者 ID")
	private Long ownerId;
	
	@ApiModelProperty("发布者")
	private UserVO owner;

	@ApiModelProperty("本文做为另一文章的回应")
	private Long repplyContent;
	
	@ApiModelProperty("被回应的文章标题")
	private ContentVO repplied;

	@ApiModelProperty("项目 ID")
	private Long projectId;

	@ApiModelProperty("内容类型")
	private Integer contentType;

	@ApiModelProperty("内容分类")
	private Integer category;

	@ApiModelProperty("内容标题")
	private String title;

	@ApiModelProperty("内容 key，关联到 contentData")
	private String contentKey;

	@ApiModelProperty("内容封面")
	private String picture;

	@ApiModelProperty("内容摘要")
	private String contentAbstract;

	@ApiModelProperty("内容发布时间")
	private Long deployTime;

	@ApiModelProperty("内容发布状态（审核中，已发布）")
	private Integer status;
	
	@ApiModelProperty("公开类型")
	private Integer publicType;
}
