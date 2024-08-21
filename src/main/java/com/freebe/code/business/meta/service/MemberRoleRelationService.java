package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.meta.entity.MemberRoleRelation;
import com.freebe.code.business.meta.vo.MemberRoleRelationVO;
import com.freebe.code.business.meta.controller.param.MemberRoleRelationParam;
import com.freebe.code.business.meta.controller.param.MemberRoleRelationQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface MemberRoleRelationService extends BaseService<MemberRoleRelation> {

	MemberRoleRelationVO createOrUpdate(MemberRoleRelationParam param) throws CustomException;

	Page<MemberRoleRelationVO> queryPage(MemberRoleRelationQueryParam param) throws CustomException;

}
