package com.freebe.code.business.meta.controller.param;

import java.util.List;

import com.freebe.code.business.base.controller.param.BaseEntityParam;
import com.freebe.code.business.meta.vo.ProjectTag;

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
@ApiModel("项目参数")
public class ProjectParam extends BaseEntityParam {
	@ApiModelProperty("项目所有者（项目主理人）")
	private Long ownerId;
	
	@ApiModelProperty("结算货币")
	private Integer currency;

	@ApiModelProperty("项目总预算")
	private Double preAmount;

	@ApiModelProperty("项目实际支出")
	private Double realAmount;

	@ApiModelProperty("项目图片（支持多张）")
	private List<String> pictures;

	@ApiModelProperty("项目标签，数组")
	private List<ProjectTag> tags;

	@ApiModelProperty("项目介绍")
	private String description;
	
	@ApiModelProperty("项目成员")
	private List<ProjectMemberParam> members;
}
