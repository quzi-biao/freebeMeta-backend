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
@ApiModel("思维导图参数")
public class MindMapParam extends BaseEntityParam {
	@ApiModelProperty("所有者")
	private Long ownerId;

	@ApiModelProperty("归属的项目")
	private Long projectId;

	@ApiModelProperty("导图内容 json 格式")
	private String content;


}
