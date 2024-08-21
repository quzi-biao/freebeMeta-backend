package com.freebe.code.business.meta.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.meta.entity.QuestionnaireSingleAnswer;
import com.freebe.code.business.meta.vo.QuestionnaireSingleAnswerVO;
import com.freebe.code.business.meta.controller.param.QuestionnaireSingleAnswerParam;
import com.freebe.code.business.meta.controller.param.QuestionnaireSingleAnswerQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface QuestionnaireSingleAnswerService extends BaseService<QuestionnaireSingleAnswer> {

	QuestionnaireSingleAnswerVO findById(Long id) throws CustomException;

	QuestionnaireSingleAnswerVO createOrUpdate(QuestionnaireSingleAnswerParam param) throws CustomException;

	Page<QuestionnaireSingleAnswerVO> queryPage(QuestionnaireSingleAnswerQueryParam param) throws CustomException;

	/**
	 * 批量创建
	 * @param id
	 * @param answers
	 * @throws CustomException 
	 */
	List<QuestionnaireSingleAnswerVO> createOrUpdate(Long id, List<QuestionnaireSingleAnswerParam> answers) throws CustomException;

	/**
	 * 获取问题答案
	 * @param answerId
	 * @return
	 * @throws CustomException
	 */
	List<QuestionnaireSingleAnswerVO> getAnswers(Long answerId) throws CustomException;

}
