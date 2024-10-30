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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alipay.api.kms.aliyun.utils.StringUtils;
import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.ContentDraftParam;
import com.freebe.code.business.meta.controller.param.ContentDraftQueryParam;
import com.freebe.code.business.meta.entity.ContentDraft;
import com.freebe.code.business.meta.repository.ContentDraftRepository;
import com.freebe.code.business.meta.service.ContentDataService;
import com.freebe.code.business.meta.service.ContentDraftService;
import com.freebe.code.business.meta.type.AuditStatus;
import com.freebe.code.business.meta.type.ContentCategory;
import com.freebe.code.business.meta.type.ContentType;
import com.freebe.code.business.meta.vo.ContentDraftVO;
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
public class ContentDraftServiceImpl extends BaseServiceImpl<ContentDraft> implements ContentDraftService {
	@Autowired
	private ContentDraftRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private ContentDataService contentDataService;
	
	@Autowired
	private UserService userService;

	@Override
	public ContentDraftVO findById(Long id) throws CustomException {
		ContentDraft ret = getEntity(id);
		return toVO(ret);
	}

	@Transactional
	@Override
	public ContentDraftVO createOrUpdate(ContentDraftParam param) throws CustomException {
		if(StringUtils.isEmpty(param.getTitle())) {
			throw new CustomException("请输入文章标题");
		}
		
		if(null == param.getContentType()) {
			param.setContentType(ContentType.NORMAL);
		}
		if(null == param.getCategory()) {
			param.setCategory(ContentCategory.MIINE);
		}
		
		ContentDraft e = this.getUpdateEntity(param, false);
		Long currId = getCurrentUser().getId();
		
		if(null != e.getOwnerId() && e.getOwnerId().longValue() != currId.longValue()) {
			throw new CustomException("您无权修改本内容");
		}

		e.setUpdateTime(System.currentTimeMillis());
		e.setOwnerId(getCurrentUser().getId());
		
		e.setRepplyContent(param.getRepplyContent());
		e.setProjectId(param.getProjectId());
		e.setContentType(param.getContentType());
		e.setCategory(param.getCategory());
		e.setTitle(param.getTitle());
		//e.setContentKey(param.getContentKey());
		e.setPicture(param.getPicture());
		e.setContentAbstract(param.getContentAbstract());
		e.setStatus(AuditStatus.UNKOWN);

		String key = this.contentDataService.updateContent(e.getContentKey(), param.getContent(), ContentType.NORMAL);
		e.setContentKey(key);
		
		e = repository.save(e);

		ContentDraftVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}

	@Override
	public Page<ContentDraftVO> queryPage(ContentDraftQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<ContentDraft> example = buildSpec(param);

		Page<ContentDraft> page = repository.findAll(example, request);
		List<ContentDraftVO> retList = new ArrayList<>();

		for(ContentDraft e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<ContentDraftVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<ContentDraft> buildSpec(ContentDraftQueryParam param) throws CustomException {
		return new Specification<ContentDraft>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ContentDraft> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<ContentDraft> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				builder.addEqual("ownerId", param.getOwnerId());
				builder.addEqual("repplyContent", param.getRepplyContent());
				builder.addEqual("projectId", param.getProjectId());
				builder.addEqual("contentType", param.getContentType());
				builder.addEqual("category", param.getCategory());
				builder.addEqual("status", param.getStatus());
				builder.addEqual("contentKey", param.getContentKey());

				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private ContentDraftVO toVO(ContentDraft e) throws CustomException {
		ContentDraftVO vo = new ContentDraftVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setUpdateTime(e.getUpdateTime());
		vo.setDeployTime(e.getDeployTime());

		vo.setOwnerId(e.getOwnerId());
		vo.setOwner(userService.getUser(e.getOwnerId()));
		vo.setRepplyContent(e.getRepplyContent());
		
		vo.setProjectId(e.getProjectId());
		vo.setContentType(e.getContentType());
		vo.setCategory(e.getCategory());
		vo.setTitle(e.getTitle());
		vo.setContentKey(e.getContentKey());
		vo.setPicture(e.getPicture());
		vo.setContentAbstract(e.getContentAbstract());
		vo.setDeployTime(e.getDeployTime());
		vo.setStatus(e.getStatus());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, ContentDraftVO.class);
		super.softDelete(id);
	}
	
	private ContentDraft getEntity(Long id) {
		ContentDraft ret = this.objectCaches.get(id, ContentDraft.class);
		if(null == ret){
			Optional<ContentDraft> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

}
