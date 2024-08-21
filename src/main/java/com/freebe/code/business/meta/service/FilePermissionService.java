package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.meta.entity.FilePermission;
import com.freebe.code.business.meta.vo.FilePermissionVO;
import com.freebe.code.business.meta.controller.param.FilePermissionParam;
import com.freebe.code.business.meta.controller.param.FilePermissionQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface FilePermissionService extends BaseService<FilePermission> {

	FilePermissionVO findById(Long id) throws CustomException;

	FilePermissionVO createOrUpdate(FilePermissionParam param) throws CustomException;

	Page<FilePermissionVO> queryPage(FilePermissionQueryParam param) throws CustomException;

}
