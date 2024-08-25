package com.freebe.code.business.meta.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freebe.code.business.meta.controller.param.TaskAuditParam;
import com.freebe.code.business.meta.controller.param.TaskParam;
import com.freebe.code.business.meta.controller.param.TaskQueryParam;
import com.freebe.code.business.meta.service.TaskService;
import com.freebe.code.business.meta.vo.TaskVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zhengbiaoxie
 *
 */
@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "*")
@Api(tags="项目任务接口")
public class TaskController {
	@Autowired
	private TaskService taskService;

	@ApiOperation(value = "创建或者更新项目任务")
	@PostMapping("createOrUpdate")
	public ResultBean<TaskVO> createOrUpdate(@Valid @RequestBody TaskParam param) throws CustomException {
		return ResultBean.ok(taskService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取项目任务")
	@GetMapping("get/{id}")
	public ResultBean<TaskVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(taskService.findById(id));
	}

	@ApiOperation(value = "查询项目任务")
	@PostMapping("list")
	public ResultBean<Page<TaskVO>> list(@Valid @RequestBody TaskQueryParam param) throws CustomException {
		return ResultBean.ok(taskService.queryPage(param));
	}
	
	@ApiOperation(value = "取消目任务")
	@GetMapping("cancel/{id}")
	public ResultBean<TaskVO> cancel(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(taskService.cancelTask(id));
	}

	@ApiOperation(value = "任务审核")
	@PostMapping("audit")
	public ResultBean<TaskVO> auditTask(@Valid @RequestBody TaskAuditParam param) throws CustomException {
		return ResultBean.ok(taskService.auditTask(param));
	}

	@ApiOperation(value = "删除项目任务")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		taskService.softDelete(id);
		return ResultBean.ok();
	}

}
