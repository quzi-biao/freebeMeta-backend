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
@ApiModel("文件权限参数")
public class FilePermissionParam extends BaseEntityParam {
	@ApiModelProperty("文件 ID")
	private Long fileId;

	@ApiModelProperty("成员 ID")
	private Long userId;

	@ApiModelProperty("关联的交易 ID")
	private Long transactionId;

	@ApiModelProperty("权限状态")
	private Integer permissionType;

	@ApiModelProperty("权限允许的次数")
	private Long permissionTimes;

	@ApiModelProperty("查看次数")
	private Long viewTimes;


}
