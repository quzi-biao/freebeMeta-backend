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
@ApiModel("可交互的对象查询参数")
public class InteractionEntityQueryParam extends PageBean {

}
