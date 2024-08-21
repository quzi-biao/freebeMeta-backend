package com.freebe.code.business.meta.controller;

import java.util.List;

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

import com.freebe.code.business.meta.controller.param.ProjectMemberBillParam;
import com.freebe.code.business.meta.controller.param.ProjectMemberQueryParam;
import com.freebe.code.business.meta.service.ProjectMemberService;
import com.freebe.code.business.meta.vo.ProjectMemberVO;
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
@RequestMapping("/projectmember")
@CrossOrigin(origins = "*")
@Api(tags="项目成员接口")
public class ProjectMemberController {
	@Autowired
	private ProjectMemberService projectmemberService;

//	@ApiOperation(value = "创建或者更新项目成员")
//	@PostMapping("createOrUpdate")
//	public ResultBean<ProjectMemberVO> createOrUpdate(@Valid @RequestBody ProjectMemberParam param) throws CustomException {
//		return ResultBean.ok(projectmemberService.createOrUpdate(param));
//	}

	@ApiOperation(value = "获取项目成员")
	@GetMapping("get/{projectId}")
	public ResultBean<List<ProjectMemberVO>> get(@PathVariable("projectId") Long projectId) throws CustomException {
		return ResultBean.ok(projectmemberService.getProjectMembers(projectId));
	}

	@ApiOperation(value = "查询项目成员")
	@PostMapping("list")
	public ResultBean<Page<ProjectMemberVO>> list(@Valid @RequestBody ProjectMemberQueryParam param) throws CustomException {
		return ResultBean.ok(projectmemberService.queryPage(param));
	}
	
	@ApiOperation(value = "工作完成")
	@GetMapping("done/{id}")
	public ResultBean<ProjectMemberVO> done(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(projectmemberService.doneWork(id));
	}
	
	@ApiOperation(value = "项目结算")
	@PostMapping("bill")
	public ResultBean<ProjectMemberVO> bill(@Valid @RequestBody ProjectMemberBillParam param) throws CustomException {
		return ResultBean.ok(projectmemberService.bill(param));
	}
	
	
//
//	@ApiOperation(value = "删除项目成员")
//	@DeleteMapping("{id}")
//	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
//		projectmemberService.softDelete(id);
//		return ResultBean.ok();
//	}

}
