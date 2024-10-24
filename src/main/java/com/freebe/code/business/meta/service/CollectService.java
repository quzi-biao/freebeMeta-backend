package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.CollectParam;
import com.freebe.code.business.meta.controller.param.CollectQueryParam;
import com.freebe.code.business.meta.entity.Collect;
import com.freebe.code.business.meta.vo.CollectVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface CollectService extends BaseService<Collect> {

	CollectVO findById(Long id) throws CustomException;

	CollectVO createOrUpdate(CollectParam param) throws CustomException;

	Page<CollectVO> queryPage(CollectQueryParam param) throws CustomException;
	
	boolean isCollect(long typeId, Long entityId);

	CollectVO cancel(CollectParam param) throws CustomException;

}
