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

import com.freebe.code.business.meta.service.MindMapService;
import com.freebe.code.business.meta.vo.MindMapVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.freebe.code.business.meta.controller.param.MindMapParam;
import com.freebe.code.business.meta.controller.param.MindMapQueryParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zhengbiaoxie
 *
 */
@RestController
@RequestMapping("/mindmap")
@CrossOrigin(origins = "*")
@Api(tags="思维导图接口")
public class MindMapController {
	@Autowired
	private MindMapService mindmapService;

	@ApiOperation(value = "创建或者更新思维导图")
	@PostMapping("createOrUpdate")
	public ResultBean<MindMapVO> createOrUpdate(@Valid @RequestBody MindMapParam param) throws CustomException {
		return ResultBean.ok(mindmapService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取思维导图")
	@GetMapping("get/{id}")
	public ResultBean<MindMapVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(mindmapService.findById(id));
	}

	@ApiOperation(value = "查询思维导图")
	@PostMapping("list")
	public ResultBean<Page<MindMapVO>> list(@Valid @RequestBody MindMapQueryParam param) throws CustomException {
		return ResultBean.ok(mindmapService.queryPage(param));
	}

	@ApiOperation(value = "删除思维导图")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		mindmapService.softDelete(id);
		return ResultBean.ok();
	}

}
