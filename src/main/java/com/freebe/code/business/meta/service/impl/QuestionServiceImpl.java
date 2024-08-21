package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.QuestionParam;
import com.freebe.code.business.meta.controller.param.QuestionQueryParam;
import com.freebe.code.business.meta.entity.Question;
import com.freebe.code.business.meta.repository.QuestionRepository;
import com.freebe.code.business.meta.service.QuestionService;
import com.freebe.code.business.meta.vo.QuestionVO;
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
public class QuestionServiceImpl extends BaseServiceImpl<Question> implements QuestionService {
	@Autowired
	private QuestionRepository repository;

	@Autowired
	private ObjectCaches objectCaches;

	@Override
	public QuestionVO findById(Long id) throws CustomException {
		QuestionVO ret = this.objectCaches.get(id, QuestionVO.class);
		if(null == ret){
			Optional<Question> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}
	
	@Override
	public List<QuestionVO> createOrUpdate(Long id, List<QuestionParam> questions) throws CustomException {
		if(null == questions || questions.size() == 0) {
			return null;
		}
		
		List<QuestionVO> rets = new ArrayList<>();
		for(QuestionParam param : questions) {
			if(StringUtils.isEmpty(param.getContent())) {
				continue;
			}
			param.setQuestionnaireId(id);
			rets.add(this.createOrUpdate(param));
		}
		
		List<QuestionVO> exists = this.getQuestion(id);
		// 原先存在，如今不存在的删除
		Set<Long> removed = new HashSet<>();
		for(QuestionVO vo : exists) {
			boolean remove = true;
			for(QuestionVO ret : rets) {
				if(vo.getId().longValue() == ret.getId().longValue()) {
					remove = false;
				}
			}
			if(remove) {
				removed.add(vo.getId());
			}
		}
		for(Long rmoveId : removed) {
			this.softDelete(rmoveId);
		}
		
		return rets;
	}

	@Override
	public List<QuestionVO> getQuestion(Long id) throws CustomException {
		if(null == id) {
			return null;
		}
		
		Question example = new Question();
		example.setQuestionnaireId(id);
		example.setIsDelete(false);
		
		List<Question> questions = this.repository.findAll(Example.of(example));
		
		List<QuestionVO> questionVos = new ArrayList<>();
		
		for(Question question : questions) {
			questionVos.add(this.findById(question.getId()));
		}
		
		return questionVos;
	}

	@Override
	public QuestionVO createOrUpdate(QuestionParam param) throws CustomException {
		Question e = this.getUpdateEntity(param, false);

		e.setQuestionnaireId(param.getQuestionnaireId());
		e.setNumber(param.getNumber());
		e.setContent(param.getContent());
		e.setDescription(param.getDescription());
		e.setQuestionType(param.getQuestionType());
		e.setQuestionSelect(param.getQuestionSelect());

		e = repository.save(e);

		QuestionVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}

	@Override
	public Page<QuestionVO> queryPage(QuestionQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<Question> example = buildSpec(param);

		Page<Question> page = repository.findAll(example, request);
		List<QuestionVO> retList = new ArrayList<>();

		for(Question e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<QuestionVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<Question> buildSpec(QuestionQueryParam param) throws CustomException {
		return new Specification<Question>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Question> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<Question> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private QuestionVO toVO(Question e) throws CustomException {
		QuestionVO vo = new QuestionVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setQuestionnaireId(e.getQuestionnaireId());
		vo.setNumber(e.getNumber());
		vo.setContent(e.getContent());
		vo.setDescription(e.getDescription());
		vo.setQuestionType(e.getQuestionType());
		vo.setQuestionSelect(e.getQuestionSelect());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, QuestionVO.class);
		super.softDelete(id);
	}

}
