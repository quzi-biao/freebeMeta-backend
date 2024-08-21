package com.freebe.code.business.meta.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.meta.entity.Question;
import com.freebe.code.business.meta.vo.QuestionVO;
import com.freebe.code.business.meta.controller.param.QuestionParam;
import com.freebe.code.business.meta.controller.param.QuestionQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface QuestionService extends BaseService<Question> {

	QuestionVO findById(Long id) throws CustomException;

	QuestionVO createOrUpdate(QuestionParam param) throws CustomException;

	Page<QuestionVO> queryPage(QuestionQueryParam param) throws CustomException;

	/**
	 * 批量添加和更新问卷
	 * @param id
	 * @param questions
	 * @throws CustomException 
	 */
	List<QuestionVO> createOrUpdate(Long id, List<QuestionParam> questions) throws CustomException;
	
	/**
	 * 批量添加和更新问卷
	 * @param id
	 * @param questions
	 * @throws CustomException 
	 */
	List<QuestionVO> getQuestion(Long id) throws CustomException;

}
