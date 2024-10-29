package com.freebe.code.business.meta.vo;


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
@ApiModel("内容草稿箱")
public class ContentDraftVO extends ContentVO {
	@ApiModelProperty("草稿更新时间")
	private Long updateTime;

	@ApiModelProperty("发布时间")
	private Long deployTime;
}
