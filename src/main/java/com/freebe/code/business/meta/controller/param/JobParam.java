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
@ApiModel("岗位参数")
public class JobParam extends InteractionEntityParam {
	@ApiModelProperty("归属的项目")
	private Long projectId;

	@ApiModelProperty("岗位标题")
	private String title;

	@ApiModelProperty("内容 key，关联到 contentData")
	private String description;

	@ApiModelProperty("岗位回报说明")
	private String rewardDescription;

	@ApiModelProperty("关联的徽章 ID")
	private Long badgeId;

	@ApiModelProperty("关联的问卷 ID")
	private Long questionaireId;

	@ApiModelProperty("关联的任务类型 ID")
	private Long taskTypeId;

	@ApiModelProperty("招聘人数")
	private Integer headCount;
	
	@ApiModelProperty("招募截止时间")
	private Long deadLine;
}
