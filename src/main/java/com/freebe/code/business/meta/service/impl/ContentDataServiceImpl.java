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

import com.freebe.code.common.ObjectCaches;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.common.CustomException;
import com.freebe.code.util.PageUtils;
import com.freebe.code.util.QueryUtils.QueryBuilder;
import com.freebe.code.business.meta.entity.ContentData;
import com.freebe.code.business.meta.service.ContentDataService;
import com.freebe.code.business.meta.type.ContentType;
import com.freebe.code.business.meta.vo.ContentDataVO;
import com.freebe.code.business.meta.controller.param.ContentDataParam;
import com.freebe.code.business.meta.controller.param.ContentDataQueryParam;
import com.freebe.code.business.meta.repository.ContentDataRepository;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class ContentDataServiceImpl extends BaseServiceImpl<ContentData> implements ContentDataService {
	@Autowired
	private ContentDataRepository repository;

	@Autowired
	private ObjectCaches objectCaches;

	@Override
	public ContentDataVO findById(Long id) throws CustomException {
		ContentDataVO ret = this.objectCaches.get(id, ContentDataVO.class);
		if(null == ret){
			Optional<ContentData> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}
	
	@Override
	public String findByKey(String key) throws CustomException {
		ContentData ret = getEntity(key);
		return ret.getContent();
	}


	@Transactional
	@Override
	public String createOrUpdate(ContentDataParam param) throws CustomException {
		ContentData e = this.getUpdateEntity(param, false);

		e.setContent(param.getContent());
		e.setBinary(param.getBinary());
//		e.setCommpress(param.getCommpress());
//		e.setCrypto(param.getCrypto());
		e.setContentType(param.getContentType());
		e.setOwnerId(getCurrentUser().getId());
		e.setContentKey(e.getCode());

		e = repository.save(e);

		//ContentDataVO vo = toVO(e);
		objectCaches.put(e.getId(), e);

		return e.getCode();
	}
	
	@Transactional
	@Override
	public String updateContent(String key, String content, Integer type) throws CustomException {
		if(null == key) {
			ContentDataParam dataParam = new ContentDataParam();
			if(null == type) {
				type = ContentType.NORMAL;
			}
			dataParam.setContent(content);
			dataParam.setContentType(type);
			return this.createOrUpdate(dataParam);
		}
		ContentData data = this.getEntity(key);
		if(null == data) {
			throw new CustomException("内容数据不存在");
		}
		
		data.setContent(content);
		this.repository.save(data);
		
		return data.getCode();
	}

	@Override
	public Page<ContentDataVO> queryPage(ContentDataQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<ContentData> example = buildSpec(param);

		Page<ContentData> page = repository.findAll(example, request);
		List<ContentDataVO> retList = new ArrayList<>();

		for(ContentData e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<ContentDataVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<ContentData> buildSpec(ContentDataQueryParam param) throws CustomException {
		return new Specification<ContentData>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ContentData> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<ContentData> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);

				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private ContentDataVO toVO(ContentData e) throws CustomException {
		ContentDataVO vo = new ContentDataVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setContent(e.getContent());
		vo.setBinary(e.getBinary());
		vo.setCommpress(e.getCommpress());
		vo.setCrypto(e.getCrypto());
		vo.setContentType(e.getContentType());
		vo.setOwnerId(e.getOwnerId());
		vo.setContentKey(e.getContentKey());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, ContentDataVO.class);
		super.softDelete(id);
	}


	private ContentData getEntity(String key) {
		ContentData ret = this.objectCaches.get(key, ContentData.class);
		if(null == ret){
			ContentData e = new ContentData();
			e.setContentKey(key);
			Optional<ContentData> op = this.repository.findOne(Example.of(e));
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}
}
