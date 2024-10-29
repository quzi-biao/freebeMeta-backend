package com.freebe.code.business.meta.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freebe.code.business.meta.service.JobService;
import com.freebe.code.business.meta.vo.JobVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.freebe.code.business.meta.controller.param.JobParam;
import com.freebe.code.business.meta.controller.param.JobQueryParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zhengbiaoxie
 *
 */
@RestController
@RequestMapping("/job")
@CrossOrigin(origins = "*")
@Api(tags="岗位接口")
public class JobController {
	@Autowired
	private JobService jobService;

	@ApiOperation(value = "创建或者更新岗位")
	@PostMapping("createOrUpdate")
	public ResultBean<JobVO> createOrUpdate(@Valid @RequestBody JobParam param) throws CustomException {
		return ResultBean.ok(jobService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取岗位")
	@GetMapping("get/{id}")
	public ResultBean<JobVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(jobService.findById(id));
	}

	@ApiOperation(value = "查询岗位")
	@PostMapping("list")
	public ResultBean<Page<JobVO>> list(@Valid @RequestBody JobQueryParam param) throws CustomException {
		return ResultBean.ok(jobService.queryPage(param));
	}

	@ApiOperation(value = "删除岗位")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		jobService.softDelete(id);
		return ResultBean.ok();
	}

}
