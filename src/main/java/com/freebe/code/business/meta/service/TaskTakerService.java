package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.business.meta.controller.param.TaskDoneParam;
import com.freebe.code.business.meta.controller.param.TaskTakerParam;
import com.freebe.code.business.meta.controller.param.TaskTakerQueryParam;
import com.freebe.code.business.meta.entity.TaskTaker;
import com.freebe.code.business.meta.vo.TaskTakerVO;
import com.freebe.code.common.CustomException;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface TaskTakerService extends BaseService<TaskTaker> {

	TaskTakerVO findById(Long id) throws CustomException;

	TaskTakerVO createOrUpdate(TaskTakerParam param) throws CustomException;

	Page<TaskTakerVO> queryPage(TaskTakerQueryParam param) throws CustomException;

	/**
	 * 完成任务
	 * @param param
	 * @return
	 * @throws CustomException
	 */
	TaskTakerVO doneTask(TaskDoneParam param) throws CustomException;
}
