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
@ApiModel("悬赏认领")
public class BountyTakerVO extends BaseVO {
	@ApiModelProperty("悬赏 ID")
	private Long bountyId;

	@ApiModelProperty("悬赏认领时间")
	private Long takeTime;

	@ApiModelProperty("悬赏认领者")
	private UserVO taker;

	@ApiModelProperty("悬赏完成时间")
	private Long doneTime;

	@ApiModelProperty("完成状态")
	private Integer state;

	@ApiModelProperty("完成评价")
	private String evaluate;
	
	@ApiModelProperty("放弃原因")
	private String giveoutReason;

	@ApiModelProperty("悬赏奖励")
	private Long freeBe;

	@ApiModelProperty("悬赏结算交易信息")
	private Long transactionId;

	@ApiModelProperty("交付文件")
	private List<String> submitFiles;

	@ApiModelProperty("交付文件")
	private List<String> submitPictures;

	@ApiModelProperty("交付描述")
	private String submitDescription;


}
