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
@ApiModel("可交互的对象")
public class InteractionEntityVO extends BaseVO {
	@ApiModelProperty("喜欢的数量")
	private Long like;

	@ApiModelProperty("收藏的数量")
	private Long collect;

	@ApiModelProperty("分享")
	private Long share;

	@ApiModelProperty("评论")
	private Long comment;


}
