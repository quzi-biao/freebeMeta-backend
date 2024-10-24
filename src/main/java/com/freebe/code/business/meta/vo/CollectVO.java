package com.freebe.code.business.meta.vo;

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
@ApiModel("收藏")
public class CollectVO extends BaseVO {
	@ApiModelProperty("用户 ID")
	private Long userId;

	@ApiModelProperty("类型 ID")
	private Long typeId;

	@ApiModelProperty("对象 ID")
	private BaseVO entity;
}
