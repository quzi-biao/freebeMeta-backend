package com.freebe.code.business.badge.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.freebe.code.business.badge.controller.param.BadgeParam;
import com.freebe.code.business.badge.controller.param.BadgeQueryParam;
import com.freebe.code.business.badge.entity.Badge;
import com.freebe.code.business.badge.repository.BadgeRepository;
import com.freebe.code.business.badge.service.BadgeHoldService;
import com.freebe.code.business.badge.service.BadgeService;
import com.freebe.code.business.badge.vo.BadgeVO;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.vo.MemberVO;
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
public class BadgeServiceImpl extends BaseServiceImpl<Badge> implements BadgeService {
	@Autowired
	private BadgeRepository repository;

	@Autowired
	private ObjectCaches objectCaches;

	@Override
	public BadgeVO findById(Long id) throws CustomException {
		if(null == id) {
			return null;
		}
		BadgeVO ret = this.objectCaches.get(id, BadgeVO.class);
		if(null == ret){
			Optional<Badge> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}
	
	@Override
	public BadgeVO createOrUpdate(BadgeParam param) throws CustomException {
		checkParam(param);
		
		// 当前用户的执行权限
		checkPermission();
		
		Badge e = this.getUpdateEntity(param);

		MemberVO memberVO = this.getCurrentMemeber();
		
		e.setCreateMemeber(memberVO.getId());
		e.setIcon(param.getIcon());
		e.setPicture(param.getPicture());
		e.setDescription(param.getDescription());
		e.setGetCondition(param.getGetCondition());
		e.setAutoGetIdentity(param.getAutoGetIdentity());
		e.setContractAddress(param.getContractAddress());
		if(e.getId() == null) {
			e.setHolderNumber(0);
		}

		e = repository.save(e);

		BadgeVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}

	@Override
	public void incHolderNumber(Long badgeId) throws CustomException {
		Optional<Badge> op = this.repository.findById(badgeId);
		if(!op.isPresent()){
			return;
		}
		
		Badge badge = op.get();
		badge.setHolderNumber(badge.getHolderNumber() + 1);
		badge = this.repository.save(badge);
		objectCaches.put(badgeId, toVO(badge));
	}

	@Override
	public void decHolderNumber(Long badgeId) throws CustomException {
		Optional<Badge> op = this.repository.findById(badgeId);
		if(!op.isPresent()){
			return;
		}
		
		Badge badge = op.get();
		badge.setHolderNumber(badge.getHolderNumber() - 1);
		if(badge.getHolderNumber() < 0) {
			// 应该要重新统计
			badge.setHolderNumber(0);
		}
		badge = this.repository.save(badge);
		objectCaches.put(badgeId, toVO(badge));
	}


	@Override
	public Page<BadgeVO> queryPage(BadgeQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<Badge> example = buildSpec(param);

		Page<Badge> page = repository.findAll(example, request);
		List<BadgeVO> retList = new ArrayList<>();

		for(Badge e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<BadgeVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<Badge> buildSpec(BadgeQueryParam param) throws CustomException {
		return new Specification<Badge>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Badge> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<Badge> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				builder.addLike("name", param.getName());
				
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private BadgeVO toVO(Badge e) throws CustomException {
		BadgeVO vo = new BadgeVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setCreateMemeber(e.getCreateMemeber());
		vo.setIcon(e.getIcon());
		vo.setPicture(e.getPicture());
		vo.setDescription(e.getDescription());
		vo.setGetCondition(e.getGetCondition());
		vo.setAutoGetIdentity(e.getAutoGetIdentity());
		vo.setContractAddress(e.getContractAddress());
		vo.setHolderNumber(e.getHolderNumber());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, BadgeVO.class);
		super.softDelete(id);
	}
	
	
	private void checkPermission() throws CustomException {
		this.checkPermssion(BadgeHoldService.BADGE_MANAGER);
	}
	
	/**
	 * 参数检查
	 * @param param
	 * @throws CustomException
	 */
	private void checkParam(BadgeParam param) throws CustomException {
		if(StringUtils.isEmpty(param.getName())) {
			throw new CustomException("请设置徽章名称");
		}
		
		if(StringUtils.isEmpty(param.getDescription())) {
			throw new CustomException("请对徽章进行描述");
		}
		
		if(StringUtils.isEmpty(param.getGetCondition())) {
			throw new CustomException("请设置徽章获取条件");
		}
	}
}
