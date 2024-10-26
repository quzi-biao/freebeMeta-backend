package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import com.alibaba.fastjson.JSONObject;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.RichTextParam;
import com.freebe.code.business.meta.controller.param.RichTextQueryParam;
import com.freebe.code.business.meta.controller.param.RichTextUpdateParam;
import com.freebe.code.business.meta.entity.RichText;
import com.freebe.code.business.meta.repository.RichTextRepository;
import com.freebe.code.business.meta.service.RichTextService;
import com.freebe.code.business.meta.type.RichTextContentType;
import com.freebe.code.business.meta.type.RichTextOwnerType;
import com.freebe.code.business.meta.vo.RichTextVO;
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
public class RichTextServiceImpl extends BaseServiceImpl<RichText> implements RichTextService {
	@Autowired
	private RichTextRepository repository;

	@Autowired
	private ObjectCaches objectCaches;

	@Override
	public RichTextVO findByDocId(String docId) throws CustomException {
		if(null == docId) {
			return null;
		}
		RichText ret = findEntityById(docId);
		if(null == ret) {
			return null;
		}
		return toVO(ret);
	}

	@Override
	public String createOrUpdate(RichTextParam param) throws CustomException {
		RichText e = this.getUpdateEntity(param, false);
		if(null == param.getName()) {
			e.setName("无标题");
		}
		
		Long currUserId = this.getCurrentUser().getId();
		int ownerType = param.getOwnerType() == null ? RichTextOwnerType.PERSONAL : param.getOwnerType().intValue();
		e.setOwnerType(ownerType);
		
		if(ownerType == RichTextOwnerType.PERSONAL) {
			e.setOwnerId(currUserId);
		}else {
			// 需要进行权限约束，比如项目组成员才能创建
			e.setOwnerId(param.getOwnerId());
		}
		
		int contentType = param.getContentType() == null ? RichTextContentType.BLOCK_SUITE : param.getContentType().intValue();
		e.setContentType(contentType);
		e.setDocId(e.getCode());
		
		e = repository.save(e);
		if(contentType == RichTextContentType.BLOCK_SUITE) {
			RichText blob = JSONObject.parseObject(JSONObject.toJSONString(e), RichText.class);
			blob.setId(null);
			blob.setContentType(RichTextContentType.BLOCK_SUITE_BLOB);
			blob.setDocId(e.getDocId() + "_blob");
			this.repository.save(blob);
		}

		objectCaches.put(e.getId(), e);

		return e.getDocId();
	}
	
	@Override
	public RichTextVO updateContent(RichTextUpdateParam param) throws CustomException {
		RichText e = this.findEntityById(param.getDocId());
		
		e.setTextContent(param.getUpdateContent());
		objectCaches.put(e.getId(), e);
		
		return toVO(e);
	}

	@Override
	public Page<RichTextVO> queryPage(RichTextQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<RichText> example = buildSpec(param);

		Page<RichText> page = repository.findAll(example, request);
		List<RichTextVO> retList = new ArrayList<>();

		for(RichText e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<RichTextVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<RichText> buildSpec(RichTextQueryParam param) throws CustomException {
		return new Specification<RichText>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<RichText> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<RichText> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				
				builder.addIn("contentType", Arrays.asList(RichTextContentType.BLOCK_SUITE, RichTextContentType.HTML, RichTextContentType.MARKDOWN));
				builder.addEqual("ownerId", param.getOwnerId());
				builder.addEqual("ownerType", param.getOwnerType());

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private RichTextVO toVO(RichText e) throws CustomException {
		RichTextVO vo = new RichTextVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setOwnerId(e.getOwnerId());
		vo.setOwnerType(e.getOwnerType());
		vo.setByteContent(e.getByteContent());
		vo.setTextContent(e.getTextContent());
		vo.setContentType(e.getContentType());
		vo.setDocId(e.getDocId());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, RichText.class);
		super.softDelete(id);
	}
	

	private RichText findEntityById(String docId) {
		RichText ret = this.objectCaches.get(docId, RichText.class);
		if(null == ret){
			RichText rt = new RichText();
			rt.setIsDelete(false);
			rt.setDocId(docId);
			
			List<RichText> match = this.repository.findAll(Example.of(rt));
			
			if(null == match || match.size() == 0) {
				return null;
			}
			
			ret = match.get(0);
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}
}
