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

import com.freebe.code.business.meta.service.FreeFileService;
import com.freebe.code.business.meta.vo.FreeFileVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.freebe.code.business.meta.controller.param.FreeFileParam;
import com.freebe.code.business.meta.controller.param.FreeFileQueryParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zhengbiaoxie
 *
 */
@RestController
@RequestMapping("/freefile")
@CrossOrigin(origins = "*")
@Api(tags="文件接口")
public class FreeFileController {
	@Autowired
	private FreeFileService freefileService;

	@ApiOperation(value = "创建或者更新文件")
	@PostMapping("createOrUpdate")
	public ResultBean<FreeFileVO> createOrUpdate(@Valid @RequestBody FreeFileParam param) throws CustomException {
		return ResultBean.ok(freefileService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取文件")
	@GetMapping("get/{id}")
	public ResultBean<FreeFileVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(freefileService.findById(id));
	}

	@ApiOperation(value = "查询文件")
	@PostMapping("list")
	public ResultBean<Page<FreeFileVO>> list(@Valid @RequestBody FreeFileQueryParam param) throws CustomException {
		return ResultBean.ok(freefileService.queryPage(param));
	}

	@ApiOperation(value = "删除文件")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		freefileService.softDelete(id);
		return ResultBean.ok();
	}

}
