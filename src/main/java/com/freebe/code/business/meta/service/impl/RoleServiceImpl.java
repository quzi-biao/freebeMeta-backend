package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
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

import com.freebe.code.business.base.service.UserService;
import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.base.vo.UserVO;
import com.freebe.code.business.meta.controller.param.RoleParam;
import com.freebe.code.business.meta.controller.param.RoleQueryParam;
import com.freebe.code.business.meta.entity.Role;
import com.freebe.code.business.meta.repository.RoleRepository;
import com.freebe.code.business.meta.service.MemberRoleRelationService;
import com.freebe.code.business.meta.service.MemberService;
import com.freebe.code.business.meta.service.RoleService;
import com.freebe.code.business.meta.service.TransactionService;
import com.freebe.code.business.meta.vo.MemberRoleRelationVO;
import com.freebe.code.business.meta.vo.RoleVO;
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
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
	@Autowired
	private RoleRepository repository;

	@Autowired
	private ObjectCaches objectCaches;
	
	@Autowired
	private MemberRoleRelationService relationService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private UserService userService;
	
	@PostConstruct
	public void init() throws CustomException {
		if(this.repository.count() == 0) {
			// 创建默认角色
			RoleParam role = new RoleParam();
			role.setName("财务官");
			role.setRoleCode(TransactionService.ROLE_CF_CODE);
			this.createOrUpdate(role);
		}
	}

	@Override
	public RoleVO findById(Long id) throws CustomException {
		RoleVO ret = this.objectCaches.get(id, RoleVO.class);
		if(null == ret){
			Optional<Role> op = this.repository.findById(id);
			if(!op.isPresent()){
				return null;
			}
			ret = toVO(op.get());
		}
		objectCaches.put(ret.getId(), ret);
		return ret;
	}
	
	@Override
	public List<RoleVO> findByIds(List<Long> ids) throws CustomException {
		if(null == ids) {
			return null;
		}
		List<RoleVO> ret = new ArrayList<>();
		for(Long id : ids) {
			ret.add(this.findById(id));
		}
		return ret;
	}

	@Override
	public RoleVO createOrUpdate(RoleParam param) throws CustomException {
		if(this.getCurrentUser().getId() != 1) {
			throw new CustomException("您无权执行此操作");
		}
		
		Role e = this.getUpdateEntity(param);

		e.setContractAddress(param.getContractAddress());
		e.setIcon(param.getIcon());
		e.setDescription(param.getDescription());
		e.setReward(param.getReward());
		e.setRoleCode(param.getRoleCode());
		e.setPicture(param.getPicture());
		e.setNumber(param.getNumber());

		e = repository.save(e);

		RoleVO vo = toVO(e);
		objectCaches.put(vo.getId(), vo);

		return vo;
	}

	@Override
	public Page<RoleVO> queryPage(RoleQueryParam param) throws CustomException {
		param.setOrder("id");
		PageRequest request = PageUtils.toPageRequest(param);

		Specification<Role> example = buildSpec(param);

		Page<Role> page = repository.findAll(example, request);
		List<RoleVO> retList = new ArrayList<>();

		for(Role e:  page.getContent()) {
			retList.add(toVO(e));
		}
		return new PageImpl<RoleVO>(retList, page.getPageable(), page.getTotalElements());
	}

	private Specification<Role> buildSpec(RoleQueryParam param) throws CustomException {
		return new Specification<Role>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				QueryBuilder<Role> builder = new QueryBuilder<>(root, criteriaBuilder);
				builder.addEqual("isDelete", false);
				return query.where(builder.getPredicate()).getRestriction();
			}
		};
	}

	private RoleVO toVO(Role e) throws CustomException {
		RoleVO vo = new RoleVO();
		vo.setId(e.getId());
		vo.setName(e.getName());
		vo.setCode(e.getCode());
		vo.setCreateTime(e.getCreateTime());

		vo.setContractAddress(e.getContractAddress());
		vo.setIcon(e.getIcon());
		vo.setDescription(e.getDescription());
		vo.setReward(e.getReward());
		vo.setRoleCode(e.getRoleCode());
		vo.setPicture(e.getPicture());
		vo.setNumber(e.getNumber());
		
		List<MemberRoleRelationVO> relations = this.relationService.getList(e.getId());
		if(null != relations && relations.size() > 0) {
			List<UserVO> members = new ArrayList<>();
			for(MemberRoleRelationVO mr : relations) {
				members.add(this.userService.getUser(this.memberService.getUserIdByMemberId(mr.getMemberId())));
			}
			vo.setRoleKeeper(members);
		}

		return vo;
	}

	@Override
	public void softDelete(Long id) throws CustomException {
		objectCaches.delete(id, RoleVO.class);
		super.softDelete(id);
	}

}
