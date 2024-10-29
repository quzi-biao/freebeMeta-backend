package com.freebe.code.business.advanture.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.advanture.entity.AdvantureCard;
import com.freebe.code.business.advanture.vo.AdvantureCardVO;
import com.freebe.code.business.advanture.controller.param.AdvantureCardParam;
import com.freebe.code.business.advanture.controller.param.AdvantureCardQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface AdvantureCardService extends BaseService<AdvantureCard> {

	AdvantureCardVO findByUserId(Long userId, Long taskTypeId) throws CustomException;

	AdvantureCardVO createOrUpdate(AdvantureCardParam param) throws CustomException;

	Page<AdvantureCardVO> queryPage(AdvantureCardQueryParam param) throws CustomException;
	
	AdvantureCardVO addExperience(Long userId, Long taskTypeId, Long added) throws CustomException;

}
