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
import com.freebe.code.business.meta.controller.param.AuditParam;
import com.freebe.code.business.meta.controller.param.ContentDataParam;
import com.freebe.code.business.meta.controller.param.ContentParam;
import com.freebe.code.business.meta.controller.param.ContentQueryParam;
import com.freebe.code.business.meta.entity.Content;
import com.freebe.code.business.meta.repository.ContentRepository;
import com.freebe.code.business.meta.service.ContentDataService;
import com.freebe.code.business.meta.service.ContentDraftService;
import com.freebe.code.business.meta.service.ContentService;
import com.freebe.code.business.meta.type.AuditStatus;
import com.freebe.code.business.meta.type.ContentCategory;
import com.freebe.code.business.meta.type.ContentType;
import com.freebe.code.business.meta.type.PermissionCode;
import com.freebe.code.business.meta.vo.ContentDraftVO;
import com.freebe.code.business.meta.vo.ContentVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ObjectCaches;
import com.freebe.code.util.CodeUtils;
import com.freebe.code.util.PageUtils;
import com.freebe.code.util.QueryUtils.QueryBuilder;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class ContentServiceImpl extends BaseServiceImpl<Content> implements ContentService {
	@Autowired
	private ContentRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContentDraftService contentDraftService;
	
	@Autowired
	private ContentDataService contentDataService;

	@Override
	public ContentVO findById(Long id) throws CustomException {
		Content ret = getEntity(id);
		return toVO(ret);
	}

	@Transactional
	@Override
	public ContentVO deployFromDraft(Long draftId) throws CustomException {
		ContentDraftVO draft = this.contentDraftService.findById(draftId);
		if(null == draft) {
			throw new CustomException("草稿不存在");
		}
		
		String content = this.contentDataService.findByKey(draft.getContentKey());
		if(null == content || content.length() < 100) {
			throw new CustomException("字数太少，不能发布");
		}
		if(null == draft.getContentAbstract() || draft.getContentAbstract().length() == 0) {
			throw new CustomException("请先填写内容摘要");
		}
		
		Content e = new Content();
		e.setIsDelete(false);
		e.setInUse(true);
		
		e.setCategory(draft.getCategory());
		e.setCode(CodeUtils.generateCode(Content.class));
		e.setCollect(0L);
		e.setComment(0L);
		e.setContentAbstract(draft.getContentAbstract());
		e.setContentType(draft.getContentType());
		e.setCreateTime(System.currentTimeMillis());
		e.setDeployTime(System.currentTimeMillis());
		e.setLike(0L);
		e.setOwnerId(draft.getOwnerId());
		e.setPicture(draft.getPicture());
		e.setProjectId(draft.getProjectId());
		e.setRepplyContent(draft.getRepplyContent());
		e.setShare(0L);
		//e.setStatus(AuditStatus.UNKOWN);
		e.setStatus(AuditStatus.PASS); // 内容先不审核了
		e.setTitle(draft.getTitle());
		
		e.setContentKey(draft.getContentKey());
		
		ContentDataParam dataParam = new ContentDataParam();
		dataParam.setContent(content);
		dataParam.setContentType(draft.getContentType());
		String key = this.contentDataService.createOrUpdate(dataParam);
		e.setContentKey(key);
		
		e = this.repository.save(e);
		objectCaches.put(e.getId(), e);
		
		return toVO(e);
	}

	@Transactional
	@Override
	public ContentVO createOrUpdate(ContentParam param) throws CustomException {
		if(StringUtils.isEmpty(param.getTitle())) {
			throw new CustomException("请输入内容标题");
		}
		
		if(null == param.getContentType()) {
			param.setContentType(ContentType.NORMAL);
		}
		if(null == param.getCategory()) {
			param.setCategory(ContentCategory.MIINE);
		}
		
		Content e = this.getUpdateEntity(param, false);
		
		Long currId = getCurrentUser().getId();
		
		if(null != e.getOwnerId() && e.getOwnerId().longValue() != currId.longValue()) {
			throw new CustomException("您无权修改本内容");
		}

		e.setOwnerId(currId);
		e.setRepplyContent(param.getRepplyContent());
		e.setProjectId(param.getProjectId());
		e.setContentType(param.getContentType());
		e.setCategory(param.getCategory());
		e.setTitle(param.getTitle());
		e.setPicture(param.getPicture());
		e.setContentAbstract(param.getContentAbstract());
		e.setStatus(AuditStatus.UNKOWN);
		e.setLike(0L);
		e.setCollect(0L);
		e.setShare(0L);
		e.setComment(0L);
		
		String key = this.contentDataService.updateContent(e.getContentKey(), param.getContent(), ContentType.NORMAL);
		e.setContentKey(key);

		e = repository.save(e);

		ContentVO vo = toVO(e);
		objectCaches.put(e.getId(), e);

		return vo;
	}
	
	@Transactional
	@Override
	public ContentVO auditContent(AuditParam param) throws CustomException {
		this.checkPermssion(PermissionCode.CONTENT_AUDIT);
		
		Content e = this.getEntity(param.getId());
		if(null == e) {
			throw new CustomException("内容不存在");
		}
		
		e.setAuditor(this.getCurrentUser().getId());
		e.setAuditorComment(param.getEvaluate());
		
		if(param.getPass()) {
			e.setStatus(AuditStatus.PASS);
		}else {
			e.setStatus(AuditStatus.REJECT);
		}
		
		objectCaches.put(e.getId(), e);
		
		return toVO(e);
	}

	@Override
	public Page<ContentVO> queryPage(ContentQueryParam param) throws CustomException {
		param.setOrder("id");
		if(null == param.getStatus() || AuditStatus.PASS != param.getStatus()) {
			this.checkPermssion(PermissionCode.CONTENT_AUDIT);
		}else {
			param.setStatus(AuditStatus.PASS);
		}
		
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<Content> example = buildSpec(param);

		Page<Content> page = repository.findAll(example, request);
		List<ContentVO> retList = new ArrayList<>();

		for(Content e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<ContentVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<Content> buildSpec(ContentQueryParam param) throws CustomException {
		return new Specification<Content>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Content> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<Content> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);

				builder.addEqual("ownerId", param.getOwnerId());
				builder.addEqual("repplyContent", param.getRepplyContent());
				builder.addEqual("projectId", param.getProjectId());
				builder.addEqual("contentType", param.getContentType());
				builder.addEqual("category", param.getCategory());
				builder.addEqual("status", param.getStatus()); // 只有审核通过的文章才返回
				builder.addEqual("contentKey", param.getContentKey());
				
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private ContentVO toVO(Content e) throws CustomException {
		ContentVO vo = new ContentVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

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

		vo.setLike(e.getLike());
		vo.setCollect(e.getCollect());
		vo.setShare(e.getShare());
		vo.setComment(e.getComment());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, ContentVO.class);
		super.softDelete(id);
	}

	
	private Content getEntity(Long id) {
		Content ret = this.objectCaches.get(id, Content.class);
		if(null == ret){
			Optional<Content> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

}
