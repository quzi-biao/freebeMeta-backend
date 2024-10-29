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
@ApiModel("岗位查询参数")
public class JobQueryParam extends InteractionEntityQueryParam {
	@ApiModelProperty("岗位发布者")
	private Long ownerId;

	@ApiModelProperty("归属的项目")
	private Long projectId;

	@ApiModelProperty("关联的徽章 ID")
	private Long badgeId;

	@ApiModelProperty("关联的问卷 ID")
	private Long questionaireId;

	@ApiModelProperty("关联的任务类型 ID")
	private Long taskTypeId;

}
