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
@ApiModel("任务认领")
public class TaskTakerVO extends BaseVO {
	@ApiModelProperty("任务 ID")
	private Long taskId;

	@ApiModelProperty("任务认领时间")
	private Long takeTime;

	@ApiModelProperty("任务认领者")
	private UserVO taker;

	@ApiModelProperty("任务完成时间")
	private Long doneTime;

	@ApiModelProperty("完成状态")
	private Integer state;

	@ApiModelProperty("完成评价")
	private String evaluate;

	@ApiModelProperty("任务奖励")
	private Long freeBe;

	@ApiModelProperty("任务结算交易信息")
	private Long transactionId;

	@ApiModelProperty("交付文件")
	private List<String> submitFiles;

	@ApiModelProperty("交付文件")
	private List<String> submitPictures;

	@ApiModelProperty("交付描述")
	private String submitDescription;


}
