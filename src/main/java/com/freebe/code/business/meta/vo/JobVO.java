package com.freebe.code.business.meta.vo;


import com.freebe.code.business.badge.vo.BadgeVO;
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
@ApiModel("岗位")
public class JobVO extends InteractionEntityVO {
	@ApiModelProperty("岗位发布者")
	private Long ownerId;
	
	@ApiModelProperty("岗位发布者")
	private UserVO owner;

	@ApiModelProperty("归属的项目")
	private Long projectId;

	@ApiModelProperty("岗位标题")
	private String title;

	@ApiModelProperty("内容 key，关联到 contentData")
	private String description;

	@ApiModelProperty("岗位回报说明")
	private String rewardDescription;

	@ApiModelProperty("申请者数量")
	private Long applier;

	@ApiModelProperty("关联的徽章 ID")
	private BadgeVO badge;

	@ApiModelProperty("关联的问卷 ID")
	private Long questionaireId;
	
	@ApiModelProperty("关联的问卷标题")
	private String questionaireTitle;

	@ApiModelProperty("关联的任务类型 ID")
	private Long taskTypeId;

}
