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

import com.freebe.code.business.meta.service.ProjectRecordService;
import com.freebe.code.business.meta.vo.ProjectRecordVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.freebe.code.business.meta.controller.param.ProjectRecordParam;
import com.freebe.code.business.meta.controller.param.ProjectRecordQueryParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zhengbiaoxie
 *
 */
@RestController
@RequestMapping("/projectrecord")
@CrossOrigin(origins = "*")
@Api(tags="项目记录接口")
public class ProjectRecordController {
	@Autowired
	private ProjectRecordService projectrecordService;

	@ApiOperation(value = "创建或者更新项目记录")
	@PostMapping("createOrUpdate")
	public ResultBean<ProjectRecordVO> createOrUpdate(@Valid @RequestBody ProjectRecordParam param) throws CustomException {
		return ResultBean.ok(projectrecordService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取项目记录")
	@GetMapping("get/{id}")
	public ResultBean<ProjectRecordVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(projectrecordService.findById(id));
	}

	@ApiOperation(value = "查询项目记录")
	@PostMapping("list")
	public ResultBean<Page<ProjectRecordVO>> list(@Valid @RequestBody ProjectRecordQueryParam param) throws CustomException {
		return ResultBean.ok(projectrecordService.queryPage(param));
	}

	@ApiOperation(value = "删除项目记录")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		projectrecordService.softDelete(id);
		return ResultBean.ok();
	}
}
