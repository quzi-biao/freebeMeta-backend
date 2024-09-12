package com.freebe.code.business.advanture.controller.param;

import java.util.List;

import com.freebe.code.common.PageBean;

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
@ApiModel("任务领取查询参数")
public class AdvantureTaskTakeQueryParam extends PageBean {
	@ApiModelProperty("领取者 ID")
	private Long takerId;
	
	@ApiModelProperty("领取者名称")
	private String takerName;
	
	@ApiModelProperty("领取者 ID")
	private List<Long> takerIds;

	@ApiModelProperty("领取的任务 ID")
	private Long taskId;

	@ApiModelProperty("状态")
	private Long state;

	@ApiModelProperty("开始时间")
	private Long createStartTime;

	@ApiModelProperty("结束时间")
	private Long createEndTime;
}
