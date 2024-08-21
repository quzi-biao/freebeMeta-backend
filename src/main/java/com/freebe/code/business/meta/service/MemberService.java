package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.MemberParam;
import com.freebe.code.business.meta.controller.param.MemberQueryParam;
import com.freebe.code.business.meta.entity.Member;
import com.freebe.code.business.meta.vo.MemberVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface MemberService extends BaseService<Member> {

	MemberVO findById(Long id) throws CustomException;

	MemberVO createOrUpdate(MemberParam param) throws CustomException;

	Page<MemberVO> queryPage(MemberQueryParam param) throws CustomException;

	MemberVO findByUserId(Long userId) throws CustomException;

	/**
	 * 根据用户ID获取成员ID
	 * @param userId
	 * @return
	 * @throws CustomException
	 */
	Long getMemberIdByUserId(Long userId) throws CustomException;
	
	/**
	 * 为成员添加某个角色
	 * @param roleId
	 * @return
	 * @throws CustomException
	 */
	MemberVO addRole(Long memberId, Long roleId) throws CustomException;

	/**
	 * 统计社区当前的成员数
	 * @return
	 * @throws CustomException
	 */
	Long countMembers() throws CustomException;

}
