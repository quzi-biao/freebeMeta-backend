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
@ApiModel("内容数据，做为各个对象富文本等内容的存储")
public class ContentDataVO extends BaseVO {
	@ApiModelProperty("内容的文本")
	private String content;

	@ApiModelProperty("内容的二进制数据（如果是压缩保存）")
	private byte[] binary;
	
	@ApiModelProperty("内容是否压缩")
	private Integer commpress;

	@ApiModelProperty("是否是加密内容")
	private Integer crypto;

	@ApiModelProperty("内容类型")
	private Integer contentType;

	@ApiModelProperty("内容所有者")
	private Long ownerId;

	@ApiModelProperty("内容的 key")
	private String contentKey;
}
