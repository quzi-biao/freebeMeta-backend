package com.freebe.code.business.meta.service;

import org.springframework.data.domain.Page;

import com.freebe.code.business.base.service.BaseService;
import com.freebe.code.common.CustomException;
import com.freebe.code.business.meta.entity.Job;
import com.freebe.code.business.meta.vo.JobVO;
import com.freebe.code.business.meta.controller.param.JobParam;
import com.freebe.code.business.meta.controller.param.JobQueryParam;

/**
 *
 * @author zhengbiaoxie
 *
 */
public interface JobService extends BaseService<Job> {

	JobVO findById(Long id) throws CustomException;

	JobVO createOrUpdate(JobParam param) throws CustomException;

	Page<JobVO> queryPage(JobQueryParam param) throws CustomException;

}
