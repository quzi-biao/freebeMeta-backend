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

import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.QuestionnaireSingleAnswerParam;
import com.freebe.code.business.meta.controller.param.QuestionnaireSingleAnswerQueryParam;
import com.freebe.code.business.meta.entity.QuestionnaireSingleAnswer;
import com.freebe.code.business.meta.repository.QuestionnaireSingleAnswerRepository;
import com.freebe.code.business.meta.service.QuestionnaireSingleAnswerService;
import com.freebe.code.business.meta.vo.QuestionnaireSingleAnswerVO;
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
public class QuestionnaireSingleAnswerServiceImpl extends BaseServiceImpl<QuestionnaireSingleAnswer> implements QuestionnaireSingleAnswerService {
	@Autowired
	private QuestionnaireSingleAnswerRepository repository;

	@Autowired
	private ObjectCaches objectCaches;

	@Override
	public QuestionnaireSingleAnswerVO findById(Long id) throws CustomException {
		QuestionnaireSingleAnswerVO ret = this.objectCaches.get(id, QuestionnaireSingleAnswerVO.class);
		if(null == ret){
			Optional<QuestionnaireSingleAnswer> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}
	
	
	@Override
	public List<QuestionnaireSingleAnswerVO> createOrUpdate(Long id, List<QuestionnaireSingleAnswerParam> answers) throws CustomException {
		List<QuestionnaireSingleAnswerVO> ret = new ArrayList<QuestionnaireSingleAnswerVO>();
		for(QuestionnaireSingleAnswerParam param : answers) {
			param.setAnswerId(id);
			ret.add(this.createOrUpdate(param));
		}
		
		return ret;
	}
	
	@Override
	public List<QuestionnaireSingleAnswerVO> getAnswers(Long answerId) throws CustomException {
		List<QuestionnaireSingleAnswerVO> ret = new ArrayList<QuestionnaireSingleAnswerVO>();
		
		QuestionnaireSingleAnswer example = new QuestionnaireSingleAnswer();
		example.setAnswerId(answerId);
		example.setIsDelete(false);	
		
		List<QuestionnaireSingleAnswer> list = this.repository.findAll(Example.of(example));
		for(QuestionnaireSingleAnswer answer : list) {
			ret.add(toVO(answer));
		}
		
		return ret;
	}
	
	

	@Override
	public QuestionnaireSingleAnswerVO createOrUpdate(QuestionnaireSingleAnswerParam param) throws CustomException {
		QuestionnaireSingleAnswer e = this.getAnswer(param.getAnswerId(), param.getQuestionId());
		
		if(null == e) {
			e = this.getUpdateEntity(param, false);
		}

		e.setQuestionnaireId(param.getQuestionnaireId());
		e.setQuestionId(param.getQuestionId());
		e.setAnswerId(param.getAnswerId());
		e.setUserId(getCurrentUser().getId());
		e.setAnswer(param.getAnswer());

		e = repository.save(e);

		QuestionnaireSingleAnswerVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}

	@Override
	public Page<QuestionnaireSingleAnswerVO> queryPage(QuestionnaireSingleAnswerQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<QuestionnaireSingleAnswer> example = buildSpec(param);

		Page<QuestionnaireSingleAnswer> page = repository.findAll(example, request);
		List<QuestionnaireSingleAnswerVO> retList = new ArrayList<>();

		for(QuestionnaireSingleAnswer e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<QuestionnaireSingleAnswerVO>(retList, page.getPageable(), page.getTotalElements());
	}
	

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, QuestionnaireSingleAnswerVO.class);
		super.softDelete(id);
	}

	private Specification<QuestionnaireSingleAnswer> buildSpec(QuestionnaireSingleAnswerQueryParam param) throws CustomException {
		return new Specification<QuestionnaireSingleAnswer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<QuestionnaireSingleAnswer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<QuestionnaireSingleAnswer> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}
	
	private QuestionnaireSingleAnswer getAnswer(Long answerId, Long questionId) {
		QuestionnaireSingleAnswer e = new QuestionnaireSingleAnswer();
		e.setAnswerId(answerId);
		e.setQuestionId(questionId);
		e.setIsDelete(false);
		
		List<QuestionnaireSingleAnswer> ret = this.repository.findAll(Example.of(e));
		if(null == ret || ret.size() == 0) {
			return null;
		}
		return ret.get(0);
	}

	private QuestionnaireSingleAnswerVO toVO(QuestionnaireSingleAnswer e) throws CustomException {
		QuestionnaireSingleAnswerVO vo = new QuestionnaireSingleAnswerVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setQuestionnaireId(e.getQuestionnaireId());
		vo.setQuestionId(e.getQuestionId());
		vo.setAnswerId(e.getAnswerId());
		vo.setUserId(e.getUserId());
		vo.setAnswer(e.getAnswer());

		return vo;
	}

}
