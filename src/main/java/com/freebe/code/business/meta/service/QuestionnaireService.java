package com.freebe.code.business.meta.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.QuestionnaireParam;
import com.freebe.code.business.meta.controller.param.QuestionnaireQueryParam;
import com.freebe.code.business.meta.entity.Questionnaire;
import com.freebe.code.business.meta.vo.QuestionnaireVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface QuestionnaireService extends BaseService<Questionnaire> {

	QuestionnaireVO findById(Long id) throws CustomException;

	QuestionnaireVO createOrUpdate(QuestionnaireParam param) throws CustomException;

	Page<QuestionnaireVO> queryPage(QuestionnaireQueryParam param) throws CustomException;

	List<QuestionnaireVO> answered() throws CustomException;

}
