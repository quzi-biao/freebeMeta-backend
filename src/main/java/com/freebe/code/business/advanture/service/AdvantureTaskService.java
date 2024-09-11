package com.freebe.code.business.advanture.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.advanture.entity.AdvantureTask;
import com.freebe.code.business.advanture.vo.AdvantureTaskVO;
import com.freebe.code.business.advanture.controller.param.AdvantureTaskParam;
import com.freebe.code.business.advanture.controller.param.AdvantureTaskQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface AdvantureTaskService extends BaseService<AdvantureTask> {

	AdvantureTaskVO findById(Long id) throws CustomException;

	AdvantureTaskVO createOrUpdate(AdvantureTaskParam param) throws CustomException;

	Page<AdvantureTaskVO> queryPage(AdvantureTaskQueryParam param) throws CustomException;

}
