package com.freebe.code.business.meta.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.meta.entity.Role;
import com.freebe.code.business.meta.vo.RoleVO;
import com.freebe.code.business.meta.controller.param.RoleParam;
import com.freebe.code.business.meta.controller.param.RoleQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface RoleService extends BaseService<Role> {

	RoleVO findById(Long id) throws CustomException;

	RoleVO createOrUpdate(RoleParam param) throws CustomException;

	Page<RoleVO> queryPage(RoleQueryParam param) throws CustomException;

	List<RoleVO> findByIds(List<Long> id) throws CustomException;

}
