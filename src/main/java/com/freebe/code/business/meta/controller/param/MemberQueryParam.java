package com.freebe.code.business.meta.controller.param;

import com.freebe.code.common.KeyWordsQueryParam;

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
@ApiModel("成员查询参数")
public class MemberQueryParam extends KeyWordsQueryParam {
	@ApiModelProperty("FreeBe ID")
	private String freeBeId;
	
	@ApiModelProperty("角色名称")
	private String roleName;
	
	@ApiModelProperty("技能")
	private String skill;
	
	@ApiModelProperty("名称")
	private String name;

}
