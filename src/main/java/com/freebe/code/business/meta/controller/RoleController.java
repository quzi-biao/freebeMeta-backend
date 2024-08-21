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

import com.freebe.code.business.meta.service.RoleService;
import com.freebe.code.business.meta.vo.RoleVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.freebe.code.business.meta.controller.param.RoleParam;
import com.freebe.code.business.meta.controller.param.RoleQueryParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zhengbiaoxie
 *
 */
@RestController
@RequestMapping("/role")
@CrossOrigin(origins = "*")
@Api(tags="角色接口")
public class RoleController {
	@Autowired
	private RoleService roleService;

	@ApiOperation(value = "创建或者更新角色")
	@PostMapping("createOrUpdate")
	public ResultBean<RoleVO> createOrUpdate(@Valid @RequestBody RoleParam param) throws CustomException {
		return ResultBean.ok(roleService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取角色")
	@GetMapping("get/{id}")
	public ResultBean<RoleVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(roleService.findById(id));
	}

	@ApiOperation(value = "查询角色")
	@PostMapping("list")
	public ResultBean<Page<RoleVO>> list(@Valid @RequestBody RoleQueryParam param) throws CustomException {
		return ResultBean.ok(roleService.queryPage(param));
	}

	@ApiOperation(value = "删除角色")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		roleService.softDelete(id);
		return ResultBean.ok();
	}

}
