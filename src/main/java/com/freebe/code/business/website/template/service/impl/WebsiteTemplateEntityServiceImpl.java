package com.freebe.code.business.website.template.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.collect.service.CollectService;
import com.freebe.code.business.website.template.controller.param.WebsiteTemplateEntityParam;
import com.freebe.code.business.website.template.controller.param.WebsiteTemplateEntityQueryParam;
import com.freebe.code.business.website.template.entity.WebsiteTemplateEntity;
import com.freebe.code.business.website.template.repository.WebsiteTemplateEntityRepository;
import com.freebe.code.business.website.template.service.WebsiteTemplateEntityService;
import com.freebe.code.business.website.template.vo.WebsiteTemplateEntityVO;
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
public class WebsiteTemplateEntityServiceImpl extends BaseServiceImpl<WebsiteTemplateEntity> implements WebsiteTemplateEntityService {
	@Autowired
	private WebsiteTemplateEntityRepository repository;

	@Autowired
	private ObjectCaches objectCaches;

	@Autowired
	private CollectService collectService;

	@Override
	public WebsiteTemplateEntityVO findById(Long id) throws CustomException {
		WebsiteTemplateEntityVO ret = this.objectCaches.get(id, WebsiteTemplateEntityVO.class);
		if(null == ret){
			Optional<WebsiteTemplateEntity> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

	@Override
	public WebsiteTemplateEntityVO createOrUpdate(WebsiteTemplateEntityParam param) throws CustomException {
		WebsiteTemplateEntity e = this.getUpdateEntity(param);

		e.setDescription(param.getDescription());
		e.setOwner(getCurrentUser().getId());
		e.setDemoUrl(param.getDemoUrl());
		e.setPicture(param.getPicture());
		e.setPrice(param.getPrice());
		e.setTemplateUrl(param.getTemplateUrl());
		e.setContent(param.getContent());

		e = repository.save(e);

		WebsiteTemplateEntityVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}

	@Override
	public Page<WebsiteTemplateEntityVO> queryPage(WebsiteTemplateEntityQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<WebsiteTemplateEntity> example = buildSpec(param);

		Page<WebsiteTemplateEntity> page = repository.findAll(example, request);
		List<WebsiteTemplateEntityVO> retList = new ArrayList<>();

		for(WebsiteTemplateEntity e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<WebsiteTemplateEntityVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<WebsiteTemplateEntity> buildSpec(WebsiteTemplateEntityQueryParam param) throws CustomException {
		return new Specification<WebsiteTemplateEntity>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<WebsiteTemplateEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<WebsiteTemplateEntity> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private WebsiteTemplateEntityVO toVO(WebsiteTemplateEntity e) throws CustomException {
		WebsiteTemplateEntityVO vo = new WebsiteTemplateEntityVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setDescription(e.getDescription());
		vo.setOwner(e.getOwner());
		vo.setDemoUrl(e.getDemoUrl());
		vo.setPicture(e.getPicture());
		vo.setPrice(e.getPrice());
		vo.setTemplateUrl(e.getTemplateUrl());
		vo.setContent(e.getContent());
		
		vo.setCollected(this.collectService.isCollect(0, e.getId()));

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, WebsiteTemplateEntityVO.class);
		super.softDelete(id);
	}

}
