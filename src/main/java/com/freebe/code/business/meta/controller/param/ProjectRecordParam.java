package com.freebe.code.business.meta.controller.param;

import java.util.List;

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
@ApiModel("项目记录参数")
public class ProjectRecordParam extends BaseEntityParam {
	@ApiModelProperty("记录的用户ID")
	private Long userId;
	
	@ApiModelProperty("回复的记录ID")
	private Long frontRecordId;

	@ApiModelProperty("回复的记录发表人")
	private Long frontUserId;

	@ApiModelProperty("记录内容")
	private String content;
	
	@ApiModelProperty("图片内容")
	private List<String> pictures;

	@ApiModelProperty("附加文件（文件 ID）")
	private List<String> appendFiles;

	@ApiModelProperty("项目ID")
	private Long projectId;
	
	@ApiModelProperty("项目角色")
	private String projectRole;
	
	@ApiModelProperty("记录类型")
	private Integer recordType;
}
