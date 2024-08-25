package com.freebe.code.business.meta.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freebe.code.business.meta.controller.param.TaskDoneParam;
import com.freebe.code.business.meta.controller.param.TaskTakerParam;
import com.freebe.code.business.meta.controller.param.TaskTakerQueryParam;
import com.freebe.code.business.meta.service.TaskTakerService;
import com.freebe.code.business.meta.vo.TaskTakerVO;
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
@RequestMapping("/tasktaker")
@CrossOrigin(origins = "*")
@Api(tags="任务认领接口")
public class TaskTakerController {
	@Autowired
	private TaskTakerService tasktakerService;

	@ApiOperation(value = "创建或者更新任务认领")
	@PostMapping("createOrUpdate")
	public ResultBean<TaskTakerVO> createOrUpdate(@Valid @RequestBody TaskTakerParam param) throws CustomException {
		return ResultBean.ok(tasktakerService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取任务认领")
	@GetMapping("get/{id}")
	public ResultBean<TaskTakerVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(tasktakerService.findById(id));
	}

	@ApiOperation(value = "查询任务认领")
	@PostMapping("list")
	public ResultBean<Page<TaskTakerVO>> list(@Valid @RequestBody TaskTakerQueryParam param) throws CustomException {
		return ResultBean.ok(tasktakerService.queryPage(param));
	}
	
	@ApiOperation(value = "完成任务")
	@PostMapping("done")
	public ResultBean<TaskTakerVO> submitDone(@Valid @RequestBody TaskDoneParam param) throws CustomException {
		return ResultBean.ok(tasktakerService.doneTask(param));
	}
}
