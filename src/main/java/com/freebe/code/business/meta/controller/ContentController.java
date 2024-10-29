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

import com.freebe.code.business.meta.controller.param.ContentParam;
import com.freebe.code.business.meta.controller.param.ContentQueryParam;
import com.freebe.code.business.meta.service.ContentService;
import com.freebe.code.business.meta.vo.ContentVO;
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
@RequestMapping("/content")
@CrossOrigin(origins = "*")
@Api(tags="内容接口")
public class ContentController {
	@Autowired
	private ContentService contentService;
	
	@ApiOperation(value = "创建或者更新内容")
	@PostMapping("createOrUpdate")
	public ResultBean<ContentVO> createOrUpdate(@Valid @RequestBody ContentParam param) throws CustomException {
		return ResultBean.ok(contentService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取内容")
	@GetMapping("get/{id}")
	public ResultBean<ContentVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(contentService.findById(id));
	}

	@ApiOperation(value = "查询内容")
	@PostMapping("list")
	public ResultBean<Page<ContentVO>> list(@Valid @RequestBody ContentQueryParam param) throws CustomException {
		return ResultBean.ok(contentService.queryPage(param));
	}
	
	@ApiOperation(value = "删除内容")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		contentService.softDelete(id);
		return ResultBean.ok();
	}

}
