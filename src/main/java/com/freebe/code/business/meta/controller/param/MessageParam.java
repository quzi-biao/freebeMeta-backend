package com.freebe.code.business.meta.controller.param;

import com.freebe.code.business.base.controller.param.BaseEntityParam;

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
@ApiModel("消息参数")
public class MessageParam extends BaseEntityParam {
	@ApiModelProperty("消息发送着")
	private Long sender;

	@ApiModelProperty("消息接收者")
	private Long reciever;

	@ApiModelProperty("消息类型")
	private Integer messageType;

	@ApiModelProperty("消息状态")
	private Integer state;

	@ApiModelProperty("消息内容")
	private String content;


}
