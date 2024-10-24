package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.meta.entity.MindMap;
import com.freebe.code.business.meta.vo.MindMapVO;
import com.freebe.code.business.meta.controller.param.MindMapParam;
import com.freebe.code.business.meta.controller.param.MindMapQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface MindMapService extends BaseService<MindMap> {

	MindMapVO findById(Long id) throws CustomException;

	MindMapVO createOrUpdate(MindMapParam param) throws CustomException;

	Page<MindMapVO> queryPage(MindMapQueryParam param) throws CustomException;

}
