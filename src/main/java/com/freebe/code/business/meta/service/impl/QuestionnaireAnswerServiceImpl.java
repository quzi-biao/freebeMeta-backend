package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.QuestionnaireAnswerParam;
import com.freebe.code.business.meta.controller.param.QuestionnaireAnswerQueryParam;
import com.freebe.code.business.meta.controller.param.QuestionnaireSingleAnswerParam;
import com.freebe.code.business.meta.entity.QuestionnaireAnswer;
import com.freebe.code.business.meta.repository.QuestionnaireAnswerRepository;
import com.freebe.code.business.meta.service.QuestionnaireAnswerService;
import com.freebe.code.business.meta.service.QuestionnaireService;
import com.freebe.code.business.meta.service.QuestionnaireSingleAnswerService;
import com.freebe.code.business.meta.vo.QuestionnaireAnswerVO;
import com.freebe.code.business.meta.vo.QuestionnaireVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ObjectCaches;
import com.freebe.code.util.PageUtils;
import com.freebe.code.util.QueryUtils.QueryBuilder;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class QuestionnaireAnswerServiceImpl extends BaseServiceImpl<QuestionnaireAnswer> implements QuestionnaireAnswerService {
	@Autowired
	private QuestionnaireAnswerRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private QuestionnaireSingleAnswerService answerService;
	
	@Autowired
	private QuestionnaireService questionnaireService;
	
	@Autowired
	private UserService userService;

	@Override
	public QuestionnaireAnswerVO findById(Long id) throws CustomException {
		QuestionnaireAnswerVO ret = this.objectCaches.get(id, QuestionnaireAnswerVO.class);
		
		if(null == ret){
			Optional<QuestionnaireAnswer> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		
		objectCaches.put(ret.getId(), ret);
		return ret;
	}
	
	@Override
	public Boolean changePublic(Long answerId, Boolean isPulbic) throws CustomException {
		QuestionnaireAnswer answer = this.repository.getById(answerId);
		
		if(null == answer) {
			throw new CustomException("回答不存在");
		}
		
		if(answer.getUserId() != this.getCurrentUser().getId()) {
			throw new CustomException("您无权执行此操作");
		}
		answer.setIsPublic(isPulbic);
		answer = this.repository.save(answer);
		
		return answer.getIsPublic();
	}

	@Override
	public QuestionnaireAnswerVO createOrUpdate(QuestionnaireAnswerParam param) throws CustomException {
		QuestionnaireAnswer e = checkParam(param);

		e.setQuestionnaireId(param.getQuestionnaireId());
		e.setUserId(getCurrentUser().getId());
		if(null == param.getIsPublic()) {
			e.setIsPublic(false);
		}else {
			e.setIsPublic(param.getIsPublic());
		}
		
		e = repository.save(e);

		QuestionnaireAnswerVO vo = toVO(e);
		
		this.answerService.createOrUpdate(e.getId(), param.getAnswers());
		
		objectCaches.put(vo.getId(), vo);

		return vo;
	}
	
	@Override
	public QuestionnaireAnswerVO getAnswer(Long questionnaireId, Long userId) throws CustomException {
		QuestionnaireAnswer example = new QuestionnaireAnswer();
		example.setQuestionnaireId(questionnaireId);
		example.setUserId(userId);
		example.setIsDelete(false);
	
		List<QuestionnaireAnswer> content = this.repository.findAll(Example.of(example));
		if(null == content || content.size() == 0) {
			return null;
		}
		
		QuestionnaireAnswer e = content.get(0);
		if(e.getIsPublic()) {
			return toVO(e);
		}
		
		if(e.getUserId().longValue() == this.getCurrentUser().getId()) {
			return toVO(e);
		}
		
		throw new CustomException("回答未公开");
	}
	

	@Override
	public Page<QuestionnaireAnswerVO> queryPage(QuestionnaireAnswerQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);
		param.setIsPublic(true);
		
		Specification<QuestionnaireAnswer> example = buildSpec(param);

		Page<QuestionnaireAnswer> page = repository.findAll(example, request);
		List<QuestionnaireAnswerVO> retList = new ArrayList<>();

		for(QuestionnaireAnswer e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<QuestionnaireAnswerVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<QuestionnaireAnswer> buildSpec(QuestionnaireAnswerQueryParam param) throws CustomException {
		return new Specification<QuestionnaireAnswer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<QuestionnaireAnswer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<QuestionnaireAnswer> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				builder.addEqual("isPublic", param.getIsPublic());
				
				builder.addEqual("questionnaireId", param.getQuestionnaireId());
				builder.addEqual("userId", param.getUserId());

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private QuestionnaireAnswerVO toVO(QuestionnaireAnswer e) throws CustomException {
		QuestionnaireAnswerVO vo = new QuestionnaireAnswerVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setQuestionnaireId(e.getQuestionnaireId());
		vo.setUserId(e.getUserId());
		vo.setIsPublic(e.getIsPublic());
		vo.setQuestionnaire(this.questionnaireService.findById(e.getQuestionnaireId()));
		vo.setAnswers(this.answerService.getAnswers(e.getId()));
		vo.setUser(this.userService.getUser(e.getUserId()));
		
		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, QuestionnaireAnswerVO.class);
		super.softDelete(id);
	}
	
	private QuestionnaireAnswer checkParam(QuestionnaireAnswerParam param) throws CustomException {
		if(null == param.getQuestionnaireId()) {
			throw new CustomException("你输入您回答的问卷");
		}
		
		if(param.getAnswers() == null || param.getAnswers().size() == 0) {
			throw new CustomException("您还未回答问题");
		}
		
		QuestionnaireVO questionnaire = questionnaireService.findById(param.getQuestionnaireId());
		if(null == questionnaire) {
			throw new CustomException("问卷不存在");
		}
		
		if(questionnaire.getQuestions().size() != param.getAnswers().size()) {
			throw new CustomException("您有问题未回答");
		}
		
		for(QuestionnaireSingleAnswerParam answer : param.getAnswers()) {
			if(null == answer.getQuestionId()) {
				throw new CustomException("参数错误");
			}
			if(answer.getAnswer() == null || answer.getAnswer().length() == 0) {
				throw new CustomException("您有问题未回答");
			}
			if(null == answer.getQuestionnaireId()) {
				answer.setQuestionnaireId(param.getQuestionnaireId());
			}
		}
		
		QuestionnaireAnswer e = getCurrAnswer(param.getQuestionnaireId());
		if(null != param.getId()) {
			QuestionnaireAnswerVO vo = this.findById(param.getId());
			if(vo.getUserId().longValue() != e.getUserId()) {
				throw new CustomException("您不能修改此回答");
			}
		}else {
			e = getUpdateEntity(param, false);
		}
		return e;
	}
	
	
	private QuestionnaireAnswer getCurrAnswer(Long questionnaireId) {
		QuestionnaireAnswer example = new QuestionnaireAnswer();
		example.setIsDelete(false);
		example.setUserId(this.getCurrentUser().getId());
		example.setQuestionnaireId(questionnaireId);
		
		List<QuestionnaireAnswer> ret = this.repository.findAll(Example.of(example));
		if(null == ret || ret.size() == 0) {
			return null;
		}
		
		return ret.get(0);
	}

}
