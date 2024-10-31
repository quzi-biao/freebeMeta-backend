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

import com.freebe.code.business.meta.controller.param.ContentDraftParam;
import com.freebe.code.business.meta.controller.param.ContentDraftQueryParam;
import com.freebe.code.business.meta.service.ContentDraftService;
import com.freebe.code.business.meta.service.ContentService;
import com.freebe.code.business.meta.vo.ContentDraftVO;
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
@RequestMapping("/contentdraft")
@CrossOrigin(origins = "*")
@Api(tags="内容草稿箱接口")
public class ContentDraftController {
	@Autowired
	private ContentDraftService contentdraftService;
	
	@Autowired
	private ContentService contentService;

	@ApiOperation(value = "创建或者更新内容草稿箱")
	@PostMapping("createOrUpdate")
	public ResultBean<ContentDraftVO> createOrUpdate(@Valid @RequestBody ContentDraftParam param) throws CustomException {
		return ResultBean.ok(contentdraftService.createOrUpdate(param));
	}
	
	@ApiOperation(value = "内容发布")
	@GetMapping("deploy/{id}")
	public ResultBean<ContentVO> createOrUpdate(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(contentService.deployFromDraft(id));
	}
	
	@ApiOperation(value = "获取内容草稿箱")
	@GetMapping("get/{id}")
	public ResultBean<ContentDraftVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(contentdraftService.findById(id));
	}

	@ApiOperation(value = "查询内容草稿箱")
	@PostMapping("list")
	public ResultBean<Page<ContentDraftVO>> list(@Valid @RequestBody ContentDraftQueryParam param) throws CustomException {
		return ResultBean.ok(contentdraftService.queryPage(param));
	}

	@ApiOperation(value = "删除内容草稿箱")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		contentdraftService.softDelete(id);
		return ResultBean.ok();
	}

}
