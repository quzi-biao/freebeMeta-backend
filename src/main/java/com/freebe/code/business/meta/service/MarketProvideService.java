package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.meta.entity.MarketProvide;
import com.freebe.code.business.meta.vo.MarketProvideVO;
import com.freebe.code.business.meta.controller.param.MarkerProvideParam;
import com.freebe.code.business.meta.controller.param.MarkerProvideQueryParam;
import com.freebe.code.business.meta.controller.param.MarketProvideApplyParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface MarketProvideService extends BaseService<MarketProvide> {

	MarketProvideVO findById(Long id) throws CustomException;

	MarketProvideVO createOrUpdate(MarkerProvideParam param) throws CustomException;

	Page<MarketProvideVO> queryPage(MarkerProvideQueryParam param) throws CustomException;

	/**
	 * 供应审核
	 * @param providerId
	 * @return
	 * @throws CustomException
	 */
	MarketProvideVO auditProvider(Long providerId) throws CustomException;

	/**
	 * 联系方式
	 * @param providerId
	 * @return
	 * @throws CustomException
	 */
	String getContact(Long providerId) throws CustomException;

	/**
	 * 申请成为供应者
	 * @throws CustomException
	 */
	MarketProvideVO applyToProvider(MarketProvideApplyParam applyParm) throws CustomException;
	
	/**
	 * 放弃成为供应者
	 * @throws CustomException
	 */
	MarketProvideVO giveoutProvider(Long provideId) throws CustomException;

}
