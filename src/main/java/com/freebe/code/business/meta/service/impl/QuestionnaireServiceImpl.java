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

import com.alipay.api.kms.aliyun.utils.StringUtils;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.QuestionnaireParam;
import com.freebe.code.business.meta.controller.param.QuestionnaireQueryParam;
import com.freebe.code.business.meta.entity.Questionnaire;
import com.freebe.code.business.meta.entity.QuestionnaireAnswer;
import com.freebe.code.business.meta.repository.QuestionnaireAnswerRepository;
import com.freebe.code.business.meta.repository.QuestionnaireRepository;
import com.freebe.code.business.meta.service.QuestionService;
import com.freebe.code.business.meta.service.QuestionnaireService;
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
public class QuestionnaireServiceImpl extends BaseServiceImpl<Questionnaire> implements QuestionnaireService {
	@Autowired
	private QuestionnaireRepository repository;
	
	@Autowired
	private QuestionnaireAnswerRepository questionnaireAnswerRepository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public QuestionnaireVO findById(Long id) throws CustomException {
		QuestionnaireVO ret = this.objectCaches.get(id, QuestionnaireVO.class);
		if(null == ret){
			Optional<Questionnaire> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

	@Override
	public QuestionnaireVO createOrUpdate(QuestionnaireParam param) throws CustomException {
		Questionnaire e = this.getUpdateEntity(param, false);
		if(StringUtils.isEmpty(param.getTitle())) {
			throw new CustomException("请输入标题");
		}
		if(param.getTitle().length() > 24) {
			throw new CustomException("标题过长");
		}
		
		if(null != e.getOwnerId()) {
			if(e.getOwnerId().longValue() != getCurrentUser().getId().longValue()) {
				throw new CustomException("您不能修改本问卷");
			}
		}

		e.setOwnerId(getCurrentUser().getId());
		e.setTitle(param.getTitle());
		e.setPicture(param.getPicture());
		e.setDescription(param.getDescription());
		e.setDeadLine(param.getDeadLine());
		
		e = repository.save(e);
		
		questionService.createOrUpdate(e.getId(), param.getQuestions());

		QuestionnaireVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}

	@Override
	public Page<QuestionnaireVO> queryPage(QuestionnaireQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<Questionnaire> example = buildSpec(param);

		Page<Questionnaire> page = repository.findAll(example, request);
		List<QuestionnaireVO> retList = new ArrayList<>();

		for(Questionnaire e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<QuestionnaireVO>(retList, page.getPageable(), page.getTotalElements());
	}
	

	@Override
	public List<QuestionnaireVO> answered() throws CustomException {
		QuestionnaireAnswer example = new QuestionnaireAnswer();
		example.setUserId(getCurrentUser().getId());
		example.setIsDelete(false);
		
		List<QuestionnaireAnswer> answerList = questionnaireAnswerRepository.findAll(Example.of(example));
		if(null == answerList || answerList.size() == 0) {
			return null;
		}
		
		List<QuestionnaireVO> ret = new ArrayList<>();
		for(QuestionnaireAnswer a : answerList) {
			ret.add(this.findById(a.getQuestionnaireId()));
		}
		
		return ret;
	}

	private Specification<Questionnaire> buildSpec(QuestionnaireQueryParam param) throws CustomException {
		return new Specification<Questionnaire>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Questionnaire> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<Questionnaire> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				builder.addEqual("isDelete", param.getOwnerId());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private QuestionnaireVO toVO(Questionnaire e) throws CustomException {
		QuestionnaireVO vo = new QuestionnaireVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setOwnerId(e.getOwnerId());
		vo.setOwner(userService.getUser(e.getOwnerId()));
		vo.setTitle(e.getTitle());
		vo.setPicture(e.getPicture());
		vo.setDescription(e.getDescription());
		vo.setDeadLine(e.getDeadLine());
		vo.setQuestions(this.questionService.getQuestion(e.getId()));

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, QuestionnaireVO.class);
		super.softDelete(id);
	}

}
