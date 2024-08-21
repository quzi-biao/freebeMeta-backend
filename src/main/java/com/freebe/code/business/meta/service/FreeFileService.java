package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.meta.entity.FreeFile;
import com.freebe.code.business.meta.vo.FreeFileVO;
import com.freebe.code.business.meta.controller.param.FreeFileParam;
import com.freebe.code.business.meta.controller.param.FreeFileQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface FreeFileService extends BaseService<FreeFile> {

	FreeFileVO findById(Long id) throws CustomException;

	FreeFileVO createOrUpdate(FreeFileParam param) throws CustomException;

	Page<FreeFileVO> queryPage(FreeFileQueryParam param) throws CustomException;

}
