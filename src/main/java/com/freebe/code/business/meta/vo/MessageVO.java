package com.freebe.code.business.meta.vo;

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
@ApiModel("消息")
public class MessageVO extends BaseVO {
	@ApiModelProperty("消息发送者")
	private Long senderId;
	
	@ApiModelProperty("消息发送者")
	private UserVO sender;

	@ApiModelProperty("消息接收者")
	private Long recieverId;
	
	@ApiModelProperty("消息发送者")
	private UserVO reciever;

	@ApiModelProperty("消息类型")
	private Integer messageType;

	@ApiModelProperty("消息状态")
	private Integer state;

	@ApiModelProperty("消息内容")
	private String content;
	
	@ApiModelProperty("消息读时间")
	private Long readTime;
}
