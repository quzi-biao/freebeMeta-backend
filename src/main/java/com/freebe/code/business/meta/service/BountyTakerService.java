package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.BountyDoneParam;
import com.freebe.code.business.meta.controller.param.BountyTakerParam;
import com.freebe.code.business.meta.controller.param.BountyTakerQueryParam;
import com.freebe.code.business.meta.entity.BountyTaker;
import com.freebe.code.business.meta.vo.BountyTakerVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface BountyTakerService extends BaseService<BountyTaker> {

	BountyTakerVO findById(Long id) throws CustomException;

	BountyTakerVO createOrUpdate(BountyTakerParam param) throws CustomException;

	Page<BountyTakerVO> queryPage(BountyTakerQueryParam param) throws CustomException;

	/**
	 * 完成悬赏
	 * @param param
	 * @return
	 * @throws CustomException
	 */
	BountyTakerVO doneBounty(BountyDoneParam param) throws CustomException;
}
