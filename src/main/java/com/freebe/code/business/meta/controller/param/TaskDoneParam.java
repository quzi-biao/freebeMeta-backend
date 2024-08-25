package com.freebe.code.business.meta.controller.param;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("任务完成参数")
@Data
public class TaskDoneParam {
	@ApiModelProperty("任务ID")
	private Long takeId;
	
	@ApiModelProperty("交付文件")
	private List<String> submitFiles;

	@ApiModelProperty("交付文件")
	private List<String> submitPictures;

	@ApiModelProperty("交付描述")
	private String submitDescription;
}
