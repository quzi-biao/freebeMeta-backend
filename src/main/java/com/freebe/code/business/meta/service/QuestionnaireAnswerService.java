package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.meta.entity.QuestionnaireAnswer;
import com.freebe.code.business.meta.vo.QuestionnaireAnswerVO;
import com.freebe.code.business.meta.controller.param.QuestionnaireAnswerParam;
import com.freebe.code.business.meta.controller.param.QuestionnaireAnswerQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface QuestionnaireAnswerService extends BaseService<QuestionnaireAnswer> {

	QuestionnaireAnswerVO findById(Long id) throws CustomException;

	QuestionnaireAnswerVO createOrUpdate(QuestionnaireAnswerParam param) throws CustomException;

	Page<QuestionnaireAnswerVO> queryPage(QuestionnaireAnswerQueryParam param) throws CustomException;

	/**
	 * 调整公开属性
	 * @param answerId
	 * @param isPulbic
	 * @return
	 * @throws CustomException
	 */
	Boolean changePublic(Long answerId, Boolean isPulbic) throws CustomException;

	/**
	 * 获取当前用户的回答
	 * @param questionnaireId
	 * @return
	 * @throws CustomException
	 */
	QuestionnaireAnswerVO getAnswer(Long questionnaireId, Long userId) throws CustomException;

}
