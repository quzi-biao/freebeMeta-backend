package com.freebe.code.business.meta.vo;

import com.freebe.code.business.graph.FreeGraph;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目悬赏的图谱
 * @author zhengbiaoxie
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("悬赏图谱")
public class BountyGraph extends FreeGraph<BountyVO, Long> {
	
	@ApiModelProperty("项目名称")
	private String projectName;
	
	@ApiModelProperty("项目ID")
	private Long projectId;
}
