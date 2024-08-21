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
@ApiModel("文件权限")
public class FilePermissionVO extends BaseVO {
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
