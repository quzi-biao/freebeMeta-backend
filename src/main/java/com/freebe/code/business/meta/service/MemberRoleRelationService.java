package com.freebe.code.business.meta.service;

import java.util.List;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.MemberRoleRelationParam;
import com.freebe.code.business.meta.entity.MemberRoleRelation;
import com.freebe.code.business.meta.vo.MemberRoleRelationVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface MemberRoleRelationService extends BaseService<MemberRoleRelation> {

	MemberRoleRelationVO createOrUpdate(MemberRoleRelationParam param) throws CustomException;


	List<MemberRoleRelationVO> getList(Long roleId) throws CustomException;
	
	
	/**
	 * 获取关系
	 * @param roleId
	 * @param memberId
	 * @return
	 * @throws CustomException
	 */
	MemberRoleRelationVO getRelation(Long roleId, Long memberId) throws CustomException;

	/**
	 * 删除关系
	 * @param roleId
	 * @param memberId
	 * @return
	 * @throws CustomException
	 */
	MemberRoleRelationVO removeRelation(Long roleId, Long memberId) throws CustomException;
}
