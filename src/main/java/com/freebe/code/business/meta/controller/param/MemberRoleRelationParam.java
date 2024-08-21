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
@ApiModel("用户角色关系表参数")
public class MemberRoleRelationParam extends BaseEntityParam {
	@ApiModelProperty("角色ID")
	private Long roleId;

	@ApiModelProperty("成员 ID")
	private Long memberId;


}
