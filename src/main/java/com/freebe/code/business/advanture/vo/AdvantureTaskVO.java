package com.freebe.code.business.advanture.vo;

import com.freebe.code.business.base.vo.BaseVO;

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
@ApiModel("冒险卡片")
public class AdvantureTaskVO extends BaseVO {
	@ApiModelProperty("任务标题")
	private String title;

	@ApiModelProperty("任务说明")
	private String description;

	@ApiModelProperty("奖励经验值")
	private Long experience;

	@ApiModelProperty("任务状态，0 表示正常，1 表示关闭")
	private Integer status;

	@ApiModelProperty("任务创建者")
	private Long creator;

	@ApiModelProperty("任务等级")
	private Integer taskLevel;
	
	@ApiModelProperty("任务类型")
	private Integer taskTypeId;
}
