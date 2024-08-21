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

import com.freebe.code.business.meta.service.FilePermissionService;
import com.freebe.code.business.meta.vo.FilePermissionVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.freebe.code.business.meta.controller.param.FilePermissionParam;
import com.freebe.code.business.meta.controller.param.FilePermissionQueryParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zhengbiaoxie
 *
 */
@RestController
@RequestMapping("/filepermission")
@CrossOrigin(origins = "*")
@Api(tags="文件权限接口")
public class FilePermissionController {
	@Autowired
	private FilePermissionService filepermissionService;

	@ApiOperation(value = "创建或者更新文件权限")
	@PostMapping("createOrUpdate")
	public ResultBean<FilePermissionVO> createOrUpdate(@Valid @RequestBody FilePermissionParam param) throws CustomException {
		return ResultBean.ok(filepermissionService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取文件权限")
	@GetMapping("get/{id}")
	public ResultBean<FilePermissionVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(filepermissionService.findById(id));
	}

	@ApiOperation(value = "查询文件权限")
	@PostMapping("list")
	public ResultBean<Page<FilePermissionVO>> list(@Valid @RequestBody FilePermissionQueryParam param) throws CustomException {
		return ResultBean.ok(filepermissionService.queryPage(param));
	}

	@ApiOperation(value = "删除文件权限")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		filepermissionService.softDelete(id);
		return ResultBean.ok();
	}

}
