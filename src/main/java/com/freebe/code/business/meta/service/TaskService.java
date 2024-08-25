package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.TaskAuditParam;
import com.freebe.code.business.meta.controller.param.TaskParam;
import com.freebe.code.business.meta.controller.param.TaskQueryParam;
import com.freebe.code.business.meta.entity.Task;
import com.freebe.code.business.meta.vo.TaskVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface TaskService extends BaseService<Task> {

	TaskVO findById(Long id) throws CustomException;

	TaskVO createOrUpdate(TaskParam param) throws CustomException;

	Page<TaskVO> queryPage(TaskQueryParam param) throws CustomException;

	/**
	 * 更新任务的认领信息
	 * @param takeId
	 * @throws CustomException 
	 */
	void updateTake(Long taskId, Long takeId) throws CustomException;

	/**
	 * 取消任务
	 * @param taskId
	 * @return
	 * @throws CustomException
	 */
	TaskVO cancelTask(Long taskId) throws CustomException;
	
	/**
	 * 取消任务
	 * @param taskId
	 * @return
	 * @throws CustomException
	 */
	TaskVO auditTask(TaskAuditParam param) throws CustomException;

	/**
	 * 任务完成
	 * @param id
	 * @throws CustomException 
	 */
	void doneTask(Long id) throws CustomException;

}
