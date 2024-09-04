package com.freebe.code.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class KeyWordsQueryParam extends PageBean {
	@ApiModelProperty("关键词")
	private String keyWords;

	@ApiModelProperty("ID列表")
	private List<?> idList;
}
