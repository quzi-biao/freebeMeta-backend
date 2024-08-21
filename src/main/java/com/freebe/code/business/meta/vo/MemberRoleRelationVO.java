package com.freebe.code.business.meta.vo;

import com.freebe.code.business.base.vo.BaseVO;

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
@ApiModel("用户角色关系表")
public class MemberRoleRelationVO extends BaseVO {
	@ApiModelProperty("角色ID")
	private Long roleId;

	@ApiModelProperty("成员 ID")
	private Long memberId;


}
