package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.CollectParam;
import com.freebe.code.business.meta.controller.param.CollectQueryParam;
import com.freebe.code.business.meta.entity.Collect;
import com.freebe.code.business.meta.repository.CollectRepository;
import com.freebe.code.business.meta.service.CollectService;
import com.freebe.code.business.meta.service.JobService;
import com.freebe.code.business.meta.service.MarketProvideService;
import com.freebe.code.business.meta.type.InteractionEntityType;
import com.freebe.code.business.meta.type.InteractionType;
import com.freebe.code.business.meta.vo.CollectVO;
import com.freebe.code.business.website.template.service.WebsiteTemplateEntityService;
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
public class CollectServiceImpl extends BaseServiceImpl<Collect> implements CollectService {
	@Autowired
	private CollectRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private MarketProvideService marketProvideService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private WebsiteTemplateEntityService websiteTemplateEntityService;
	
	@Autowired
	private InteractionCountUtils countUtils;

	@Override
	public CollectVO findById(Long id) throws CustomException {
		CollectVO ret = this.objectCaches.get(id, CollectVO.class);
		if(null == ret){
			Optional<Collect> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

	@Transactional
	@Override
	public CollectVO createOrUpdate(CollectParam param) throws CustomException {
		if(null == param.getTypeId() || null == param.getEntityId()) {
			throw new CustomException("参数错误");
		}
		
		Collect e = new Collect();

		e.setUserId(getCurrentUser().getId());
		e.setTypeId(param.getTypeId());
		e.setEntityId(param.getEntityId());
		if(this.repository.exists(Example.of(e))) {
			throw new CustomException("您已经收藏了");
		}
		e.setIsDelete(false);
		e = repository.save(e);

		CollectVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);
		countUtils.inc(param.getEntityId(), param.getTypeId(), InteractionType.COLLECT);

		return vo;
	}
	
	@Transactional
	@Override
	public CollectVO cancel(CollectParam param) throws CustomException {
		if(null == param.getTypeId() || null == param.getEntityId()) {
			throw new CustomException("参数错误");
		}
		
		Collect c = new Collect();
		c.setTypeId(param.getTypeId());
		c.setEntityId(param.getEntityId());
		c.setUserId(getCurrentUser().getId());
		c.setIsDelete(false);
		
		List<Collect> collects = this.repository.findAll(Example.of(c));
		if(null == collects || collects.size() == 0) {
			return null;
		}
		
		this.repository.deleteAll(collects);
		countUtils.dec(param.getEntityId(), param.getTypeId(), InteractionType.COLLECT);
		
		return null;
	}

	@Override
	public Page<CollectVO> queryPage(CollectQueryParam param) throws CustomException {
		param.setOrder("id");
		param.setUserId(getCurrentUser().getId());
		
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<Collect> example = buildSpec(param);

		Page<Collect> page = repository.findAll(example, request);
		List<CollectVO> retList = new ArrayList<>();

		for(Collect e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<CollectVO>(retList, page.getPageable(), page.getTotalElements());
	}
	
	@Override
	public boolean isCollect(long typeId, Long entityId) {
		if(getCurrentUser() == null) {
			return false;
		}
		Collect c = new Collect();
		c.setTypeId(typeId);
		c.setEntityId(entityId);
		c.setUserId(getCurrentUser().getId());
		c.setIsDelete(false);
		
		return this.repository.exists(Example.of(c));
	}

	private Specification<Collect> buildSpec(CollectQueryParam param) throws CustomException {
		return new Specification<Collect>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Collect> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<Collect> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				builder.addEqual("typeId", param.getTypeId());
				builder.addEqual("userId", param.getUserId());

				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private CollectVO toVO(Collect e) throws CustomException {
		CollectVO vo = new CollectVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setUserId(e.getUserId());
		vo.setTypeId(e.getTypeId());
		
		if(e.getTypeId().intValue() == InteractionEntityType.PROVIDE) {
			vo.setEntity(this.marketProvideService.findById(e.getEntityId()));
		}else if(e.getTypeId().intValue() == InteractionEntityType.WEBSITE_TEMPLATE) {
			vo.setEntity(this.websiteTemplateEntityService.findById(e.getEntityId()));
		}else if(e.getTypeId().intValue() == InteractionEntityType.JOB) {
			vo.setEntity(this.jobService.findById(e.getEntityId()));
		}

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, CollectVO.class);
		super.softDelete(id);
	}


}
