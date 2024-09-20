package com.freebe.code.business.advanture.controller.param;

import com.freebe.code.common.PageBean;

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
@ApiModel("冒险卡片查询参数")
public class AdvantureCardQueryParam extends PageBean {
	
	@ApiModelProperty("冒险卡状态")
	private Integer state;
}
