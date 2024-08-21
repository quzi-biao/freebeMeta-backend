package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.List;

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
import com.freebe.code.business.meta.controller.param.MemberRoleRelationParam;
import com.freebe.code.business.meta.controller.param.MemberRoleRelationQueryParam;
import com.freebe.code.business.meta.entity.MemberRoleRelation;
import com.freebe.code.business.meta.repository.MemberRoleRelationRepository;
import com.freebe.code.business.meta.service.MemberRoleRelationService;
import com.freebe.code.business.meta.vo.MemberRoleRelationVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.util.PageUtils;
import com.freebe.code.util.QueryUtils.QueryBuilder;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class MemberRoleRelationServiceImpl extends BaseServiceImpl<MemberRoleRelation> implements MemberRoleRelationService {
	@Autowired
	private MemberRoleRelationRepository repository;
	
	
	
	public void removeRelation(Long memberId, Long roleId) throws CustomException {
		
	}


	@Override
	public MemberRoleRelationVO createOrUpdate(MemberRoleRelationParam param) throws CustomException {
		MemberRoleRelation e = this.getUpdateEntity(param, false);

		e.setRoleId(param.getRoleId());
		e.setMemberId(param.getMemberId());

		e = repository.save(e);

		MemberRoleRelationVO vo = toVO(e);

		return vo;
	}

	@Override
	public Page<MemberRoleRelationVO> queryPage(MemberRoleRelationQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<MemberRoleRelation> example = buildSpec(param);

		Page<MemberRoleRelation> page = repository.findAll(example, request);
		List<MemberRoleRelationVO> retList = new ArrayList<>();

		for(MemberRoleRelation e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<MemberRoleRelationVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<MemberRoleRelation> buildSpec(MemberRoleRelationQueryParam param) throws CustomException {
		return new Specification<MemberRoleRelation>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<MemberRoleRelation> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<MemberRoleRelation> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);

				builder.addBetween("createTime", param.getCreateStartTime(), param.getCreateEndTime());
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private MemberRoleRelationVO toVO(MemberRoleRelation e) throws CustomException {
		MemberRoleRelationVO vo = new MemberRoleRelationVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setRoleId(e.getRoleId());
		vo.setMemberId(e.getMemberId());

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		super.softDelete(id);
	}

}
