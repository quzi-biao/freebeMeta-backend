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
@ApiModel("冒险卡片")
public class AdvantureCardVO extends BaseVO {
	@ApiModelProperty("卡片持有者 ID")
	private Long userId;
	
	@ApiModelProperty("卡片持有者")
	private UserVO user;

	@ApiModelProperty("冒险开始时间")
	private Long startTime;

	@ApiModelProperty("冒险结束时间")
	private Long endTime;

	@ApiModelProperty("经验值")
	private Long experience;

	@ApiModelProperty("状态")
	private Integer state;
}
