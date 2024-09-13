package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.TransactionParam;
import com.freebe.code.business.meta.controller.param.TransactionQueryParam;
import com.freebe.code.business.meta.entity.Transaction;
import com.freebe.code.business.meta.vo.ProjectReward;
import com.freebe.code.business.meta.vo.TransactionVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface TransactionService extends BaseService<Transaction> {
	public static final String ROLE_CF_CODE = "CF";
	
	public static final String ROLE_ADVANTURE_AUDITOR_CODE = "ADVANTURE_AUDITOR";
	
	public static final long PUBLIC_WALLET_ID = 1;

	TransactionVO findById(Long id) throws CustomException;

	TransactionVO createOrUpdate(TransactionParam param) throws CustomException;

	Page<TransactionVO> queryPage(TransactionQueryParam param) throws CustomException;

	/**
	 * 系统内部创建
	 * @param param
	 * @return
	 * @throws CustomException
	 */
	TransactionVO innerCreateOrUpdate(TransactionParam param) throws CustomException;
	
	/**
	 * 交易确认,确认后的交易进入公示期
	 * @param param
	 * @return
	 * @throws CustomException
	 */
	TransactionVO transactionAudit(Long transactionId) throws CustomException;
	
	/**
	 * 交易确认
	 * @param transactionId
	 * @return
	 * @throws CustomException
	 */
	TransactionVO confirm(Long transactionId) throws CustomException;
	
	/**
	 * 交易撤销
	 * @param transactionId
	 * @return
	 * @throws CustomException
	 */
	TransactionVO reback(Long transactionId) throws CustomException;
	
	/**
	 * 交易确认,确认后的交易进入公示期
	 * @param param
	 * @return
	 * @throws CustomException
	 */
	ProjectReward getProjectReward(Long projectId) throws CustomException;

}
