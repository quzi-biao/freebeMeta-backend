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

import com.freebe.code.business.meta.controller.param.ProjectMemberQueryParam;
import com.freebe.code.business.meta.controller.param.ProjectParam;
import com.freebe.code.business.meta.controller.param.ProjectQueryParam;
import com.freebe.code.business.meta.service.ProjectService;
import com.freebe.code.business.meta.vo.ProjectVO;
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
@RequestMapping("/project")
@CrossOrigin(origins = "*")
@Api(tags="项目接口")
public class ProjectController {
	@Autowired
	private ProjectService projectService;

	@ApiOperation(value = "创建或者更新项目")
	@PostMapping("createOrUpdate")
	public ResultBean<ProjectVO> createOrUpdate(@Valid @RequestBody ProjectParam param) throws CustomException {
		return ResultBean.ok(projectService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取项目")
	@GetMapping("get/{id}")
	public ResultBean<ProjectVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(projectService.findById(id));
	}

	@ApiOperation(value = "查询项目")
	@PostMapping("list")
	public ResultBean<Page<ProjectVO>> list(@Valid @RequestBody ProjectQueryParam param) throws CustomException {
		return ResultBean.ok(projectService.queryPage(param));
	}
	
	@ApiOperation(value = "查询项目")
	@PostMapping("listMember")
	public ResultBean<Page<ProjectVO>> listMine(@Valid @RequestBody ProjectMemberQueryParam param) throws CustomException {
		return ResultBean.ok(projectService.queryMemberPage(param));
	}
	
	@ApiOperation(value = "完成项目")
	@GetMapping("done/{id}")
	public ResultBean<ProjectVO> done(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(projectService.doneProject(id));
	}
	
	@ApiOperation(value = "开始项目")
	@GetMapping("start/{id}")
	public ResultBean<ProjectVO> start(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(projectService.startProject(id));
	}
	
	@ApiOperation(value = "项目开始结算")
	@GetMapping("startBill/{id}")
	public ResultBean<ProjectVO> startBill(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(projectService.startBill(id));
	}
	
	@ApiOperation(value = "项目结算完成")
	@GetMapping("billEnd/{id}")
	public ResultBean<ProjectVO> billEnd(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(projectService.billEnd(id));
	}
}
