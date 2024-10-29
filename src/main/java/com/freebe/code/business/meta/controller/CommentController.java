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

import com.freebe.code.business.meta.service.CommentService;
import com.freebe.code.business.meta.vo.CommentVO;
import com.freebe.code.common.CustomException;
import com.freebe.code.common.ResultBean;
import com.freebe.code.business.meta.controller.param.CommentParam;
import com.freebe.code.business.meta.controller.param.CommentQueryParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @author zhengbiaoxie
 *
 */
@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = "*")
@Api(tags="评论接口")
public class CommentController {
	@Autowired
	private CommentService commentService;

	@ApiOperation(value = "创建或者更新评论")
	@PostMapping("createOrUpdate")
	public ResultBean<CommentVO> createOrUpdate(@Valid @RequestBody CommentParam param) throws CustomException {
		return ResultBean.ok(commentService.createOrUpdate(param));
	}

	@ApiOperation(value = "获取评论")
	@GetMapping("get/{id}")
	public ResultBean<CommentVO> get(@PathVariable("id") Long id) throws CustomException {
		return ResultBean.ok(commentService.findById(id));
	}

	@ApiOperation(value = "查询评论")
	@PostMapping("list")
	public ResultBean<Page<CommentVO>> list(@Valid @RequestBody CommentQueryParam param) throws CustomException {
		return ResultBean.ok(commentService.queryPage(param));
	}

	@ApiOperation(value = "删除评论")
	@DeleteMapping("{id}")
	public ResultBean<?> delete(@PathVariable("id") Long id) throws CustomException {
		commentService.softDelete(id);
		return ResultBean.ok();
	}

}
