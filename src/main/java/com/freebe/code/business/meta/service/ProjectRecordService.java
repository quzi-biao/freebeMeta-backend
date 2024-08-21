package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.ProjectRecordParam;
import com.freebe.code.business.meta.controller.param.ProjectRecordQueryParam;
import com.freebe.code.business.meta.entity.ProjectRecord;
import com.freebe.code.business.meta.vo.ProjectRecordVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface ProjectRecordService extends BaseService<ProjectRecord> {

	ProjectRecordVO findById(Long id) throws CustomException;

	ProjectRecordVO createOrUpdate(ProjectRecordParam param) throws CustomException;

	Page<ProjectRecordVO> queryPage(ProjectRecordQueryParam param) throws CustomException;

	/**
	 * 内部创建
	 * @param param
	 * @return 
	 * @throws CustomException
	 */
	ProjectRecord innerCreateOrUpdate(ProjectRecordParam param) throws CustomException;
	
	/**
	 * 为项目添加记录
	 * @param projectId
	 * @param content
	 * @return
	 * @throws CustomException
	 */
	ProjectRecord addRecord(Long projectId, String content) throws CustomException;

}
