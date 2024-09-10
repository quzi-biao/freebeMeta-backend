package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.meta.entity.MarketProvider;
import com.freebe.code.business.meta.vo.MarketProviderVO;
import com.freebe.code.business.meta.controller.param.MarkerProviderParam;
import com.freebe.code.business.meta.controller.param.MarkerProviderQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface MarketProviderService extends BaseService<MarketProvider> {

	MarketProviderVO findById(Long id) throws CustomException;

	MarketProviderVO createOrUpdate(MarkerProviderParam param) throws CustomException;

	Page<MarketProviderVO> queryPage(MarkerProviderQueryParam param) throws CustomException;

	/**
	 * 供应审核
	 * @param providerId
	 * @return
	 * @throws CustomException
	 */
	MarketProviderVO auditProvider(Long providerId) throws CustomException;

	/**
	 * 联系方式
	 * @param providerId
	 * @return
	 * @throws CustomException
	 */
	String getContact(Long providerId) throws CustomException;

}
