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
import com.freebe.code.business.meta.controller.param.LikeParam;
import com.freebe.code.business.meta.controller.param.LikeQueryParam;
import com.freebe.code.business.meta.entity.Like;
import com.freebe.code.business.meta.repository.LikeRepository;
import com.freebe.code.business.meta.service.LikeService;
import com.freebe.code.business.meta.type.InteractionType;
import com.freebe.code.business.meta.vo.LikeVO;
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
public class LikeServiceImpl extends BaseServiceImpl<Like> implements LikeService {
	@Autowired
	private LikeRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private InteractionCountUtils countUtils;

	@Override
	public LikeVO findById(Long id) throws CustomException {
		Like ret = getEntity(id);
		return toVO(ret);
	}

	@Transactional
	@Override
	public LikeVO createOrUpdate(LikeParam param) throws CustomException {
		if(null == param.getTypeId() || null == param.getEntityId()) {
			throw new CustomException("参数错误");
		}
		Like e = this.getUpdateEntity(param, false);

		e.setUserId(getCurrentUser().getId());
		e.setTypeId(param.getTypeId());
		e.setEntityId(param.getEntityId());

		e = repository.save(e);

		LikeVO vo = toVO(e);
		objectCaches.put(e.getId(), e);
		countUtils.inc(param.getEntityId(), param.getTypeId(), InteractionType.LIKE);

		return vo;
	}

	@Override
	public Page<LikeVO> queryPage(LikeQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<Like> example = buildSpec(param);

		Page<Like> page = repository.findAll(example, request);
		List<LikeVO> retList = new ArrayList<>();

		for(Like e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<LikeVO>(retList, page.getPageable(), page.getTotalElements());
	}
	
	@Transactional
	@Override
	public LikeVO cancel(LikeParam param) throws CustomException {
		if(null == param.getTypeId() || null == param.getEntityId()) {
			throw new CustomException("参数错误");
		}
		
		Like c = new Like();
		c.setTypeId(param.getTypeId());
		c.setEntityId(param.getEntityId());
		c.setUserId(getCurrentUser().getId());
		c.setIsDelete(false);
		
		List<Like> collects = this.repository.findAll(Example.of(c));
		if(null == collects || collects.size() == 0) {
			return null;
		}
		
		this.repository.deleteAll(collects);
		countUtils.dec(param.getEntityId(), param.getTypeId(), InteractionType.LIKE);
		
		return null;
	}

	@Override
	public boolean isLike(long typeId, Long entityId) {
		if(getCurrentUser() == null) {
			return false;
		}
		Like c = new Like();
		c.setTypeId(typeId);
		c.setEntityId(entityId);
		c.setUserId(getCurrentUser().getId());
		c.setIsDelete(false);
		
		return this.repository.exists(Example.of(c));
	}

	private Specification<Like> buildSpec(LikeQueryParam param) throws CustomException {
		return new Specification<Like>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Like> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<Like> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				builder.addEqual("typeId", param.getTypeId());
				builder.addEqual("userId", param.getUserId());

				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private LikeVO toVO(Like e) throws CustomException {
		LikeVO vo = new LikeVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setUserId(e.getUserId());
		vo.setTypeId(e.getTypeId());
		vo.setEntityId(e.getEntityId());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, LikeVO.class);
		super.softDelete(id);
	}

	private Like getEntity(Long id) {
		Like ret = this.objectCaches.get(id, Like.class);
		if(null == ret){
			Optional<Like> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = op.get();
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}

}
