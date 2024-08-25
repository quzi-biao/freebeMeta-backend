package com.freebe.code.business.meta.vo;

import java.util.List;

import com.freebe.code.business.base.vo.BaseVO;
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
@ApiModel("项目")
public class ProjectVO extends BaseVO {
	@ApiModelProperty("项目所有者（项目主理人）")
	private Long ownerId;
	
	@ApiModelProperty("项目所有者")
	private UserVO owner;

	@ApiModelProperty("项目总预算")
	private Double preAmount;

	@ApiModelProperty("项目实际支出")
	private Double realAmount;

	@ApiModelProperty("结算货币")
	private Integer currency;

	@ApiModelProperty("项目图片（支持多张）")
	private List<String> pictures;

	@ApiModelProperty("项目状态")
	private Integer state;

	@ApiModelProperty("项目标签，数组")
	private List<ProjectTag> tags;

	@ApiModelProperty("项目介绍")
	private String description;

	@ApiModelProperty("项目开始时间")
	private Long startTime;

	@ApiModelProperty("项目完成时间")
	private Long doneTime;

	@ApiModelProperty("项目结束时间")
	private Long endTime;

	@ApiModelProperty("项目结算时间")
	private Long billTime;

	@ApiModelProperty("项目结算状态")
	private Integer billState;
	
	@ApiModelProperty("项目类型")
	private Integer projectType;

	@ApiModelProperty("项目成员")
	private List<ProjectMemberVO> members;
}
