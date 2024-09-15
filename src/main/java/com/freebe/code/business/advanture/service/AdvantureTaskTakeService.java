package com.freebe.code.business.advanture.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.advanture.controller.param.AdvantureTaskTakeAuditParam;
import com.freebe.code.business.advanture.controller.param.AdvantureTaskTakeParam;
import com.freebe.code.business.advanture.controller.param.AdvantureTaskTakeQueryParam;
import com.freebe.code.business.advanture.controller.param.AdvantureTaskTakeSubmitParam;
import com.freebe.code.business.advanture.entity.AdvantureTaskTake;
import com.freebe.code.business.advanture.vo.AdvantureTaskTakeVO;
import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface AdvantureTaskTakeService extends BaseService<AdvantureTaskTake> {
	public static final String ROLE_ADVANTURE_AUDITOR_CODE = "ADVANTURE_AUDITOR";

	AdvantureTaskTakeVO findById(Long id) throws CustomException;

	AdvantureTaskTakeVO createOrUpdate(AdvantureTaskTakeParam param) throws CustomException;

	Page<AdvantureTaskTakeVO> queryPage(AdvantureTaskTakeQueryParam param) throws CustomException;
	
	
	/**
	 * 提交
	 * @param takeId
	 * @return
	 * @throws CustomException 
	 */
	AdvantureTaskTakeVO submit(AdvantureTaskTakeSubmitParam param) throws CustomException;
	
	/**
	 * 审核
	 * @param takeId
	 * @return
	 */
	AdvantureTaskTakeVO audit(AdvantureTaskTakeAuditParam param) throws CustomException;
	
	
	/**
	 * 放弃
	 * @param takeId
	 * @return
	 */
	AdvantureTaskTakeVO giveOut(AdvantureTaskTakeSubmitParam param) throws CustomException;
}
