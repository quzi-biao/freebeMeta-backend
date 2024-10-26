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

import com.freebe.code.business.meta.service.RichTextService;
import com.freebe.code.business.meta.vo.RichTextVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.freebe.code.business.meta.controller.param.RichTextParam;
import com.freebe.code.business.meta.controller.param.RichTextQueryParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zhengbiaoxie
 *
 */
@RestController
@RequestMapping("/richtext")
@CrossOrigin(origins = "*")
@Api(tags="问卷问题接口")
public class RichTextController {
	@Autowired
	private RichTextService richtextService;

	@ApiOperation(value = "创建或者更新问卷问题")
	@PostMapping("createOrUpdate")
	public ResultBean<RichTextVO> createOrUpdate(@Valid @RequestBody RichTextParam param) throws CustomException {
		return ResultBean.ok(richtextService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取问卷问题")
	@GetMapping("get/{id}")
	public ResultBean<RichTextVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(richtextService.findById(id));
	}

	@ApiOperation(value = "查询问卷问题")
	@PostMapping("list")
	public ResultBean<Page<RichTextVO>> list(@Valid @RequestBody RichTextQueryParam param) throws CustomException {
		return ResultBean.ok(richtextService.queryPage(param));
	}

	@ApiOperation(value = "删除问卷问题")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		richtextService.softDelete(id);
		return ResultBean.ok();
	}

}
