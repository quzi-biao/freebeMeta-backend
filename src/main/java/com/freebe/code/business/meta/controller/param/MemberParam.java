package com.freebe.code.business.meta.controller.param;

import java.util.List;

import com.freebe.code.business.base.controller.param.BaseEntityParam;
import com.freebe.code.business.meta.vo.Skill;

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
@ApiModel("成员参数")
public class MemberParam extends BaseEntityParam {
	@ApiModelProperty("邮箱")
	private String email;

	@ApiModelProperty("简介")
	private String description;

	@ApiModelProperty("技能树")
	private List<Skill> skills;
	
	@ApiModelProperty("头像")
	private String avator;
	
	@ApiModelProperty("freebeId")
	private String freeBeId;

}
