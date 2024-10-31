package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.ProjectMemberQueryParam;
import com.freebe.code.business.meta.controller.param.ProjectParam;
import com.freebe.code.business.meta.controller.param.ProjectQueryParam;
import com.freebe.code.business.meta.entity.Project;
import com.freebe.code.business.meta.vo.ProjectVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface ProjectService extends BaseService<Project> {

	ProjectVO findById(Long id) throws CustomException;

	ProjectVO createOrUpdate(ProjectParam param) throws CustomException;

	Page<ProjectVO> queryPage(ProjectQueryParam param) throws CustomException;
	
	
	/**
	 * 当项目成员都接受后，项目启动
	 * @param projectId
	 * @return
	 */
	ProjectVO startProject(Long projectId) throws CustomException;
	
	/**
	 * 当所有的项目成员都完成工作后，项目完成
	 * @param projectId
	 * @return
	 */
	ProjectVO doneProject(Long projectId) throws CustomException;

	/**
	 * 项目开始结算
	 * @param projectId
	 * @return
	 */
	ProjectVO startBill(Long projectId) throws CustomException;
	
	/**
	 * 项目结算完成，即项目结束
	 * @param projectId
	 * @return
	 */
	ProjectVO billEnd(Long projectId) throws CustomException;

	/**
	 * 查询成员的项目列表
	 * @return
	 * @throws CustomException
	 */
	Page<ProjectVO> queryMemberPage(ProjectMemberQueryParam param) throws CustomException;

	/**
	 * 用用户接受了项目
	 * @param projectId
	 * @throws CustomException 
	 */
	void memberAccept(Long projectId) throws CustomException;
	
	/**
	 * 获取项目状态
	 * @param projectId
	 * @return
	 * @throws CustomException
	 */
	Integer getProjectState(Long projectId) throws CustomException;

	/**
	 * 获取项目所有者
	 * @param projectId
	 * @return
	 */
	Long getOwnerId(Long projectId);
}
