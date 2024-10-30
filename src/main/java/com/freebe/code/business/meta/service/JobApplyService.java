package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.AuditParam;
import com.freebe.code.business.meta.controller.param.JobApplyParam;
import com.freebe.code.business.meta.controller.param.JobApplyQueryParam;
import com.freebe.code.business.meta.entity.JobApply;
import com.freebe.code.business.meta.vo.JobApplyVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface JobApplyService extends BaseService<JobApply> {

	JobApplyVO findById(Long id) throws CustomException;

	JobApplyVO createOrUpdate(JobApplyParam param) throws CustomException;

	Page<JobApplyVO> queryPage(JobApplyQueryParam param) throws CustomException;
	
	/**
	 * 问卷回答完成
	 * @param jobId
	 * @param answerId
	 * @return
	 */
	JobApplyVO questionnaireEnd(Long applyId) throws CustomException;
	
	/**
	 * 问卷回答审核
	 * @param jobId
	 * @param answerId
	 * @return
	 */
	JobApplyVO answerAudit(AuditParam param) throws CustomException;
	
	/**
	 * 任务完成
	 * @param jobId
	 * @param answerId
	 * @return
	 */
	JobApplyVO taskEnd(Long applyId) throws CustomException;
	
	/**
	 * 任务审核
	 * @param jobId
	 * @param answerId
	 * @return
	 */
	JobApplyVO taskAudit(AuditParam param) throws CustomException;
	
	/**
	 * 申请审核
	 * @param jobId
	 * @param answerId
	 * @return
	 */
	JobApplyVO review(AuditParam param) throws CustomException;

	/**
	 * 获取我的申请
	 * @param jobId
	 * @return
	 * @throws CustomException 
	 */
	JobApplyVO findApply(Long jobId) throws CustomException;
}
