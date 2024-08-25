package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.WalletParam;
import com.freebe.code.business.meta.controller.param.WalletQueryParam;
import com.freebe.code.business.meta.entity.Wallet;
import com.freebe.code.business.meta.vo.FinanceInfo;
import com.freebe.code.business.meta.vo.WalletVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface WalletService extends BaseService<Wallet> {

	WalletVO findById(Long id) throws CustomException;

	WalletVO createOrUpdate(WalletParam param) throws CustomException;

	Page<WalletVO> queryPage(WalletQueryParam param) throws CustomException;

	WalletVO findByAddress(String address) throws CustomException;

	WalletVO findByUser(Long memberId) throws CustomException;
	
	
	/**
	 * FreeBe 转账
	 * @param transactionId
	 * @param src
	 * @param dst
	 * @param amount
	 * @throws CustomException
	 */
	void transferFreeBe(Long transactionId, Long src, Long dst, Double amount) throws CustomException;
	
	/**
	 * 获取社区的资产信息
	 * @return
	 * @throws CustomException
	 */
	FinanceInfo getFinanceInfo() throws CustomException;

	/**
	 * 获取 FreeBe 积分
	 * @param id
	 * @param freeBe
	 * @return
	 * @throws CustomException 
	 */
	Double getAmount(Long id, Integer freeBe) throws CustomException;
}
