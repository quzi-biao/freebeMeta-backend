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
@ApiModel("角色查询参数")
public class RoleQueryParam extends PageBean {

}
