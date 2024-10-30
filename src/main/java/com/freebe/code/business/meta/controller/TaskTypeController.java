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

import com.freebe.code.business.meta.service.TaskTypeService;
import com.freebe.code.business.meta.vo.TaskTypeVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.freebe.code.business.meta.controller.param.TaskTypeParam;
import com.freebe.code.business.meta.controller.param.TaskTypeQueryParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zhengbiaoxie
 *
 */
@RestController
@RequestMapping("/tasktype")
@CrossOrigin(origins = "*")
@Api(tags="任务类型接口")
public class TaskTypeController {
	@Autowired
	private TaskTypeService tasktypeService;

	@ApiOperation(value = "创建或者更新任务类型")
	@PostMapping("createOrUpdate")
	public ResultBean<TaskTypeVO> createOrUpdate(@Valid @RequestBody TaskTypeParam param) throws CustomException {
		return ResultBean.ok(tasktypeService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取任务类型")
	@GetMapping("get/{id}")
	public ResultBean<TaskTypeVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(tasktypeService.findById(id));
	}

	@ApiOperation(value = "查询任务类型")
	@PostMapping("list")
	public ResultBean<Page<TaskTypeVO>> list(@Valid @RequestBody TaskTypeQueryParam param) throws CustomException {
		return ResultBean.ok(tasktypeService.queryPage(param));
	}

	@ApiOperation(value = "删除任务类型")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		tasktypeService.softDelete(id);
		return ResultBean.ok();
	}

}
