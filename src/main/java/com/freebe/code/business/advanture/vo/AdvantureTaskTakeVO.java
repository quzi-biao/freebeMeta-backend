package com.freebe.code.business.advanture.vo;

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
@ApiModel("任务领取")
public class AdvantureTaskTakeVO extends BaseVO {
	@ApiModelProperty("领取者 ID")
	private Long takerId;
	
	@ApiModelProperty("领取者")
	private UserVO taker;

	@ApiModelProperty("领取的任务 ID")
	private Long taskId;
	
	@ApiModelProperty("领取的任务")
	private AdvantureTaskVO task;

	@ApiModelProperty("领取时间")
	private Long takeTime;

	@ApiModelProperty("提交时间")
	private Long submitTime;

	@ApiModelProperty("审核时间")
	private Long auditTime;
	
	@ApiModelProperty("审核人")
	private UserVO auditor;

	@ApiModelProperty("提交说明")
	private String submitDescription;

	@ApiModelProperty("审核说明")
	private String auditDescription;

	@ApiModelProperty("状态")
	private Integer state;


}
