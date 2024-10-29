package com.freebe.code.business.meta.controller.param;

import com.freebe.code.common.PageBean;

import io.swagger.annotations.ApiModel;
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
@ApiModel("内容数据，做为各个对象富文本等内容的存储查询参数")
public class ContentDataQueryParam extends PageBean {
	
}
