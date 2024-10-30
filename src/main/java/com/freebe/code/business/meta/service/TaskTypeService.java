package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.meta.entity.TaskType;
import com.freebe.code.business.meta.vo.TaskTypeVO;
import com.freebe.code.business.meta.controller.param.TaskTypeParam;
import com.freebe.code.business.meta.controller.param.TaskTypeQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface TaskTypeService extends BaseService<TaskType> {

	TaskTypeVO findById(Long id) throws CustomException;

	TaskTypeVO createOrUpdate(TaskTypeParam param) throws CustomException;

	Page<TaskTypeVO> queryPage(TaskTypeQueryParam param) throws CustomException;

}
