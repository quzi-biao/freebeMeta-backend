package com.freebe.code.business.meta.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.ProjectMemberBillParam;
import com.freebe.code.business.meta.controller.param.ProjectMemberParam;
import com.freebe.code.business.meta.controller.param.ProjectMemberQueryParam;
import com.freebe.code.business.meta.entity.ProjectMember;
import com.freebe.code.business.meta.vo.ProjectMemberVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface ProjectMemberService extends BaseService<ProjectMember> {

	ProjectMemberVO findById(Long id) throws CustomException;

	ProjectMemberVO createOrUpdate(ProjectMemberParam param) throws CustomException;
	
	Page<ProjectMemberVO> queryPage(ProjectMemberQueryParam param) throws CustomException;
	
	/**
	 * 获取一个项目的成员列表
	 * @param projectId
	 * @return
	 * @throws CustomException 
	 */
	List<ProjectMemberVO> getProjectMembers(Long projectId) throws CustomException;
	
	/**
	 * 统计成员参与的项目数量
	 * @param projectId
	 * @param memberId
	 * @param evaluate
	 * @return
	 * @throws CustomException
	 */
	Integer countProject(Long memberId) throws CustomException;
	
	
	/**
	 * 项目结算，有成员处于结算中时，项目进入结算中的状态，所有成员都结算完成后，项目才结算完成
	 * 结算后将自动发起一笔系统到成员的转账，
	 * @param projectId
	 * @param memberId
	 * @param amount
	 * @return
	 * @throws CustomException
	 */
	ProjectMemberVO bill(ProjectMemberBillParam billParam) throws CustomException;
	
	/**
	 * 接受项目邀请
	 * @param projectId
	 * @param memberId
	 * @return
	 * @throws CustomException
	 */
	ProjectMemberVO accept(Long projectId) throws CustomException;
	
	/**
	 * 拒绝项目邀请
	 * @param projectId
	 * @param memberId
	 * @return
	 * @throws CustomException
	 */
	ProjectMemberVO reject(Long projectId) throws CustomException;
	
	/**
	 * 退出，reason 为退出原因
	 * @param projectId
	 * @param memberId
	 * @return
	 * @throws CustomException
	 */
	ProjectMemberVO dismission(Long projectId, String reason) throws CustomException;

	/**
	 * 查询
	 * @param projectId
	 * @param memberId
	 * @return
	 * @throws CustomException 
	 */
	ProjectMemberVO findProjectMmeber(Long projectId, Long memberId) throws CustomException;

	/**
	 * 更新项目成员
	 * @param projectId
	 * @param members
	 * @throws CustomException
	 */
	void updateProjectMembers(Long projectId, List<ProjectMemberParam> members) throws CustomException;
	
	/**
	 * 完成工作，所有成员都完成工作，项目才允许标记为完成
	 * @param projectId
	 * @return
	 * @throws CustomException
	 */
	ProjectMemberVO doneWork(Long projectId) throws CustomException;
	
	/**
	 * 结算成功
	 * @param transactionId
	 * @param transactionState 
	 * @return
	 * @throws CustomException
	 */
	ProjectMemberVO billEnd(Long transactionId, Integer transactionState) throws CustomException;

	/**
	 * 
	 * @param projectId
	 * @param memberId
	 * @return
	 */
	ProjectMember findOne(Long projectId, Long memberId);
	
}
