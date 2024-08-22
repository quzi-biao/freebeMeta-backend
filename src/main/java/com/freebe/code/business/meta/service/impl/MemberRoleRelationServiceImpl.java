package com.freebe.code.business.meta.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.freebe.code.business.base.service.impl.BaseServiceImpl;
import com.freebe.code.business.meta.controller.param.MemberRoleRelationParam;
import com.freebe.code.business.meta.entity.MemberRoleRelation;
import com.freebe.code.business.meta.repository.MemberRoleRelationRepository;
import com.freebe.code.business.meta.service.MemberRoleRelationService;
import com.freebe.code.business.meta.vo.MemberRoleRelationVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
@Service
public class MemberRoleRelationServiceImpl extends BaseServiceImpl<MemberRoleRelation> implements MemberRoleRelationService {
	@Autowired
	private MemberRoleRelationRepository repository;


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
	public List<MemberRoleRelationVO> getList(Long roleId) throws CustomException {
		MemberRoleRelation e = new MemberRoleRelation();

		e.setRoleId(roleId);
		e.setIsDelete(false);
		
		List<MemberRoleRelation> ret = this.repository.findAll(Example.of(e));
		
		List<MemberRoleRelationVO> vos = new ArrayList<>();
		for(MemberRoleRelation mr : ret) {
			vos.add(toVO(mr));
		}
		
		return vos;

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


	@Override
	public MemberRoleRelationVO getRelation(Long roleId, Long memberId) throws CustomException {
		MemberRoleRelation e = new MemberRoleRelation();

		e.setRoleId(roleId);
		e.setMemberId(memberId);
		e.setIsDelete(false);
		
		List<MemberRoleRelation> ret = this.repository.findAll(Example.of(e));
		
		if(null == ret || ret.size() == 0) {
			return null;
		}
		return toVO(ret.get(0));
	}
	
	@Override
	public MemberRoleRelationVO removeRelation(Long roleId, Long memberId) throws CustomException {
		MemberRoleRelationVO vo = this.getRelation(roleId, memberId);
		if(null == vo) {
			return null;
		}
		this.softDelete(vo.getId());
		return null;
	}

}
