package com.freebe.code.business.meta.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("项目邀请消息")
public class ProjectInviteMessage {
	
	@ApiModelProperty("项目ID")
	private Long projectId;
	
	@ApiModelProperty("成员ID")
	private Long memberId;
	
	@ApiModelProperty("消息内容")
	private String content;
	
	@ApiModelProperty("项目名称")
	private String projectName;
	
	@ApiModelProperty("主理人名称")
	private String ownerName;
}
