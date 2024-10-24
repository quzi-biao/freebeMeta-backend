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
@ApiModel("思维导图")
public class MindMapVO extends BaseVO {
	@ApiModelProperty("所有者")
	private UserVO owner;

	@ApiModelProperty("归属的项目")
	private ProjectVO project;

	@ApiModelProperty("导图内容 json 格式")
	private String content;
}
